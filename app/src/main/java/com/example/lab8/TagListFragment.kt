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
import com.example.lab8.adapters.TagsAdapter
import com.example.lab8.databinding.FragmentTagListBinding
import com.example.lab8.viewmodels.TagListViewModel


class TagListFragment : Fragment() {
    private var _binding: FragmentTagListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TagsAdapter
    private val viewModel: TagListViewModel by viewModels { TagListViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTagListBinding.inflate(layoutInflater, container, false)
        val recyclerView: RecyclerView = binding.tagRecycler
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = TagsAdapter(action).apply {
            viewModel.tags.observe(viewLifecycleOwner) {
                tags = it
            }
        }
        recyclerView.adapter = adapter

        binding.addbuttonid.setOnClickListener {
            val action = TagListFragmentDirections.actionTagListFragmentToEditTagFragment("-1")
            findNavController().navigate(action)
        }
        binding.backButton.setOnClickListener {
            val action = TagListFragmentDirections.actionTagListFragmentToCardListFragment()
            findNavController().navigate(action)
        }
        return binding.root

    }

    private val action = object : ActionInterface {
        override fun onItemClick(itemId: String) {
            val action = TagListFragmentDirections
                .actionTagListFragmentToSeeTagFragment(itemId)
            findNavController().navigate(action)
        }

        override fun onDeleteItem(itemId: String) {
            viewModel.deleteTag(itemId)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}