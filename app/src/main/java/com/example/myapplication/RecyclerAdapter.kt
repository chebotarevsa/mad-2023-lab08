package com.example.myapplication

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.domain.entity.Card

class RecyclerAdapter(private val action: ActionInterface) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnailImage: ImageView = itemView.findViewById(R.id.thumbnail)
        val largeTextView: TextView = itemView.findViewById(R.id.textViewLarge)
        val smallTextView: TextView = itemView.findViewById(R.id.textViewSmall)
        val deleteImage: ImageView = itemView.findViewById(R.id.deleter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val card = cards[position]
        holder.itemView.tag = card.id
        if (card.image != null) {
            holder.thumbnailImage.setImageBitmap(cards[position].image)
        } else {
            holder.thumbnailImage.setImageResource(R.drawable.imageicon)
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
                ).setPositiveButton("Да") { _, _ ->
                    action.onDeleteCard(card.id)

                }
                .setNegativeButton("Нет") { _, _ ->
                    Toast.makeText(
                        holder.deleteImage.context, "Удаление отменено", Toast.LENGTH_LONG
                    ).show()
                }.show()
        }

    }

    var cards: List<Card> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
        get() = field

    fun setCards(){
        this.cards=cards
        notifyDataSetChanged()
    }

    fun enableSwipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val card = cards[position]

                action.onDeleteCard(card.id)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun refreshCardsViewWith(cards: List<Card>) {
        this.cards = cards
        notifyDataSetChanged()
    }


}

