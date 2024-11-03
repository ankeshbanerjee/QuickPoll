package com.example.quickpoll.ui.screens.add_poll_screen

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quickpoll.LocalParentNavController
import com.example.quickpoll.R
import com.example.quickpoll.ui.common.shared_components.CustomOutlinedTextInput
import com.example.quickpoll.ui.common.shared_components.CustomPrimaryButton
import com.example.quickpoll.ui.common.shared_components.FullScreenDialog
import com.example.quickpoll.utils.UiState
import com.example.quickpoll.utils.getFileFromUri
import com.example.quickpoll.utils.showToast
import kotlinx.coroutines.launch
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddPollScreen(viewModel: AddPollViewModel) {
    val navController = LocalParentNavController.current
    fun goBack() = navController.popBackStack()
    val pollQuestion by viewModel.pollQuestion.collectAsStateWithLifecycle()
    val setPollQuestion = viewModel::setPollQuestion
    val options by viewModel.options.collectAsStateWithLifecycle()
    fun addOption(option: String) = viewModel.addOption(option)
    fun deleteOption(option: String) = viewModel.deleteOption(option)
    fun createPoll() = viewModel.createPoll(::goBack)
    fun uploadImage(file: File?) = viewModel.uploadImage(file)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val imageUrl by viewModel.imageurl.collectAsStateWithLifecycle()

    AddPollScreenContent(
        goBack = ::goBack,
        pollQuestion = pollQuestion,
        setPollQuestion = setPollQuestion,
        options = options,
        addOption = ::addOption,
        deleteOption = ::deleteOption,
        createPoll = ::createPoll,
        uploadImage = ::uploadImage,
        imageUrl = imageUrl,
        uiState = uiState
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPollScreenContent(
    goBack: () -> Unit,
    pollQuestion: String,
    setPollQuestion: (String) -> Unit,
    options: List<String>,
    addOption: (String) -> Unit,
    deleteOption: (String) -> Unit,
    createPoll: () -> Unit,
    uploadImage: (File?) -> Unit,
    imageUrl: String?,
    uiState: UiState
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = createPoll,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Icon(Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable { goBack() })
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Add Poll", style = MaterialTheme.typography.headlineSmall)
            }
            CustomOutlinedTextInput(
                label = "Question",
                placeholder = "Enter Your Question",
                value = pollQuestion,
                onValueChange = setPollQuestion,
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(options.size) { index ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 14.dp)
                    ) {
                        Text(
                            text = options[index],
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.Outlined.Delete,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.clickable { deleteOption(options[index]) })
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            CustomPrimaryButton(
                text = "Add Option", onClick = {
                    showBottomSheet = true
                }, icon = Icons.Filled.Add
            )
            Spacer(modifier = Modifier.height(16.dp))
            val context = LocalContext.current
            val pickMedia =
                rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                    if (uri != null) {
                        val file = getFileFromUri(contentResolver = context.contentResolver, uri = uri)
                        uploadImage(file)
                    }
                }
            CustomPrimaryButton(
                text = "Upload Image", onClick = {
                    pickMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }, icon = ImageVector.vectorResource(R.drawable.ic_upload)
            )
            Spacer(modifier = Modifier.height(8.dp))
            imageUrl?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Image uploaded", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
    FullScreenDialog(uiState == UiState.LOADING)
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            }, sheetState = sheetState
        ) {
            // Sheet content
            var option by rememberSaveable { mutableStateOf("") }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Add Option", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                CustomOutlinedTextInput(
                    label = "Option",
                    placeholder = "Enter Option",
                    onValueChange = {
                        option = it
                    },
                    value = option,
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomPrimaryButton(
                    text = "Add Option", onClick = {
                        scope.launch {
                            addOption(option)
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }, icon = Icons.Filled.Add
                )
            }
        }
    }
}


@Preview
@Composable
private fun AddPollScreenPreview() {
    AddPollScreenContent(
        goBack = {},
        pollQuestion = "",
        setPollQuestion = {},
        options = listOf("Option 1", "Option 2"),
        addOption = {},
        deleteOption = {},
        createPoll = {},
        uploadImage = {},
        imageUrl = null,
        uiState = UiState.IDLE
    )
}