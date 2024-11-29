package id.derysudrajat.benchmark

import android.content.Intent
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import id.derysudrajat.benchmark.util.DEFAULT_ITERATIONS
import id.derysudrajat.benchmark.util.TARGET_PACKAGE
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class FullyDrawnStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = listOf(StartupTimingMetric()),
        startupMode = StartupMode.COLD,
        iterations = DEFAULT_ITERATIONS,
    ) {
        val intent = Intent("$TARGET_PACKAGE.FULLY_DRAWN_STARTUP_ACTIVITY")

        // Waits for first rendered frame
        startActivityAndWait(intent)

        // Waits for an element that corresponds to fully drawn state
        device.wait(Until.hasObject(By.text("Fully Drawn")), 10_000)
    }
}
