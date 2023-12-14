package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.FragmentViewCardBinding

class ViewCardFragment : Fragment() {
    private var _binding: FragmentViewCardBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ViewCardFragmentArgs>()
    private val cardId by lazy { args.cardId }
    private val viewModel: ViewCardViewModel by viewModels { ViewCardViewModel.Factory(cardId) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentViewCardBinding.inflate(layoutInflater, container, false)


        with(viewModel) {
            card.observe(viewLifecycleOwner) {
                binding.questionTextCard.text = getString(R.string.questionT, it.question)
                binding.hintTextCard.text = getString(R.string.hintT, it.example)
                binding.answerTextCard.text = getString(R.string.answerT, it.answer)
                binding.translationTextCard.text = getString(R.string.translateT, it.translation)
                if (it.image == null) {
                    binding.cardImage.setImageResource(R.drawable.panorama_outline)
                } else {
                    binding.cardImage.setImageBitmap(it.image)
                }
            }
        }

        binding.edit.setOnClickListener {
            val action = ViewCardFragmentDirections.actionViewCardFragmentToEditCardFragment(cardId)
            findNavController().navigate(action)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action = ViewCardFragmentDirections.actionViewCardFragmentToMainFragment()
                    findNavController().navigate(action)
                }
            })
        return binding.root
    }

}