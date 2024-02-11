package com.example.lab8

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab8.databinding.FragmentSeeTagBinding
import com.example.lab8.viewmodels.SeeTagViewModel


class SeeTagFragment : Fragment() {
    private var _binding: FragmentSeeTagBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<SeeTagFragmentArgs>()
    private val tagId by lazy { args.tagId }
    private val viewModel: SeeTagViewModel by viewModels { SeeTagViewModel.Factory(tagId) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeeTagBinding.inflate(layoutInflater, container, false)
        viewModel.tag.observe(viewLifecycleOwner){
            binding.tagName.text = getString(R.string.questionField, it.tagName)
            binding.colorCode.text = getString(R.string.exampleField, it.colorCode)
            binding.tagColor.setBackgroundColor(Color.parseColor(it.colorCode))
        }
        binding.editButton.setOnClickListener {
            val action = SeeTagFragmentDirections.actionSeeTagFragmentToEditTagFragment(tagId)
            findNavController().navigate(action)
        }

        binding.backButton.setOnClickListener {
            val action = SeeTagFragmentDirections.actionSeeTagFragmentToTagListFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}