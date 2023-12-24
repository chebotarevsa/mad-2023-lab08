package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
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
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentViewCardBinding.inflate(layoutInflater, container, false)

        viewModel.card.observe(viewLifecycleOwner) {
            binding.cardQuestion.text = getString(R.string.questionField, it.question)
            binding.cardExample.text = getString(R.string.exampleField, it.example)
            binding.cardAnswer.text = getString(R.string.answerField, it.answer)
            binding.cardTranslation.text = getString(R.string.translationField, it.translation)
            binding.cardThumbnail.setImageBitmap(it.image)
            if (it.image != null) {
            binding.cardThumbnail.setImageBitmap(it.image)
            } else {
                binding.cardThumbnail.setImageResource(R.drawable.imageicon)
            }
        }

        binding.editButton.setOnClickListener {
            val action = ViewCardFragmentDirections.actionViewCardFragmentToEditCardFragment(cardId)
            findNavController().navigate(action)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action =
                        ViewCardFragmentDirections.actionViewCardFragmentToCardListFragment()
                    findNavController().navigate(action)
                }
            })
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}