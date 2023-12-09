package com.example.lab8

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.data.db.CardTable

class CustomRecyclerAdapter(
    private val action: ActionInterface
) : RecyclerView.Adapter<CustomRecyclerAdapter.CardHolder>() {

    class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val thumbnailImage: ImageView = itemView.findViewById(R.id.thumbnail)
        val largeTextView: TextView = itemView.findViewById(R.id.textViewLarge)
        val smallTextView: TextView = itemView.findViewById(R.id.textViewSmall)
        val deleteImage: ImageView = itemView.findViewById(R.id.deleter)
    }

    var cardTables: List<CardTable> = emptyList()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return CardHolder(itemView)
    }

    override fun getItemCount() = cardTables.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val card = cardTables[position]
        holder.itemView.tag = card.id
        if (card.image != null) {
            holder.thumbnailImage.setImageBitmap(card.image)
        } else {
            holder.thumbnailImage.setImageResource(R.drawable.wallpapericon)
        }
        holder.largeTextView.text = card.answer
        holder.smallTextView.text = card.translation
        holder.itemView.setOnClickListener {
            action.onItemClick(card.id)
        }
        holder.deleteImage.setOnClickListener {
            AlertDialog.Builder(holder.deleteImage.context)
                .setIcon(android.R.drawable.ic_menu_delete)
                .setTitle("Вы действительно хотите удалить карточку?").setMessage(
                    "Будет удалена карточка:" + "\n ${card.answer} / ${card.translation}"
                ).setPositiveButton("Да") { _, _ -> action.onDeleteCard(card.id) }
                .setNegativeButton("Нет") { _, _ ->
                    Toast.makeText(
                        holder.deleteImage.context, "Удаление отменено", Toast.LENGTH_LONG
                    ).show()
                }.show()
        }
    }
}

