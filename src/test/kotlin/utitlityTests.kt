import com.example.Utilities.uploadPicture
import com.example.dtos.utilities.pictureDto
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

}