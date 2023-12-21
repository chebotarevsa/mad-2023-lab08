package com.example.lab8.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.databinding.FragmentArrayListBinding
import com.example.lab8.service.CustomRecyclerAdapter
import com.example.lab8.util.ActionInterface
import com.example.lab8.viewmodels.ArrayListViewModel

class ArrayListFragment : Fragment() {

    private var _binding: FragmentArrayListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CustomRecyclerAdapter
    private val viewModel: ArrayListViewModel by viewModels{ArrayListViewModel.Factory}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArrayListBinding.inflate(layoutInflater, container, false)
        val recyclerView: RecyclerView = binding.recyclerid
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CustomRecyclerAdapter(action).apply {
            viewModel.cards.observe(viewLifecycleOwner) {
                cards = it
            }
        }
        recyclerView.adapter = adapter
        binding.addbuttonid.setOnClickListener {
            val action =
                ArrayListFragmentDirections.actionListCardFragmentToEditCardFragment(
                    "-1"
                )
            findNavController().navigate(action)
        }
        return binding.root
    }

    private val action = object : ActionInterface {
        override fun onItemClick(cardId: String) {
            val action =
                ArrayListFragmentDirections.actionListCardFragmentToSeeCardFragment(
                    cardId
                )
            findNavController().navigate(action)
        }

        override fun onDeleteCard(cardId: String) {
            viewModel.deleteCard(cardId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}