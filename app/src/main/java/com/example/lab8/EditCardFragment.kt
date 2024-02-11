package com.example.lab8

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab8.databinding.FragmentEditCardBinding
import com.example.lab8.viewmodels.EditCardViewModel


class EditCardFragment : Fragment() {

    private var _binding: FragmentEditCardBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<EditCardFragmentArgs>()
    private val cardId by lazy { args.cardId }
    private val viewModel: EditCardViewModel by viewModels { EditCardViewModel.Factory(cardId) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCardBinding.inflate(layoutInflater, container, false)
        viewModel.card.observe(viewLifecycleOwner) {
            binding.number.setText(viewModel.card.value!!.id)
            binding.questionField.setText(viewModel.card.value!!.question)
            binding.exampleField.setText(viewModel.card.value!!.example)
            binding.answerField.setText(viewModel.card.value!!.answer)
            binding.translationField.setText(viewModel.card.value!!.translation)
            if (it.image != null) {
                binding.cardImage.setImageBitmap(it.image)
                viewModel.setImage(it.image)
            } else {
                binding.cardImage.setImageResource(R.drawable.icon)
            }
        }
        viewModel.image.observe(viewLifecycleOwner) {
            binding.cardImage.setImageBitmap(it)
        }

        binding.cardImage.setOnClickListener {
            getSystemContent.launch("image/*")
        }

        binding.saveButton.setOnClickListener {
            val question =
                binding.questionField.text.toString()

            val example =
                binding.exampleField.text.toString()

            val answer =
                binding.answerField.text.toString()


            val translation =

                binding.translationField.text.toString()
            viewModel.saveCard(question, example, answer, translation)
            if (!viewModel.checkIfNewCard()){
            val action = EditCardFragmentDirections.actionEditCardFragmentToSeeCardFragment(cardId)
            findNavController().navigate(action)}
            else{
                val action = EditCardFragmentDirections.actionEditCardFragmentToCardListFragment()
                findNavController().navigate(action)
            }
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