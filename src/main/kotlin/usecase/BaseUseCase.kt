package usecase

import com.example.core.ObjectResult
import model.Activity

interface BaseUseCase<TOutput> {
    suspend fun execute(): ObjectResult<TOutput>
}