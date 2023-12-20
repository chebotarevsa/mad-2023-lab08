package com.example.lab8mobile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab8mobile.Domain.Entity.TermCard
import com.example.lab8mobile.databinding.FragmentViewCardBinding

class ViewCardFragment : Fragment() {

    private lateinit var binding: FragmentViewCardBinding
    private val viewModel: ViewCardViewModel by viewModels { ViewCardViewModel.Factory(cardId) }

    private val args by navArgs<ViewCardFragmentArgs>()
    private val cardId by lazy { args.id }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewCardBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action =
                        ViewCardFragmentDirections.actionViewCardFragmentToMainFragment()
                    findNavController().navigate(action)
                }
            })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.card.observe(viewLifecycleOwner) { card ->
            card?.let {
                updateUI(it)
            }
        }

        binding.button.setOnClickListener {
            val action =
                ViewCardFragmentDirections.actionViewCardFragmentToAddCardFragment(cardId)

            findNavController().navigate(action)
        }

    }

    private fun updateUI(card: TermCard) {
        binding.questionField.text = card.question
        binding.exampleField.text = card.example
        binding.answerView.text = card.answer
        binding.translateView.text = card.translate

        if (card.image != null) {
            binding.imageView3.setImageBitmap(card.image)
        } else {
            binding.imageView3.setImageResource(R.drawable.ic_image)
        }
    }
}

