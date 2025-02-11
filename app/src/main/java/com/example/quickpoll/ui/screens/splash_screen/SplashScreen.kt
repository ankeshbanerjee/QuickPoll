package com.example.quickpoll.ui.screens.splash_screen

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quickpoll.LocalParentNavController
import com.example.quickpoll.Login
import com.example.quickpoll.Main
import com.example.quickpoll.R
import com.example.quickpoll.Splash
import com.example.quickpoll.utils.PreferencesDataStoreHelper
import com.example.quickpoll.utils.PreferencesDataStoreKey
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SplashScreen() {
    val navController = LocalParentNavController.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val key = rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(key) {
        Handler(Looper.getMainLooper()).postDelayed({
            scope.launch {
                PreferencesDataStoreHelper.retrieveData(context, PreferencesDataStoreKey.AUTH_TOKEN)
                    .collectLatest { authToken ->
                        Log.d("AUTH_TOKEN", authToken ?: "No Auth Token")
                        if (authToken != null) {
                            navController.navigate(Main) {
                                popUpTo<Splash> {
                                    inclusive = true
                                }
                            }
                        } else {
                            navController.navigate(Login) {
                                popUpTo<Splash> {
                                    inclusive = true
                                }
                            }
                        }
                    }
            }
        }, 1000)
    }
    SplashScreenContent()
}

@Composable
private fun SplashScreenContent() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_quickpoll),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "QuickPoll",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreenContent()
}