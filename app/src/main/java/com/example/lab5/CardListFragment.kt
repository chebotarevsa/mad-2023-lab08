package com.example.lab5

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab5.databinding.FragmentCardListBinding
import androidx.fragment.app.viewModels

class CardListFragment : Fragment() {
    private var _binding: FragmentCardListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AdapterRecyclerView
    private val view_model: CardListViewModel by viewModels { CardListViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardListBinding.inflate(layoutInflater)
        val recyclerView: RecyclerView = binding.recyclerId
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AdapterRecyclerView(action).apply {
            view_model.list_cards.observe(viewLifecycleOwner){
                view_model.getCardsFromRemoteIfEmpty()
                cards = it
            }
        }
        recyclerView.adapter = adapter

        binding.addButton.setOnClickListener {
            val navAction =
                CardListFragmentDirections.actionCardListFragmentToCardEditFragment("empty") //Закончить работу с CardEditFragment
            findNavController().navigate(navAction)
        }
        return binding.root

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val action = object : ActionInterface {
            override fun onItemClick(cardId: String) {
            val action = CardListFragmentDirections.actionCardListFragmentToCardSeeFragment(cardId)
            findNavController().navigate(action)
        }

            override fun onDeleteCard(cardId: String) {
            view_model.deleteCard(cardId)
        }
    }
}
