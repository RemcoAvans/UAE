package com.example.usecase.activity

import io.ktor.server.http.content.staticFiles
import java.io.File

class getPhotoUseCase() {
    fun getPicture(photoId : String) : ByteArray{
        // 1. Schoon de bestandsnaam op (voorkom dubbele // of mappenstructuren)
        val bestandsnaam = photoId.substringAfterLast('/')

        // 2. Bepaal de root map van waaruit de applicatie draait
        // System.getProperty("user.dir") geeft de map waar het project in staat
        val projectMap = File(System.getProperty("user.dir"))

        // 3. Bouw het pad naar de uploads map
        val uploadMap = File(projectMap, "uploads")

        // 4. VEILIGHEID: Maak de map aan als hij niet bestaat (voor je teamgenoten)
        if (!uploadMap.exists()) {
            uploadMap.mkdirs() // Maakt de map aan
            println("Map 'uploads' bestond niet, is nu aangemaakt op: ${uploadMap.absolutePath}")
            return ByteArray(0) // Geen foto, want de map is net nieuw
        }

        val bestand = File(uploadMap, bestandsnaam)

        println("Zoeken naar bestand: ${bestand.absolutePath}") // Debug info

        return bestand.readBytes()
    }
}
