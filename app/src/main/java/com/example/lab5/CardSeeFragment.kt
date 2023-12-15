package com.example.lab5

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab5.databinding.FragmentCardSeeBinding
import androidx.fragment.app.viewModels
import androidx.activity.OnBackPressedCallback

class CardSeeFragment : Fragment() {
    private var _binding: FragmentCardSeeBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<CardSeeFragmentArgs>()
    private val cardId by lazy { args.cardId }
    private val view_model: CardSeeViewModel by viewModels { CardSeeViewModel.Factory(cardId) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardSeeBinding.inflate(layoutInflater)
        with(binding) {
            view_model.list_cards.observe(viewLifecycleOwner) {
                textQuestion.text = getString(R.string.questionField, it.question)
                textExample.text = getString(R.string.exampleField, it.example)
                textAnswer.text = getString(R.string.answerField, it.answer)
                textTranslation.text = getString(R.string.translationField, it.translation)
                idNumber.text = it.id.toString()
                if (it.image != null) {
                    picture.setImageBitmap(it.image)
                } else {
                    picture.setImageResource(R.drawable.empty)
                }
            }
            buttonEdit.setOnClickListener {
                val navAction =
                    CardSeeFragmentDirections.actionCardSeeFragmentToCardEditFragment(cardId)
                findNavController().navigate(navAction)
            }
            buttonBack.setOnClickListener {
                val navAction = CardSeeFragmentDirections.actionCardSeeFragmentToCardListFragment()
                findNavController().navigate(navAction)
            }
            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        val action =
                            CardSeeFragmentDirections.actionCardSeeFragmentToCardListFragment()
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





