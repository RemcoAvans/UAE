package com.example.Utilities
import com.example.dtos.utilities.splitMultipartDataAndPicturedDto
import io.ktor.utils.io.core.*
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readByteArray
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray


suspend fun splitMultipartDataAndPicture(multipartData: MultiPartData): splitMultipartDataAndPicturedDto {

    // Initialiseer de variabelen die je wilt retourneren
    var jsonString: String? = null
    var fileRead : ByteReadChannel? = null
    var fileBytes: ByteArray? = null
    var fileName: String? = null

    // De 'sportActivity' en 'fileDescription' heb je niet nodig
    // voor dit specifieke return-type.

    multipartData.forEachPart { part ->
        when (part) {
            is PartData.FormItem -> {
                if (part.name == "form-data")
                jsonString = part.value
            }
            is PartData.FileItem -> {
                fileName = part.originalFileName

                // LEES DE BYTES: Dit is de belangrijkste wijziging.
                // Sla het bestand niet op, maar lees het direct in het geheugen.
                fileRead = part.provider()

                fileBytes = fileRead.readRemaining().readByteArray()

                // Deze regels zijn niet meer nodig:
                // val file = File("uploads/$fileName")
                // part.provider().copyAndClose(file.writeChannel())
            }
            else -> {}
        }
        part.dispose() // Goed dat je dit doet!
    }


    
    return splitMultipartDataAndPicturedDto(jsonString,fileBytes, fileName)

    // Retourneer een Pair (wat (ByteArray, String) eigenlijk is)
    // met de ingelezen bytes en de bestandsnaam.

}