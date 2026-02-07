package ai.ljp.network.model
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
@OptIn(InternalSerializationApi::class)
@Serializable
data class LoginResponse(
    val accessToken : String,
    val email : String,
    val userId : String
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class RegisterResponse(
    val userId: String,
    val email: String,
    val accessToken: String?
)