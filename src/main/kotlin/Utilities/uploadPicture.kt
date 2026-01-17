package com.example.Utilities

import com.example.core.ObjectResult
import com.example.dtos.utilities.pictureDto
import com.example.repository.IActivityRepository
import java.io.File
import java.util.UUID

fun uploadPicture(data: pictureDto): ObjectResult<String> {

    val uploadDir = File("uploads")
    if (!uploadDir.exists()) { // controleren of die bestaat
        uploadDir.mkdirs() // Maakt de 'uploads' map aan
    }
    // 1. Haal de extensie op van de originele bestandsnaam (bv. "jpg")
    val extension = File(data.fileName).extension

    // 2. Maak de nieuwe unieke naam: UUID + punt + extensie
    val uniqueFileName = "${UUID.randomUUID()}.$extension"

    // 3. Gebruik deze nieuwe naam om het bestand aan te maken
    val fileToSave = File(uploadDir, uniqueFileName)

    fileToSave.writeBytes(data.fileBytes)

    // 4. Geef het pad met de NIEUWE naam terug
    return ObjectResult.success("/uploads/$uniqueFileName")
}
