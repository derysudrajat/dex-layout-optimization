package id.derysudrajat.dexlayoutops.ui.activity.clicklatency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import id.derysudrajat.dexlayoutops.databinding.ActivityNestedRvBinding
import id.derysudrajat.dexlayoutops.ui.recylerview.NestedRecyclerViewModel
import id.derysudrajat.dexlayoutops.ui.recylerview.ParentAdapter
import kotlinx.coroutines.launch

class NestedRecyclerActivity : ComponentActivity() {

    private val viewModel by viewModels<NestedRecyclerViewModel>()

    private lateinit var binding: ActivityNestedRvBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val useRecyclerPools = intent.getBooleanExtra(USE_RECYCLER_POOLS, false)

        binding = ActivityNestedRvBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val adapter = ParentAdapter(useRecyclerPools)

        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect {
                    adapter.submitList(it)
                }
            }
        }
    }
}

const val USE_RECYCLER_POOLS = "USE_RECYCLER_POOLS"
