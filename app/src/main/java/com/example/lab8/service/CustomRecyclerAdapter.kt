package com.example.lab8.service

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.R
import com.example.lab8.db.entity.Card
import com.example.lab8.util.ActionInterface

class CustomRecyclerAdapter(
    private val action: ActionInterface,
) :
    RecyclerView.Adapter<CustomRecyclerAdapter.CardHolder>() {
    class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnailImage: ImageView = itemView.findViewById(R.id.thumbnail)
        val largeTextView: TextView = itemView.findViewById(R.id.textViewLarge)
        val smallTextView: TextView = itemView.findViewById(R.id.textViewSmall)
        val deleteImage: ImageView = itemView.findViewById(R.id.deleter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item, parent, false)
        return CardHolder(itemView)
    }

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val card = cards[position]
        if (card.image != null) {
            holder.thumbnailImage.setImageBitmap(cards[position].image)
        } else {
            holder.thumbnailImage.setImageResource(R.drawable.image_icon)
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
                ).setPositiveButton("Да") { _, _ -> action.onDeleteCard(card.id!!) }
                .setNegativeButton("Нет") { _, _ ->
                    Toast.makeText(
                        holder.deleteImage.context, "Удаление отменено", Toast.LENGTH_LONG
                    ).show()
                }.show()
        }
    }

    var cards: List<Card> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
}