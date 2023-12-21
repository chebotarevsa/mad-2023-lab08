package com.example.lab8.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab8.R
import com.example.lab8.databinding.FragmentSaveEditBinding
import com.example.lab8.util.CustomEmptyTextWatcher
import com.example.lab8.util.Failed
import com.example.lab8.util.Success
import com.example.lab8.util.bitmap
import com.example.lab8.viewmodels.CardManagerViewModel

class SaveEditFragment : Fragment() {

    private var _binding: FragmentSaveEditBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<SaveEditFragmentArgs>()
    private val cardId by lazy { args.cardId }
    private val viewModel: CardManagerViewModel by viewModels { CardManagerViewModel.Factory(cardId) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveEditBinding.inflate(layoutInflater, container, false)
        observeCardAndImage()
        viewModel.status.observe(viewLifecycleOwner) {
            if (it.isProcessed) {
                return@observe
            }
            if (it is Failed) {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            } else if (it is Success) {
                if (cardId != "-1") {
                    val navAction =
                        SaveEditFragmentDirections.actionEditCardFragmentToSeeCardFragment(
                            cardId
                        )
                    findNavController().navigate(navAction)
                } else {
                    val navAction =
                        SaveEditFragmentDirections.actionEditCardFragmentToListCardFragment()
                    findNavController().navigate(navAction)
                }
            }
            it.isProcessed = true
        }
        validateFields()
        binding.saveButton.setOnClickListener {
            createOrUpdateCard()
        }
        binding.cardImage.setOnClickListener {
            getSystemContent.launch("image/*")
        }
        return binding.root
    }

    private fun observeCardAndImage() {
        viewModel.card.observe(viewLifecycleOwner) {
            binding.questionField.setText(it.question)
            binding.exampleField.setText(it.example)
            binding.answerField.setText(it.answer)
            binding.translationField.setText(it.translation)
            if (viewModel.card.value?.image != null) {
                binding.cardImage.setImageBitmap(it.image)
                viewModel.setImage(it.image)
            } else {
                binding.cardImage.setImageResource(R.drawable.image_icon)
            }
        }
        viewModel.image.observe(viewLifecycleOwner) {
            binding.cardImage.setImageBitmap(it)
        }
    }

    private fun validateFields() {
        binding.questionField.addTextChangedListener(object : CustomEmptyTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.validateQuestion(s.toString())
            }
        })
        binding.exampleField.addTextChangedListener(object : CustomEmptyTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.validateExample(s.toString())
            }
        })
        binding.answerField.addTextChangedListener(object : CustomEmptyTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.validateAnswer(s.toString())
            }
        })
        binding.translationField.addTextChangedListener(object : CustomEmptyTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.validateTranslation(s.toString())
            }
        })
    }

    private fun createOrUpdateCard() {
        viewModel.saveCard(
            binding.questionField.text.toString(),
            binding.exampleField.text.toString(),
            binding.answerField.text.toString(),
            binding.translationField.text.toString(),
        )
    }

    private val getSystemContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        viewModel.setImage(it.bitmap(requireContext()))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}