package com.example.quickpoll.ui.common.shared_components

import android.view.ViewTreeObserver
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quickpoll.R

@Composable
fun CustomOutlinedTextInput(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
) {
    var isVisible by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val view = LocalView.current
    val viewTreeObserver = view.viewTreeObserver
    DisposableEffect(viewTreeObserver) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            // ... do anything you want here with `isKeyboardOpen`
            if (!isKeyboardOpen) {
                focusManager.clearFocus()
            }
        }

        viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = {
            Text(
                placeholder, style = TextStyle(color = MaterialTheme.colorScheme.secondaryContainer)
            )
        },
        visualTransformation = if (isPassword) {
            if (isVisible) VisualTransformation.None else PasswordVisualTransformation()
        } else VisualTransformation.None,
        textStyle = MaterialTheme.typography.bodyMedium,
        colors = OutlinedTextFieldDefaults.colors().copy(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
            focusedSuffixColor = MaterialTheme.colorScheme.primary,
            unfocusedSuffixColor = MaterialTheme.colorScheme.primaryContainer
        ),
        suffix = {
            if (isPassword) {
                if (isVisible) {
                    Icon(painterResource(R.drawable.ic_eye),
                        "Toggle password visibility",
                        modifier = Modifier.clickable { isVisible = !isVisible })
                } else {
                    Icon(painterResource(R.drawable.ic_eye_off),
                        "Toggle password visibility",
                        modifier = Modifier.clickable { isVisible = !isVisible })
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
    )
}

@Preview
@Composable
private fun CustomOutlinedTextInputPreview() {
    CustomOutlinedTextInput(
        "Email", "Enter email", "", {}, isPassword = true
    )
}