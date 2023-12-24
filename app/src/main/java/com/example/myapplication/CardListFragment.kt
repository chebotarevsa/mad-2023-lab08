package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentCardListBinding

class CardListFragment : Fragment() {
    private var _binding: FragmentCardListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecyclerAdapter
    private val viewModel: CardListViewModel by viewModels { CardListViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentCardListBinding.inflate(layoutInflater, container, false)


        val recyclerView: RecyclerView = binding.recyclerid
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = RecyclerAdapter(action).apply {
            viewModel.cards.observe(viewLifecycleOwner) {
                cards = it
            }
        }

        recyclerView.adapter = adapter

        adapter.enableSwipeToDelete(recyclerView)

        binding.addButton.setOnClickListener {
            val action = CardListFragmentDirections.actionCardListFragmentToEditCardFragment("-1")
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val action = object : ActionInterface {
        override fun onItemClick(cardId: String) {
            val action = CardListFragmentDirections.actionCardListFragmentToViewCardFragment(cardId)
            findNavController().navigate(action)
        }

        override fun onDeleteCard(cardId: String) {
            viewModel.deleteCard(cardId)
        }
    }


}