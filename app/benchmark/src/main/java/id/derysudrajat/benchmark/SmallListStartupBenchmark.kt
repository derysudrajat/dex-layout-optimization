package id.derysudrajat.benchmark

import android.content.Intent
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.filters.LargeTest
import id.derysudrajat.benchmark.util.DEFAULT_ITERATIONS
import id.derysudrajat.benchmark.util.TARGET_PACKAGE
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

private const val LIST_ITEMS = 5

@LargeTest
@RunWith(Parameterized::class)
class SmallListStartupBenchmark(
    private val startupMode: StartupMode
) {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = listOf(StartupTimingMetric()),
        iterations = DEFAULT_ITERATIONS,
        startupMode = startupMode,
        setupBlock = {
            // Press home button before each run to ensure the starting activity isn't visible.
            pressHome()
        }
    ) {
        val intent = Intent("$packageName.RECYCLER_VIEW_ACTIVITY")
        intent.putExtra("ITEM_COUNT", LIST_ITEMS)

        startActivityAndWait(intent)
    }

    @Test
    fun startupCompose() = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = listOf(StartupTimingMetric()),
        iterations = DEFAULT_ITERATIONS,
        startupMode = startupMode,
        setupBlock = {
            // Press home button before each run to ensure the starting activity isn't visible.
            pressHome()
        }
    ) {
        val intent = Intent("$packageName.COMPOSE_ACTIVITY")
        intent.putExtra("ITEM_COUNT", LIST_ITEMS)

        startActivityAndWait(intent)
    }

    companion object {
        @Parameterized.Parameters(name = "mode={0}")
        @JvmStatic
        fun parameters(): List<Array<Any>> {
            return listOf(
                StartupMode.COLD,
                StartupMode.WARM
            ).map { arrayOf(it) }
        }
    }
}
