package id.derysudrajat.dexlayoutops

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.profileinstaller.ProfileVerifier
import id.derysudrajat.dexlayoutops.ui.activity.FullyDrawnStartupActivity
import id.derysudrajat.dexlayoutops.ui.activity.clicklatency.ComposeActivity
import id.derysudrajat.dexlayoutops.ui.activity.clicklatency.ListViewActivity
import id.derysudrajat.dexlayoutops.ui.activity.clicklatency.NestedRecyclerActivity
import id.derysudrajat.dexlayoutops.ui.activity.clicklatency.NonExportedRecyclerActivity
import id.derysudrajat.dexlayoutops.ui.activity.clicklatency.ScrollViewActivity
import id.derysudrajat.dexlayoutops.ui.activity.clicklatency.USE_RECYCLER_POOLS
import id.derysudrajat.dexlayoutops.ui.activity.login.LoginActivity
import id.derysudrajat.dexlayoutops.util.ClickTrace
import id.derysudrajat.dexlayoutops.util.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Column(
                    modifier = Modifier.semantics {
                        testTagsAsResourceId = true
                    }
                ) {
                    TopAppBar(title = { Text(text = "Benchmark Sample Target App") })
                    ActivityList()
                }
            }

        }
        lifecycleScope.launch {
            logCompilationStatus()
        }
    }

    @Composable
    fun ActivityList() {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = "This app runs macrobenchmark tests",
            textAlign = TextAlign.Center
        )
        Column {
            BenchmarkActivityButton(name = "Login") {
                launchActivityWithTrace<LoginActivity>()
            }
            BenchmarkActivityButton(name = "ListView") {
                launchActivityWithTrace<ListViewActivity>()
            }
            BenchmarkActivityButton(name = "Compose") {
                launchActivityWithTrace<ComposeActivity>()
            }
            BenchmarkActivityButton(name = "ScrollView") {
                launchActivityWithTrace<ScrollViewActivity>()
            }
            BenchmarkActivityButton(name = "Fully Drawn") {
                launchActivityWithTrace<FullyDrawnStartupActivity>()
            }
            BenchmarkActivityButton(
                name = "RecyclerView",
                "launchRecyclerActivity"
            ) {
                launchActivityWithTrace<NonExportedRecyclerActivity>()
            }
            BenchmarkActivityButton(
                name = "Nested RecyclerView",
                "nestedRecyclerActivity"
            ) {
                launchActivityWithTrace<NestedRecyclerActivity>()
            }
            BenchmarkActivityButton(
                name = "Nested RecyclerView with Pools",
                "nestedRecyclerWithPoolsActivity"
            ) {
                launchActivityWithTrace<NestedRecyclerActivity>(
                    Intent().putExtra(
                        USE_RECYCLER_POOLS, true
                    )
                )
            }
        }
    }

    @Composable
    fun BenchmarkActivityButton(name: String, testTag: String = "", onClick: () -> Unit) {
        TextButton(
            modifier = Modifier
                .padding(8.dp)
                .testTag(testTag),
            onClick = onClick,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Text(name)
        }
    }

    private inline fun <reified T : Activity> launchActivityWithTrace(base: Intent? = null) {
        ClickTrace.onClickPerformed()
        val intent = Intent(this, T::class.java)
        if (base != null) {
            intent.putExtras(base)
        }
        startActivity(intent)
    }

    /**
     * Logs the app's Baseline Profile Compilation Status using [ProfileVerifier].
     */
    private suspend fun logCompilationStatus() {
        withContext(Dispatchers.IO) {

            /*
            To verify profile installation locally, you need to either compile the app
            with the speed-profile like so:
             `adb shell cmd package compile -f -m speed-profile com.example.macrobenchmark.target`

            Or trigger background dex optimizations manually like so:
            `adb shell pm bg-dexopt-job`
            As these run in the background it might take a while for this to complete.

            To see quick turnaround of the ProfileVerifier, we recommend using `speed-profile`.
            If you don't do either of these steps, you might only see the profile being enqueued for
            compilation when running the sample locally.
             */
            val status = ProfileVerifier.getCompilationStatusAsync().await()
            Log.d(TAG, "ProfileInstaller status code: ${status.profileInstallResultCode}")
            Log.d(
                TAG,
                when {
                    status.isCompiledWithProfile -> "ProfileInstaller: is compiled with profile"
                    status.hasProfileEnqueuedForCompilation() -> "ProfileInstaller: Enqueued for compilation"
                    else -> "Profile not compiled or enqueued"
                }
            )
        }
    }
}