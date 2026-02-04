package ai.ljp.network.model

data class LoginResponse(
    val accessToken : String,
    val email : String,
    val userId : String
)

data class RegisterResponse(
    val userId: String,
    val email: String,
    val accessToken: String?
)