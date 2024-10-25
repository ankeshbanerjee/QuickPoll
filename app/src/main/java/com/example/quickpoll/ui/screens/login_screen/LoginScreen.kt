package com.example.quickpoll.ui.screens.login_screen

import android.provider.Contacts.Intents.UI
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quickpoll.LocalParentNavController
import com.example.quickpoll.Login
import com.example.quickpoll.Main
import com.example.quickpoll.Register
import com.example.quickpoll.Splash
import com.example.quickpoll.ui.components.CustomOutlinedTextInput
import com.example.quickpoll.ui.components.CustomPrimaryButton
import com.example.quickpoll.ui.components.FullScreenDialog
import com.example.quickpoll.utils.UiState

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val email = viewModel.email.collectAsStateWithLifecycle()
    val setEmail = viewModel::setEmail
    val password = viewModel.password.collectAsStateWithLifecycle()
    val setPassword = viewModel::setPassword
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val handleLogin = viewModel::handleLogin
    val navController = LocalParentNavController.current
    fun navigateToMain() {
        navController.navigate(Main) {
            popUpTo<Splash> {
                inclusive = true
            }
        }
    }

    fun login() = handleLogin(::navigateToMain)
    LoginScreenContent(
        email.value, setEmail, password.value, setPassword, uiState.value, ::login, {
            navController.navigate(Register)
        }
    )
}

@Composable
private fun LoginScreenContent(
    email: String,
    setEmail: (String) -> Unit,
    password: String,
    setPassword: (String) -> Unit,
    uiState: UiState,
    handleLogin: () -> Unit,
    goToRegister: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Text("Welcome User!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(10.dp))
        CustomOutlinedTextInput(
            label = "Email",
            placeholder = "Enter your email",
            value = email,
            onValueChange = {
                setEmail(it)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        CustomOutlinedTextInput(
            label = "Password",
            placeholder = "Enter your password",
            value = password,
            onValueChange = {
                setPassword(it)
            },
            isPassword = true
        )
        Spacer(modifier = Modifier.height(18.dp))
        CustomPrimaryButton(
            "Login",
            handleLogin
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text("Don't have an account? ")
            Text("Register", style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            ), modifier = Modifier.clickable { goToRegister() })
        }
    }
    FullScreenDialog(uiState == UiState.LOADING)
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreenContent(
        "", {}, "", {}, UiState.IDLE, {}, {}
    )
}