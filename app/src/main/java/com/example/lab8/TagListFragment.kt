package com.example.lab8

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        adapter = TagsAdapter().apply {
            viewModel.tags.observe(viewLifecycleOwner) {
                tags = it
            }
        }
        recyclerView.adapter = adapter
        return binding.root

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}