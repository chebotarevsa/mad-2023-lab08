package com.example.lab8.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.R
import com.example.lab8.data.db.Tag

class TagsAdapter() : RecyclerView.Adapter<TagsAdapter.TagViewHolder>() {
    // ... other methods ...
    var tags: List<Tag> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = tags.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.tag_item, parent, false)
        return TagViewHolder(itemView)
    }


    class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagNameTextView: TextView = itemView.findViewById(R.id.tagName)
        val tagColorCodeTextView: TextView = itemView.findViewById(R.id.tagColorCode)
        val tagColorView: View = itemView.findViewById(R.id.tagColor)

        fun bind(tag: Tag) {
            tagNameTextView.text = tag.tagName
            tagColorView.setBackgroundColor(Color.parseColor(tag.colorCode))
        }
    }

    override fun onBindViewHolder(holder: TagsAdapter.TagViewHolder, position: Int) {
        val tag = tags[position]

        holder.itemView.tag = tag.id
        holder.tagNameTextView.text = tag.tagName
        holder.tagColorCodeTextView.text = tag.colorCode
        holder.bind(tag)
//        holder.itemView.setOnClickListener {
//            action.onItemClick(card.id)
//
//        }
//        holder.deleteImage.setOnClickListener {
//            AlertDialog.Builder(holder.deleteImage.context)
//                .setIcon(android.R.drawable.ic_menu_delete)
//                .setTitle("Вы действительно хотите удалить карточку?").setMessage(
//                    "Будет удалена карточка:" + "\n ${card.answer} / ${card.translation}"
//                ).setPositiveButton("Да") { _, _ ->
//                    action.onDeleteCard(card.id)
//                }
//                .setNegativeButton("Нет") { _, _ ->
//                    Toast.makeText(
//                        holder.deleteImage.context, "Удаление отменено", Toast.LENGTH_LONG
//                    ).show()
//                }.show()
//        }


    }
}
