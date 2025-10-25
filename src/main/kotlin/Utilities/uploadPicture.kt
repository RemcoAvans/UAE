package com.example.Utilities

import com.example.dtos.utilities.pictureDto
import java.io.File

fun uploadPicture(data : pictureDto){

    val uploadDir = File("uploads")


    if (!uploadDir.exists()) { // controleren of die bestaat
        uploadDir.mkdirs() // Maakt de 'uploads' map aan
    }


    val fileToSave = File(uploadDir, data.fileName)


    fileToSave.writeBytes(data.fileBytes)

}