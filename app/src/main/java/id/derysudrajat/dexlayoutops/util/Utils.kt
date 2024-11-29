package id.derysudrajat.dexlayoutops.util

import android.content.Context
import android.util.TypedValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

const val TAG = "Perf Sample"

val KEY_USER = stringPreferencesKey("user")
val KEY_PASSWORD = stringPreferencesKey("password")

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

fun Int.dp(context: Context): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    )
