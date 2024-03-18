package com.example.lab8

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab8.databinding.FragmentEditTagBinding
import com.example.lab8.viewmodels.EditTagViewModel


class EditTagFragment : Fragment() {

    private var _binding: FragmentEditTagBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<EditTagFragmentArgs>()
    private val tagId by lazy { args.tagId }
    private val viewModel: EditTagViewModel by viewModels { EditTagViewModel.Factory(tagId) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTagBinding.inflate(layoutInflater, container, false)
        viewModel.tag.observe(viewLifecycleOwner) {
            binding.tagNameField.setText(viewModel.tag.value!!.tagName)
            binding.colorCodeField.setText(viewModel.tag.value!!.colorCode)
            binding.number.setText(viewModel.tag.value!!.id)
        }

        binding.saveButton.setOnClickListener {
            val tagName =
                binding.tagNameField.text.toString()

            val colorCode =
                binding.colorCodeField.text.toString()

            viewModel.saveTag(tagName, colorCode)
//            if (!viewModel.checkIfNewTag()){
//                val action = EditTagFragmentDirections.actionEditTagFragmentToTagListFragment(tagId)
//                findNavController().navigate(action)}
//            else{
            val action = EditTagFragmentDirections.actionEditTagFragmentToTagListFragment()
            findNavController().navigate(action)
//            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}