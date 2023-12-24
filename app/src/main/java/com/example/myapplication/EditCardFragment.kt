package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.FragmentEditCardBinding

class EditCardFragment : Fragment() {
    private var _binding: FragmentEditCardBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<EditCardFragmentArgs>()
    private val cardId by lazy { args.cardId }
    private val viewModel: EditCardViewModel by viewModels { EditCardViewModel.Factory(cardId) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentEditCardBinding.inflate(layoutInflater, container, false)

        viewModel.card.observe(viewLifecycleOwner) {
        binding.questionField.setText(it.question)
        binding.exampleField.setText(it.example)
        binding.answerField.setText(it.answer)
        binding.translationField.setText(it.translation)
        binding.cardImage.setImageBitmap(it.image)
            if (it.image != null) {
                binding.cardImage.setImageBitmap(it.image)
                viewModel.setImage(it.image)
            } else {
                binding.cardImage.setImageResource(R.drawable.imageicon)
            }
        }

        binding.cardImage.setOnClickListener {
            getSystemContent.launch("image/*")
        }

        viewModel.image.observe(viewLifecycleOwner) {
            binding.cardImage.setImageBitmap(it)
        }

        binding.saveButton.setOnClickListener {
            with(binding) {
                viewModel.saveCard(
                    questionField.text.toString(),
                    exampleField.text.toString(),
                    answerField.text.toString(),
                    translationField.text.toString(),
                )
            }
            val action = EditCardFragmentDirections.actionEditCardFragmentToViewCardFragment(viewModel.card.value!!.id)
            findNavController().navigate(action)
        }
        return binding.root
    }

    private val getSystemContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        viewModel.setImage(it.bitmap(requireContext()))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}