package com.example.lab8

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab8.databinding.FragmentSeeCardBinding

class SeeCardFragment : Fragment() {

    private var _binding: FragmentSeeCardBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<SeeCardFragmentArgs>()
    private val cardId by lazy { args.cardId }
    private val viewModel: SeeCardViewModel by viewModels { SeeCardViewModel.Factory(cardId) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeeCardBinding.inflate(layoutInflater, container, false)
        with(binding) {
            viewModel.cardTable.observe(viewLifecycleOwner) {
                cardQuestion.text = getString(R.string.question_field, it.question)
                cardExample.text = getString(R.string.example_field, it.example)
                cardAnswer.text = getString(R.string.answer_field, it.answer)
                cardTranslation.text = getString(R.string.translationField, it.translation)
                idNum.text = it.id
                if (it.image != null) {
                    cardImage.setImageBitmap(it.image)
                } else {
                    cardImage.setImageResource(R.drawable.wallpapericon)
                }
            }
            editButton.setOnClickListener {
                val navAction =
                    SeeCardFragmentDirections.actionSeeCardFragmentToEditCardFragment(cardId)
                findNavController().navigate(navAction)
            }
            backButton.setOnClickListener {
                val navAction = SeeCardFragmentDirections.actionSeeCardFragmentToListCardFragment()
                findNavController().navigate(navAction)
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        val action =
                            SeeCardFragmentDirections.actionSeeCardFragmentToListCardFragment()
                        findNavController().navigate(action)
                    }
                })
            return root
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}