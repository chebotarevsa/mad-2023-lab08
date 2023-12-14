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

        with(viewModel) {
            with(binding) {
                card.observe(viewLifecycleOwner) {
                    questionEditText.setText(it.question)
                    hintEditText.setText(it.example)
                    answerEditText.setText(it.answer)
                    translationEditText.setText(it.translation)
                    if (it.image == null) {
                        cardImage.setImageResource(R.drawable.panorama_outline)
                    } else {
                        cardImage.setImageBitmap(it.image)
                        setImageToCard(it.image)
                    }
                }
            }
        }

        binding.fab.setOnClickListener {
            viewModel.editCard(
                viewModel.card.value!!.id!!,
                binding.questionEditText.text.toString(),
                binding.hintEditText.text.toString(),
                binding.answerEditText.text.toString(),
                binding.translationEditText.text.toString()
            )

            val action = EditCardFragmentDirections.actionEditCardFragmentToViewCardFragment(cardId)
            findNavController().navigate(action)
        }

        binding.cardImage.setOnClickListener {
            getSystemContent.launch("image/*")
        }
        viewModel.image.observe(viewLifecycleOwner) {
            binding.cardImage.setImageBitmap(it)
        }
        return binding.root
    }

    private val getSystemContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        viewModel.setImageToCard(it.bitmap(requireContext()))
    }

}