package com.example.myapplication

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentAddCardBinding
import com.google.android.material.snackbar.Snackbar

class AddCardFragment : Fragment() {
    private var _binding: FragmentAddCardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddCardViewModel by viewModels { AddCardViewModel.Factory() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentAddCardBinding.inflate(layoutInflater, container, false)

        binding.fab.setOnClickListener {
            if (fieldsValid()) {
                val question = binding.questionAddText.text.toString()
                val example = binding.hintAddText.text.toString()
                val answer = binding.answerAddText.text.toString()
                val translation = binding.translationAddText.text.toString()
                viewModel.addCard(question, example, answer, translation)
                if (viewModel.card.value!!.image != null) {
                    binding.cardImage.setImageBitmap(viewModel.card.value!!.image)
                    viewModel.setImageToCard(viewModel.card.value!!.image)
                } else {
                    binding.cardImage.setImageResource(R.drawable.panorama_outline)
                }
                val action = AddCardFragmentDirections.actionAddCardFragmentToMainFragment()
                findNavController().navigate(action)
            } else {
                fieldsIncompleteError()
            }
        }
        viewModel.image.observe(viewLifecycleOwner) {
            binding.cardImage.setImageBitmap(it)
        }
        binding.cardImage.setOnClickListener {
            getSystemContent.launch("image/*")
        }
        return binding.root
    }

    private fun fieldsValid(): Boolean {
        return binding.questionAddText.text.isNotEmpty() && binding.hintAddText.text.isNotEmpty() && binding.answerAddText.text.isNotEmpty() && binding.translationAddText.text.isNotEmpty()
    }

    private fun fieldsIncompleteError() {
        val errorMessage = getString(R.string.error_message)
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
    }

    private val getSystemContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        viewModel.setImageToCard(it.bitmap(requireContext()))
    }
}