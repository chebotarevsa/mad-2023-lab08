package com.example.lab8.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab8.R
import com.example.lab8.viewmodels.SeeViewModel
import com.example.lab8.databinding.FragmentSeeBinding

class SeeFragment : Fragment() {

    private var _binding: FragmentSeeBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<SeeFragmentArgs>()
    private val cardId by lazy { args.cardId }
    private val viewModel: SeeViewModel by viewModels{SeeViewModel.Factory(cardId)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentSeeBinding.inflate(layoutInflater, container, false)

        observeCardAndImage()

        binding.editButton.setOnClickListener {
            val action =
                SeeFragmentDirections.actionSeeCardFragmentToEditCardFragment(
                    cardId
                )
            findNavController().navigate(action)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action =
                        SeeFragmentDirections.actionSeeCardFragmentToListCardFragment()
                    findNavController().navigate(action)
                }
            })
        return binding.root
    }

    private fun observeCardAndImage() {
        viewModel.card.observe(viewLifecycleOwner) {
            binding.cardQuestion.text = getString(R.string.question_field, it.question)
            binding.cardExample.text = getString(R.string.example_field, it.example)
            binding.cardAnswer.text = getString(R.string.answer_field, it.answer)
            binding.cardTranslation.text = getString(R.string.translation_field, it.translation)
            if (it.image != null) {
                binding.cardThumbnail.setImageBitmap(it.image)
            } else {
                binding.cardThumbnail.setImageResource(R.drawable.image_icon)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}