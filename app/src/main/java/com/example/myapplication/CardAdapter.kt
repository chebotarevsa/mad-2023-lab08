package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.provider.Settings.System.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.RecyclerItemBinding

class CardAdapter(
    private val context: Context, private val action: ActionInterface
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    class CardViewHolder(binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val thumbnail: ImageView = binding.thumbnail
        val answerText: TextView = binding.answer
        val translateText: TextView = binding.translate
        val delete: ImageView = binding.deleteIcon
    }

    var cardList: List<Card> = emptyList()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding =
            RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardList[position]
        holder.itemView.tag = card.id
        if (card.image != null) {
            holder.thumbnail.setImageBitmap(cardList[position].image)
        } else {
            holder.thumbnail.setImageResource(R.drawable.panorama_outline)
        }
        holder.answerText.text = card.answer
        holder.translateText.text = card.translation

        holder.itemView.setOnClickListener {
            action.onItemClick(card.id!!)
        }

        holder.delete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(holder.delete.context)
                .setTitle(context.getString(R.string.delete_card_title))
                .setMessage(context.getString(R.string.delete_card_message))
                .setPositiveButton(context.getString(R.string.yes), null)
                .setNegativeButton(context.getString(R.string.cancel), null).show()
            val positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            val negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            positiveButton.setTextColor(ContextCompat.getColor(holder.delete.context, R.color.red))
            negativeButton.setTextColor(
                ContextCompat.getColor(
                    holder.delete.context, R.color.green
                )
            )
            positiveButton.setOnClickListener {
                action.onDeleteCard(card.id!!)
                alertDialog.dismiss()
            }
            negativeButton.setOnClickListener {
                alertDialog.dismiss()
            }
        }
    }

    fun setCards(cardList: List<Card>) {
        this.cardList = cardList
        notifyDataSetChanged()
    }
}

interface ActionInterface {
    fun onItemClick(cardId: Int)
    fun onDeleteCard(cardId: Int)
}