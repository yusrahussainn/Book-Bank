package com.example.try12

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.try12.data.local.entities.Book
import com.example.try12.databinding.FragmentAddEditBookBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddEditBookFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddEditBookBinding
    private var listener: AddEditBookListener? = null
    private var book: Book? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as AddEditBookListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddEditBookBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            book = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                arguments?.getParcelable("book", Book::class.java)
            else
                arguments?.getParcelable("book")
        }
        book?.let { setExistingDataOnUi(it) }
        attachUiListener()
    }

    private fun setExistingDataOnUi(book: Book) {
        binding.bookNameEt.setText(book.name)
        binding.bookAuthorEt.setText(book.author)
        binding.bookRatingEt.setText(book.rating.toString())
        binding.saveBtn.text = "Update"
    }

    private fun attachUiListener() {
        binding.saveBtn.setOnClickListener {
            val name = binding.bookNameEt.text.toString()
            val author = binding.bookAuthorEt.text.toString()
            val rating = binding.bookRatingEt.text.toString()
            if (name.isNotEmpty() && author.isNotEmpty() && rating.isNotEmpty()) {
                val book1 = Book(book?.bId ?: 0, name, author, rating.toInt())
                listener?.onSaveBtnClicked(book != null, book1)
            }
            dismiss()
        }
    }

    companion object {
        const val TAG = "AddEditBookFragment"

        @JvmStatic
        fun newInstance(book: Book?) = AddEditBookFragment().apply {
            arguments = Bundle().apply {
                putParcelable("book", book)
            }
        }
    }

    interface AddEditBookListener {
        fun onSaveBtnClicked(isUpdate: Boolean, book: Book)
    }
}