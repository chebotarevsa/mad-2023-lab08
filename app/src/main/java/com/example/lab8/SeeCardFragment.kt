package com.example.lab8

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab8.databinding.FragmentSeeCardBinding
import com.example.lab8.viewmodels.SeeCardViewModel


class SeeCardFragment : Fragment() {
    private var _binding: FragmentSeeCardBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<SeeCardFragmentArgs>()
    private val cardId by lazy { args.cardId }
    private val viewModel: SeeCardViewModel by viewModels { SeeCardViewModel.Factory(cardId) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeeCardBinding.inflate(layoutInflater, container, false)
        viewModel.card.observe(viewLifecycleOwner){
            binding.cardQuestion.text = getString(R.string.questionField, viewModel.card.value!!.question)
            binding.cardExample.text = getString(R.string.exampleField, viewModel.card.value!!.example)
            binding.cardAnswer.text = getString(R.string.answerField, viewModel.card.value!!.answer)
            binding.cardTranslation.text = getString(R.string.translationField, viewModel.card.value!!.translation)
            if (it.image != null) {
                binding.cardImage.setImageBitmap(it.image)
            } else {
                binding.cardImage.setImageResource(R.drawable.icon)
            }
        }
        binding.editButton.setOnClickListener {
            val action = SeeCardFragmentDirections.actionSeeCardFragmentToEditCardFragment(cardId)
            findNavController().navigate(action)
        }

        binding.backButton.setOnClickListener {
            val action = SeeCardFragmentDirections.actionSeeCardFragmentToCardListFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}