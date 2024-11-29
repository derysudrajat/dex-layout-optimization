package id.derysudrajat.dexlayoutops.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.Random

/** Pretending to load data from a remote source */
class SampleData {
    private val random = Random()
    suspend fun isReady(): Boolean {
        withContext(Dispatchers.Default) {
            delay(1000 + random.nextInt(1000).toLong())
        }
        return true
    }
}
