package com.example.usecase

import com.example.core.ObjectResult

interface BaseInputUseCase<in TInput, TOutput> {
    suspend fun execute(input: TInput): ObjectResult<TOutput>
}