package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentMainBinding

class MainFragment : Fragment(), ActionInterface {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CardAdapter
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel.setCardList()

        val recyclerView: RecyclerView = binding.recid
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        adapter = CardAdapter(requireContext(), this).apply {
            viewModel.cards.observe(viewLifecycleOwner) {
                setCards(it)
            }
        }
        recyclerView.adapter = adapter

        binding.add.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToAddCardFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(cardId: Int) {
        val action = MainFragmentDirections.actionMainFragmentToViewCardFragment(cardId)
        findNavController().navigate(action)
    }

    override fun onDeleteCard(cardId: Int) {
        viewModel.setCardToDelete(cardId)
        viewModel.deleteCardById(cardId)
    }
}