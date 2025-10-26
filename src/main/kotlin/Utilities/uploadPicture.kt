package com.example.Utilities

import com.example.core.ObjectResult
import com.example.dtos.utilities.pictureDto
import com.example.repository.IActivityRepository
import java.io.File

fun uploadPicture(data: pictureDto): ObjectResult<String> {

    val uploadDir = File("uploads")
    if (!uploadDir.exists()) { // controleren of die bestaat
        uploadDir.mkdirs() // Maakt de 'uploads' map aan
    }
    val fileToSave = File(uploadDir, data.fileName)
    fileToSave.writeBytes(data.fileBytes)
    return ObjectResult.success("/uploads/${data.fileName}")
}
