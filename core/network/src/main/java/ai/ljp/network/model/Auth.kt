package ai.ljp.network.model
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@OptIn(InternalSerializationApi::class)
@Serializable
data class LoginResponse(
    @SerialName("access_token")
    val accessToken : String,
    val email : String,
    @SerialName("user_id")
    val userId : String
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class RegisterResponse(
    @SerialName("user_id")
    val userId: String,
    val email: String,
    @SerialName("access_token")
    val accessToken: String?
)