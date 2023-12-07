package com.example.lab8

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.databinding.FragmentListCardBinding

class ListCardFragment : Fragment() {
    private var _binding: FragmentListCardBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CustomRecyclerAdapter
    private val viewModel: ListCardViewModel by viewModels { ListCardViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCardBinding.inflate(layoutInflater, container, false)

        val recyclerView: RecyclerView = binding.recyclerid
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CustomRecyclerAdapter(action).apply {
            viewModel.cards.observe(viewLifecycleOwner) {
                viewModel.getCardsFromRemoteIfEmpty()
                cards = it
            }
        }
        recyclerView.adapter = adapter


        binding.addbuttonid.setOnClickListener {
            val navAction = ListCardFragmentDirections
                .actionListCardFragmentToEditCardFragment("empty")
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
            val action = ListCardFragmentDirections
                .actionListCardFragmentToSeeCardFragment(cardId)
            findNavController().navigate(action)
        }

        override fun onDeleteCard(cardId: String) {
            viewModel.deleteCard(cardId)
        }
    }
}