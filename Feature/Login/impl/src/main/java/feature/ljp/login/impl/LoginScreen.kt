package feature.ljp.login.impl

import ai.ljp.designsystem.component.AIForumButton
import ai.ljp.designsystem.component.AIForumTab
import ai.ljp.designsystem.component.AIForumTabRow
import ai.ljp.designsystem.icon.AIForumIcon
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import feature.ljp.login.api.R
import kotlinx.coroutines.flow.SharedFlow


@Composable
internal fun LoginScreen(
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val loginUiState by viewModel.loginUiState.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val isLogin by viewModel.isLogin.collectAsStateWithLifecycle()
    val loginEvent = viewModel.loginEvent
    val registerEvent = viewModel.registerEvent
    LoginScreen(
        loginUiState = loginUiState,
        email = email,
        password = password,
        isLogin = isLogin,
        loginEvent = loginEvent,
        registerEvent = registerEvent,
        onHomeClick = onHomeClick,
        onLoginClick=viewModel::onLoginClick,
        onRegisterClick = viewModel::onRegisterClick,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onLoginUiChanged = viewModel::onLoginUiChanged,
        modifier = modifier.fillMaxSize()
    )
}
@Composable
internal fun LoginScreen(
    loginUiState: LoginUiState,
    email : String,
    password : String,
    isLogin : Boolean,
    loginEvent : SharedFlow<LoginEvent>,
    registerEvent: SharedFlow<RegisterEvent>,
    modifier: Modifier = Modifier,
    onHomeClick : () -> Unit,
    onLoginClick : (String, String) -> Unit,
    onRegisterClick : (String, String) -> Unit,
    onEmailChanged : (String) -> Unit,
    onPasswordChanged : (String) -> Unit,
    onLoginUiChanged : () -> Unit,
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        loginEvent.collect { event ->
            when(event) {
                is LoginEvent.Success -> {
                    onHomeClick()
                }
                is LoginEvent.Error -> {
                    Toast.makeText(context,event.msg, Toast.LENGTH_SHORT)
                }
            }
        }
    }
    LaunchedEffect(isLogin) {
        if (isLogin) {
            onHomeClick()
        }
    }
    LaunchedEffect(Unit) {
        registerEvent.collect { event ->
            when(event) {
                is RegisterEvent.Success -> Unit
                is RegisterEvent.Error -> {
                    Toast.makeText(context,event.msg, Toast.LENGTH_SHORT)
                }
            }
        }
    }
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize().padding(24.dp)
        ) {
            ProvideTextStyle(MaterialTheme.typography.headlineLarge) {
                Text(text = stringResource(R.string.feature_login_api_ai_community))
            }
            AIForumTabRow(
                selectedTabIndex = loginUiState.index,
                modifier = Modifier.fillMaxWidth()
            ) {
                AIForumTab(
                    selected = loginUiState.index == LoginUiState.Login().index,
                    onClick = onLoginUiChanged,
                    modifier = Modifier.padding(6.dp),
                    text = {
                        ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                            Text(text = stringResource(R.string.feature_login_api_login))
                        }
                    }
                )
                AIForumTab(
                    selected = loginUiState.index == LoginUiState.Register().index,
                    onClick = onLoginUiChanged,
                    modifier = Modifier.padding(6.dp),
                    text = {
                        ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                            Text(text = stringResource(R.string.feature_login_api_login))
                        }
                    }
                )
            }
            LoginTextField(
                focusRequester = focusRequester,
                input = email,
                onInputChanged = onEmailChanged,
                placeholderStringRes = R.string.feature_login_api_email_placeholder,
            )
            LoginTextField(
                focusRequester = focusRequester,
                input = password,
                onInputChanged = onPasswordChanged,
                placeholderStringRes = R.string.feature_login_api_password_placeholder,
            )
            LoginButton(
                loginUiState = loginUiState,
                email = email,
                password = password,
                onLoginClick = onLoginClick,
                onRegisterClick = onRegisterClick
            )
        }
    }
}
@Composable
private fun LoginTextField(
    focusRequester : FocusRequester,
    input : String,
    @StringRes placeholderStringRes : Int,
    onInputChanged : (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val onExplicitlyTriggered = {
        keyboardController?.hide()
    }
    TextField(
        value = input,
        onValueChange = {
            if ("\n" !in it) {
                onInputChanged(it)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        trailingIcon = {
            if (input.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onInputChanged("")
                    }
                ) {
                    Icon(
                        imageVector = AIForumIcon.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        singleLine = true,
        maxLines = 1,
        placeholder = {
            Text(text = stringResource(placeholderStringRes), color = Color.LightGray)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    if (input.isBlank()) return@onKeyEvent false
                    onExplicitlyTriggered()
                    true
                    true
                } else {
                    false
                }
            },
        shape = RoundedCornerShape(16.dp),
        keyboardActions = KeyboardActions(
            onSend = {
                if (input.isBlank()) return@KeyboardActions
                onExplicitlyTriggered()
            }
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send)

    )
}
@Composable
private fun LoginButton(
    loginUiState: LoginUiState,
    email : String,
    password : String,
    modifier: Modifier = Modifier,
    onLoginClick : (String, String) -> Unit,
    onRegisterClick : (String, String) -> Unit,
) {
    val onClick = when(loginUiState) {
        is LoginUiState.Login -> {
            { onLoginClick(email,password) }
        }
        is LoginUiState.Register -> {
            { onRegisterClick(email,password) }
        }
    }
    val text = when(loginUiState) {
        is LoginUiState.Login -> {
            stringResource(R.string.feature_login_api_login)
        }
        is LoginUiState.Register -> {
            stringResource(R.string.feature_login_api_register)
        }
    }
    AIForumButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = email.isNotBlank() && password.isNotBlank(),
        text = {
            Text(text = text, color = MaterialTheme.colorScheme.onPrimary)
        }
    )

}
