package model
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(val token : String,
                         val expiresAt: Long? = null,   // Timestamp van vervaldatum
                         val username: String? = null)
