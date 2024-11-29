package id.derysudrajat.dexlayoutops.ui.recylerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import id.derysudrajat.dexlayoutops.R
import id.derysudrajat.dexlayoutops.util.ClickTrace

data class Entry(val contents: String)

class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val content: TextView = itemView.findViewById(R.id.content)
}

class EntryAdapter(private val entries: List<Entry>) : RecyclerView.Adapter<EntryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.recycler_row, parent, false)
        itemView.findViewById<View>(R.id.card).setOnClickListener {
            ClickTrace.onClickPerformed()
            AlertDialog.Builder(parent.context)
                .setMessage("Item clicked")
                .show()
        }
        return EntryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = entries[position]
        holder.content.text = entry.contents
    }

    override fun getItemCount(): Int = entries.size
}
