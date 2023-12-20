package com.example.lab8mobile

import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab8mobile.databinding.FragmentAddCardBinding


class AddCardFragment : Fragment() {
    private lateinit var _binding: FragmentAddCardBinding
    private val binding get() = _binding
    private val viewModel: AddCardViewModel by viewModels { AddCardViewModel.Factory(index) }

    private val args by navArgs<AddCardFragmentArgs>()
    private val index by lazy { args.id }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)
        viewModel.card.observe(viewLifecycleOwner) {
            val card = viewModel.card.value!!
            binding.editTextText.setText(card.question)
            binding.editTextText2.setText(card.example)
            binding.editTextText3.setText(card.answer)
            binding.editTextText4.setText(card.translate)
            if (card.image != null) {
                viewModel.setImageBitmap(card.image)
            } else {
                setupDefaultImage()
            }
        }
        viewModel.imageBitmap.observe(viewLifecycleOwner) {
            binding.imageView.setImageBitmap(it)
        }

        binding.imageView.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.button.setOnClickListener {

            addTermCard()
        }

        viewModel.imageBitmap.observe(viewLifecycleOwner) { bitmap ->
            binding.imageView.setImageBitmap(bitmap)
        }

        return binding.root
    }

    private val getImage = registerForActivityResult(ImageContract()) { uri ->
        uri?.let {
            val image = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.createSource(this.requireContext().contentResolver, uri)
            } else {
                TODO("VERSION.SDK_INT < P")
            }
            viewModel.setImageBitmap(ImageDecoder.decodeBitmap(image))
        }
    }

    private fun addTermCard() {
        val question = binding.editTextText.text.toString()
        val hint = binding.editTextText2.text.toString()
        val answer = binding.editTextText3.text.toString()
        val translate = binding.editTextText4.text.toString()

        if (question.isEmpty() || hint.isEmpty() || answer.isEmpty() || translate.isEmpty()) {
            Toast.makeText(this.context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.addOrUpdateCard(question, hint, answer, translate)
        if(index != NEW_CARD) {
            val action =
                AddCardFragmentDirections.actionAddCardFragmentToViewCardFragment(viewModel.card.value!!.id)
            findNavController().navigate(action)
        } else {
            val action =
                AddCardFragmentDirections.actionAddCardFragmentToMainFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupDefaultImage() {
        val imageWidth = getResources().getDimension(R.dimen.WidthimageViewAddCard).toInt()
        val imageHeight = getResources().getDimension(R.dimen.HeightimageViewAddCard).toInt()
        val yourBitmap =
            getDrawable(requireContext(), R.drawable.download_img)!!.toBitmap(imageWidth, imageHeight)
        binding.imageView.setImageBitmap(yourBitmap)
    }
}
