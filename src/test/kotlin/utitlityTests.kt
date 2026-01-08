import com.example.Utilities.uploadPicture
import com.example.dtos.utilities.pictureDto
import com.example.usecase.activity.getPhotoUseCase
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class utitlityTests {

    @Test
    fun `uploadPicture should create file and return correct path in task`(){
        val pictureDto = pictureDto("test.jpg", "HelloWorld".toByteArray())

        // Zorg dat de uploads-map schoon is
        val uploadDir = File("uploads")
        if (uploadDir.exists()) uploadDir.deleteRecursively()

        // Act
        val result = uploadPicture(pictureDto)

        // Assert
        val savedFile = File(uploadDir, "test.jpg")
        assertTrue(savedFile.exists(), "Bestand moet zijn aangemaakt")
        assertEquals("/uploads/test.jpg", result.result, "Return pad moet correct zijn")
        assertEquals("HelloWorld", savedFile.readText(), "Bestand moet correcte inhoud hebben")

        // Clean up
        uploadDir.deleteRecursively()
    }

    @Test
    fun `getPicture should find saved photo`() {
        // Arrange
        val fileName = "test_get_picture.jpg"
        val content = "Test content".toByteArray()
        val uploadDir = File("uploads")
        if (!uploadDir.exists()) uploadDir.mkdirs()
        val testFile = File(uploadDir, fileName)
        testFile.writeBytes(content)

        val useCase = getPhotoUseCase()

        // Act
        val result = useCase.getPicture(fileName)

        // Assert
        assertTrue(result.isNotEmpty(), "Resultaat mag niet leeg zijn")
        assertEquals("Test content", String(result), "Inhoud moet overeenkomen")

        // Clean up
        testFile.delete()
    }

}