package id.derysudrajat.dexlayoutops.ui.activity.clicklatency

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import id.derysudrajat.dexlayoutops.R
import id.derysudrajat.dexlayoutops.databinding.ActivityScrollViewBinding
import id.derysudrajat.dexlayoutops.ui.recylerview.Entry
import id.derysudrajat.dexlayoutops.util.ClickTrace

/**
 * An activity displaying a large ScrollView.
 */
class ScrollViewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "ScrollView Sample"
        val binding = ActivityScrollViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val itemCount = intent.getIntExtra(RecyclerViewActivity.EXTRA_ITEM_COUNT, 1000)

        val items = List(itemCount) {
            Entry("Item $it")
        }

        val parent = binding.scrollcontent
        val inflater = LayoutInflater.from(parent.context)

        items.forEach { entry ->
            val itemView = inflater.inflate(R.layout.recycler_row, parent, false)
            parent.addView(itemView)
            val contentView = itemView.findViewById<TextView>(R.id.content)
            contentView.text = entry.contents
            itemView.setOnClickListener {
                ClickTrace.onClickPerformed()
                AlertDialog.Builder(this)
                    .setMessage("Item clicked")
                    .show()
            }
        }
    }
}