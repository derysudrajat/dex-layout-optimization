package id.derysudrajat.dexlayoutops.ui.activity.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ReportDrawn
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import id.derysudrajat.dexlayoutops.MainActivity
import id.derysudrajat.dexlayoutops.util.SampleViewModel

import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
class LoginActivity : ComponentActivity() {

    private val sampleViewModel by viewModels<SampleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.run {
            val userName = getString("user", "")
            val password = getString("password", "")
            if (userName.isNotEmpty() && password.isNotEmpty()) {
                sampleViewModel.loginSynchronous(userName, password)
            }
        }
        setContent {
            LoginScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginScreen() {
        var userName by remember {
            mutableStateOf(sampleViewModel.login.userName)
        }
        var password by remember {
            mutableStateOf(sampleViewModel.login.password)
        }
        MaterialTheme {
            Box(
                modifier = Modifier.semantics {
                    // Allows to use testTag() for UiAutomator's resource-id.
                    // It can be enabled high in the compose hierarchy,
                    // so that it's enabled for the whole subtree
                    testTagsAsResourceId = true
                }
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopAppBar(title = {
                        Text(text = "Please login to this sample")
                    })
                    InputField(
                        title = "User: ",
                        value = userName,
                        testTag = "userName",
                        onValueChange = { userName = it })
                    InputField(
                        title = "Password: ",
                        value = password,
                        testTag = "password",
                        onValueChange = {
                            password = it
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Button(
                        modifier = Modifier.testTag("login"),
                        enabled = userName.isNotEmpty() && password.isNotEmpty(),
                        onClick = {
                            sampleViewModel.viewModelScope.launch {
                                sampleViewModel.login(userName, password)
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        MainActivity::class.java
                                    )
                                )
                                finish()
                            }
                        }) {
                        Text("Login")
                    }
                    Text(
                        text = "Do not put any actual userdata here, only data for testing purposes.",
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }

        ReportDrawn()
    }

    @Composable
    fun InputField(
        title: String,
        value: String,
        testTag: String,
        onValueChange: (String) -> Unit,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        keyboardOptions: KeyboardOptions = KeyboardOptions()
    ) {
        Surface(shape = MaterialTheme.shapes.small, modifier = Modifier.padding(4.dp)) {
            Text(text = title, modifier = Modifier.padding(start = 4.dp))
            TextField(
                modifier = Modifier.testTag(testTag),
                value = value,
                singleLine = true,
                onValueChange = onValueChange,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions
            )
        }
    }
}

