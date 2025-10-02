package usecase

import com.example.core.ObjectResult

interface BaseUseCase<TOutput> {
    suspend fun execute(): ObjectResult<TOutput>
}