package com.example.lab8

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab8.data.db.Tag
import com.example.lab8.databinding.FragmentEditCardBinding
import com.example.lab8.viewmodels.EditCardViewModel


class EditCardFragment : Fragment() {

    private var _binding: FragmentEditCardBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<EditCardFragmentArgs>()
    private val cardId by lazy { args.cardId }
    private val viewModel: EditCardViewModel by viewModels { EditCardViewModel.Factory(cardId) }
    private lateinit var spinner: Spinner

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

        spinner = binding.spinner

        val adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        viewModel.tagNames.observe(viewLifecycleOwner){
            adapter.addAll(it)
        }
        spinner.adapter = adapter

        var selectedTagName = ""
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                 selectedTagName = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Вызывается, когда ничего не выбрано
            }
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

            viewModel.saveCard(question, example, answer, translation, selectedTagName)
            if (!viewModel.checkIfNewCard()) {
                val action =
                    EditCardFragmentDirections.actionEditCardFragmentToSeeCardFragment(cardId)
                findNavController().navigate(action)
            } else {
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