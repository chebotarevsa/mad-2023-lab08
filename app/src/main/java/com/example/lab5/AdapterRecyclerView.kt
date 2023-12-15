package com.example.lab5 //ПОМЕНЯТЬ УДАЛЕНИЕ КАТОЧКИ

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.widget.Toast

class AdapterRecyclerView(private val action: ActionInterface) :
    RecyclerView.Adapter<AdapterRecyclerView.MyViewHolder>() {
    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val thumbnailImage: ImageView = itemView.findViewById(R.id.pictureSmall)
        val largeTextView: TextView = itemView.findViewById(R.id.textAbove)
        val smallTextView: TextView = itemView.findViewById(R.id.textBelow)
        val deleteImage: ImageView = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val card = cards[position]
        holder.itemView.tag = card.id
        if (card.image != null) {
            holder.thumbnailImage.setImageBitmap(cards[position].image)
        } else {
            holder.thumbnailImage.setImageResource(R.drawable.empty)
        }
        holder.largeTextView.text = card.answer
        holder.smallTextView.text = card.translation
        holder.itemView.setOnClickListener {
            action.onItemClick(card.id!!)
        }
        holder.deleteImage.setOnClickListener {
            AlertDialog.Builder(holder.deleteImage.context)
                .setIcon(android.R.drawable.ic_menu_delete)
                .setTitle("Удаление").setMessage(
                    "Удалить карточку:" + "\n ${card.answer}"
                ).setPositiveButton("Да") { _, _ -> action.onDeleteCard(card.id!!) }
                .setNegativeButton("Нет") { _, _ ->
                    Toast.makeText(
                        holder.deleteImage.context, "Отмена удаления", Toast.LENGTH_LONG
                    ).show()
                }.show()
        }
    }

    var cards: List<Card> = emptyList()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
}

