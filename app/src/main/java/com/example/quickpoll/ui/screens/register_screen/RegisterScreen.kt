package com.example.quickpoll.ui.screens.register_screen

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
import com.example.quickpoll.ui.common.shared_components.CustomOutlinedTextInput
import com.example.quickpoll.ui.common.shared_components.CustomPrimaryButton
import com.example.quickpoll.ui.common.shared_components.FullScreenDialog
import com.example.quickpoll.utils.UiState

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel
) {
    val name = viewModel.name.collectAsStateWithLifecycle()
    val setName = viewModel::setName
    val email = viewModel.email.collectAsStateWithLifecycle()
    val setEmail = viewModel::setEmail
    val password = viewModel.password.collectAsStateWithLifecycle()
    val setPassword = viewModel::setPassword
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val handleRegister = viewModel::handleRegister
    val navController = LocalParentNavController.current
    fun register() {
        handleRegister({
            navController.popBackStack()
        })
    }
    RegisterScreenContent(
        name = name.value,
        setName = setName,
        email = email.value,
        setEmail = setEmail,
        password = password.value,
        setPassword = setPassword,
        uiState = uiState.value,
        handleRegister = ::register,
        goBack = { navController.popBackStack() }
    )
}

@Composable
private fun RegisterScreenContent(
    name: String,
    setName: (String) -> Unit,
    email: String,
    setEmail: (String) -> Unit,
    password: String,
    setPassword: (String) -> Unit,
    uiState: UiState,
    handleRegister: () -> Unit,
    goBack: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Text("Create Account!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(10.dp))
        CustomOutlinedTextInput(
            label = "Name",
            placeholder = "Enter your name",
            value = name,
            onValueChange = {
                setName(it)
            }
        )
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
            "Register",
            handleRegister
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text("Already have an account? ")
            Text("Login", style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            ), modifier = Modifier.clickable { goBack() })
        }
    }
    FullScreenDialog(uiState == UiState.LOADING)
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    RegisterScreenContent(
        name = "test",
        setName = {},
        email = "test",
        setEmail = {},
        password = "test",
        setPassword = {},
        uiState = UiState.IDLE,
        handleRegister = {},
        goBack = {}
    )
}