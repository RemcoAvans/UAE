package com.example.config

import org.junit.Assert.*
import org.junit.Test

class PasswordHasherTest {

    @Test
    fun `should hash password successfully`() {
        // Arrange
        val password = "mySecurePassword123"

        // Act
        val hashed = PasswordHasher.hash(password)

        // Assert
        assertNotNull(hashed)
        assertNotEquals(password, hashed)
        assertTrue(hashed.length > 50) // BCrypt hashes are typically 60 characters
    }

    @Test
    fun `should verify correct password`() {
        // Arrange
        val password = "mySecurePassword123"
        val hashed = PasswordHasher.hash(password)

        // Act
        val result = PasswordHasher.verify(password, hashed)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `should not verify incorrect password`() {
        // Arrange
        val password = "mySecurePassword123"
        val wrongPassword = "wrongPassword456"
        val hashed = PasswordHasher.hash(password)

        // Act
        val result = PasswordHasher.verify(wrongPassword, hashed)

        // Assert
        assertFalse(result)
    }

    @Test
    fun `should generate different hashes for same password`() {
        // Arrange
        val password = "mySecurePassword123"

        // Act
        val hash1 = PasswordHasher.hash(password)
        val hash2 = PasswordHasher.hash(password)

        // Assert
        assertNotEquals(hash1, hash2) // BCrypt uses salt, so same password produces different hashes
        assertTrue(PasswordHasher.verify(password, hash1))
        assertTrue(PasswordHasher.verify(password, hash2))
    }

    @Test
    fun `should handle empty password`() {
        // Arrange
        val password = ""

        // Act
        val hashed = PasswordHasher.hash(password)
        val result = PasswordHasher.verify(password, hashed)

        // Assert
        assertNotNull(hashed)
        assertTrue(result)
    }

    @Test
    fun `should handle long password`() {
        // Arrange
        val password = "a".repeat(100)

        // Act
        val hashed = PasswordHasher.hash(password)
        val result = PasswordHasher.verify(password, hashed)

        // Assert
        assertNotNull(hashed)
        assertTrue(result)
    }

    @Test
    fun `should handle special characters in password`() {
        // Arrange
        val password = "p@ssw0rd!#\$%^&*()_+{}[]|:;<>?,./"

        // Act
        val hashed = PasswordHasher.hash(password)
        val result = PasswordHasher.verify(password, hashed)

        // Assert
        assertNotNull(hashed)
        assertTrue(result)
    }

    @Test
    fun `should be case sensitive`() {
        // Arrange
        val password = "MyPassword123"
        val hashed = PasswordHasher.hash(password)

        // Act
        val resultLower = PasswordHasher.verify("mypassword123", hashed)
        val resultUpper = PasswordHasher.verify("MYPASSWORD123", hashed)
        val resultCorrect = PasswordHasher.verify("MyPassword123", hashed)

        // Assert
        assertFalse(resultLower)
        assertFalse(resultUpper)
        assertTrue(resultCorrect)
    }
}
