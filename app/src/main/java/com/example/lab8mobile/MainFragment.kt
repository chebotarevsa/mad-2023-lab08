package com.example.lab8mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab8mobile.databinding.FragmentMainBinding
import com.example.lab8mobile.Data.CardsAdapter
import com.example.lab8mobile.Domain.Entity.TermCard

class MainFragment : Fragment() {
    private lateinit var adapter: CardsAdapter
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        adapter = CardsAdapter(adapterCallBack)
        val layoutManager = LinearLayoutManager(context)
        binding.RecyclerView.layoutManager = layoutManager
        binding.RecyclerView.adapter = adapter

        viewModel.cardsList.observe(viewLifecycleOwner) { cards ->
            adapter.setItem(cards)
        }

        binding.addCardFromNet.setOnClickListener{
            viewModel.getCardsFromRemoteIfEmpty()
        }

        val button = binding.button
        button.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToAddCardFragment(NEW_CARD)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private val adapterCallBack = object : CallbackFun {
        override fun deleteCard(card: TermCard) {
            viewModel.deleteCard(card)
        }

        override fun showCard(cardId: String) {
            val action = MainFragmentDirections.actionMainFragmentToViewCardFragment(cardId)
            findNavController().navigate(action)
        }
    }

}
