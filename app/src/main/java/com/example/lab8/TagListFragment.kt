package com.example.lab8

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.adapters.TagsAdapter
import com.example.lab8.data.db.Tag
import com.example.lab8.databinding.FragmentTagListBinding
import com.example.lab8.viewmodels.TagListViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException


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
        binding.searchButton.setOnClickListener {
            viewModel.tags.observe(viewLifecycleOwner) {
                viewModel.findTagsLike(binding.tagNameField.text.toString())
                adapter.tags = it
            }
        }
        binding.deleteAllTagsButton.setOnClickListener {
            viewModel.tags.value?.let { it1 -> viewModel.deleteTags(it1) }
        }
        binding.backButton.setOnClickListener {
            val action = TagListFragmentDirections.actionTagListFragmentToCardListFragment()
            findNavController().navigate(action)
        }
        val gson = Gson()
        binding.saveToFileButton.setOnClickListener {
            val jsonTags = gson.toJson(viewModel.tags.value)
            val directory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val file = File(directory, "tags.json")

            try {
                FileOutputStream(file).use { outputStream ->
                    outputStream.write(jsonTags.toByteArray())
                }
                Toast.makeText(requireContext(), "Tags are exported", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        binding.readFromFileButton.setOnClickListener {
            try {
                val directory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                val file = File(directory, "tags.json")

                val br = BufferedReader(FileReader(file))
                val listType = object : TypeToken<List<Tag>>() {}.type
                val tagsList: List<Tag> = gson.fromJson(br, listType)
                viewModel.saveTags(tagsList)
                Toast.makeText(requireContext(), "Tags are imported", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
            }
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