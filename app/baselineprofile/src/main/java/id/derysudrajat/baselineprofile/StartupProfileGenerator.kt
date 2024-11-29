package id.derysudrajat.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Generates a startup profile.
 * See
 * [the documentation](https://d.android.com//topic/performance/baselineprofiles/dex-layout-optimizations)
 * for details.
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class StartupProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun profileGenerator() {
        // do something
    }
}