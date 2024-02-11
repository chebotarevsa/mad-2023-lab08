package com.example.lab8

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.adapters.CustomRecyclerAdapter
import com.example.lab8.databinding.FragmentCardListBinding
import com.example.lab8.viewmodels.CardListViewModel


class CardListFragment : Fragment() {
    private var _binding: FragmentCardListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CustomRecyclerAdapter
    private val viewModel: CardListViewModel by viewModels { CardListViewModel.Factory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardListBinding.inflate(layoutInflater, container, false)
        val recyclerView: RecyclerView = binding.recyclerid
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CustomRecyclerAdapter(action).apply {
            viewModel.cards.observe(viewLifecycleOwner) {
                viewModel.getCardsFromServerIfEmpty()
                cards = it
            }
        }
        recyclerView.adapter = adapter
        binding.addbuttonid.setOnClickListener {
            val action = CardListFragmentDirections.actionCardListFragmentToEditCardFragment("-1")
            findNavController().navigate(action)
        }
        binding.tagButton.setOnClickListener {
            val action = CardListFragmentDirections.actionCardListFragmentToTagListFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private val action = object : ActionInterface {
        override fun onItemClick(itemId: String) {
            val action = CardListFragmentDirections
                .actionCardListFragmentToSeeCardFragment(itemId)
            findNavController().navigate(action)
        }

        override fun onDeleteItem(itemId: String) {
            viewModel.deleteCard(itemId)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
