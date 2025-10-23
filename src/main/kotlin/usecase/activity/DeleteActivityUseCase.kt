package com.example.usecase.activity

import com.example.core.ObjectResult
import com.example.repository.IActivityRepository
import com.example.usecase.BaseInputUseCase

class DeleteActivityUseCase(val repository: IActivityRepository) : BaseInputUseCase<Int?, Boolean> {

    override suspend fun execute(input: Int?): ObjectResult<Boolean> {
        if (input == null || input < 0) {
            return ObjectResult.fail("Id is niet validen.")
        }
        val success = repository.delete(input)
        if (!success){
            return ObjectResult.notFound("Activiteit is niet gevonden.")
        }
        return ObjectResult.success(true)
    }
}