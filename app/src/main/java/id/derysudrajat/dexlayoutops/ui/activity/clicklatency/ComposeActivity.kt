package id.derysudrajat.dexlayoutops.ui.activity.clicklatency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tracing.trace
import id.derysudrajat.dexlayoutops.ui.recylerview.Entry
import id.derysudrajat.dexlayoutops.util.ClickTrace

@OptIn(ExperimentalComposeUiApi::class)
class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This argument allows the Macrobenchmark tests control the content being tested.
        // In your app, you could use this approach to navigate to a consistent UI.
        // e.g. Here the UI is being populated with a well known number of list items.
        val itemCount = intent.getIntExtra(EXTRA_ITEM_COUNT, 1000)
        val data = entries(itemCount)

        setContent {
            BenchmarkComposeList(data)
        }
    }

    @Composable
    private fun BenchmarkComposeList(data: List<Entry>) {
        MaterialTheme {
            Box(
                modifier = Modifier.semantics {
                    // Allows to use testTag() for UiAutomator's resource-id.
                    // It can be enabled high in the compose hierarchy,
                    // so that it's enabled for the whole subtree
                    testTagsAsResourceId = true
                }
            ) {
                // Thanks to [SemanticsPropertyReceiver.testTagsAsResourceId],
                // [Modifier.testTag]s will be propagated to resource-id
                // and can be accessed from benchmarks.
                var value by remember { mutableStateOf("Enter text here") }
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input"),
                        value = value,
                        onValueChange = { value = it },
                        placeholder = { Text("Enter text here") }
                    )

                    LazyColumn(
                        modifier = Modifier
                            .testTag("myLazyColumn")
                    ) {
                        items(data, key = { it.contents }) { item ->
                            EntryRow(
                                entry = item,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        ClickTrace.onClickPerformed()
                                        AlertDialog
                                            .Builder(this@ComposeActivity)
                                            .setMessage("Item clicked")
                                            .show()
                                    }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun entries(size: Int) = List(size) {
        Entry("Item $it")
    }

    companion object {
        const val EXTRA_ITEM_COUNT = "ITEM_COUNT"
    }
}

@Composable
private fun EntryRow(entry: Entry, modifier: Modifier = Modifier) = trace("EntryRowCustomTrace") {
    Card(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = entry.contents,
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize()
            )

            Spacer(modifier = Modifier.weight(1f))

            Checkbox(
                checked = false,
                onCheckedChange = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun EntryRowPreview() {
    val entry = Entry("Sample text")
    EntryRow(entry = entry, Modifier.padding(8.dp))
}
