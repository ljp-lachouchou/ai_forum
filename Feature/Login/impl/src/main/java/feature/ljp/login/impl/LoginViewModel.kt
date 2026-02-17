package feature.ljp.login.impl

import ai.ljp.data.repository.ProfileRepository
import ai.ljp.data.repository.UserDataRepository
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userDataRepository: UserDataRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    val email : StateFlow<String> = savedStateHandle.getStateFlow(EMAIL_KEY,"")
    val password : StateFlow<String> = savedStateHandle.getStateFlow(PASSWORD_KEY,"")
    val isLogin : StateFlow<Boolean> = userDataRepository
        .userData.map { it.currentUserId != null && it.authToken != null }
        .stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5_000)
        )
    private val _loginEvent = MutableSharedFlow<LoginEvent>(extraBufferCapacity = 1)
    val loginEvent = _loginEvent.asSharedFlow()

    private val _registerEvent = MutableSharedFlow<RegisterEvent>(extraBufferCapacity = 1)
    val registerEvent = _registerEvent.asSharedFlow()

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Login())
    val loginUiState : StateFlow<LoginUiState> = _loginUiState
    fun onLoginClick(email : String,password : String) {
        viewModelScope.launch {
            val success = profileRepository.login(email,password)
            if (success) {
                _loginEvent.emit(LoginEvent.Success)
            } else {
                _loginEvent.emit(LoginEvent.Error("登录失败"))
            }
        }
    }
    fun onRegisterClick(email: String,password: String) {
        viewModelScope.launch {
            val success = profileRepository.login(email,password)
            if (success) {
                _registerEvent.emit(RegisterEvent.Success)
            } else {
                _registerEvent.emit(RegisterEvent.Error("注册失败"))
            }
        }
    }
    fun onEmailChanged(email: String) {
        savedStateHandle[EMAIL_KEY] = email
    }
    fun onPasswordChanged(password: String) {
        savedStateHandle[PASSWORD_KEY] = password
    }
    fun onLoginUiChanged() {
        when(loginUiState.value) {
            is LoginUiState.Login -> _loginUiState.value = LoginUiState.Register()
            is LoginUiState.Register -> _loginUiState.value = LoginUiState.Login()
        }
    }
}
sealed interface LoginEvent {
    object Success : LoginEvent
    data class Error(val msg : String) : LoginEvent
}

sealed interface RegisterEvent {
    object Success : RegisterEvent
    data class Error(val msg : String) : RegisterEvent
}
sealed class LoginUiState(open val index: Int = -1) {
    data class Login(override val index : Int = 0) : LoginUiState()
    data class Register(override val index : Int = 1) : LoginUiState()
}

private const val EMAIL_KEY = "emailKey"
private const val PASSWORD_KEY = "passwordKey"