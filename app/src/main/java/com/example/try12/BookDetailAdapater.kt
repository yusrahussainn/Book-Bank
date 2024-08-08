package com.example.try12

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.try12.databinding.SingleItemBinding
import com.example.try12.data.local.entities.Book

class BookDetailsAdapter(private val listener : BookDetailsClickListener) : ListAdapter<Book, BookDetailsAdapter.BookDetailsViewHolder>(DiffUtilCallback()) {

    inner class BookDetailsViewHolder(private val binding: SingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.editBtn.setOnClickListener {
                listener.onEditBookClick(getItem(adapterPosition))
            }
            binding.deleteBtn.setOnClickListener {
                listener.onDeleteBookClick(getItem(adapterPosition))
            }
        }
        fun bind(book: Book){
            binding.bookNameTv.text = book.name
            binding.bookAuthorTv.text = book.author
            binding.bookRatingTv.text = book.rating.toString()
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Book>(){
        override fun areItemsTheSame(oldItem: Book, newItem: Book) = oldItem.bId == newItem.bId

        override fun areContentsTheSame(oldItem: Book, newItem: Book) = oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookDetailsViewHolder {
        return BookDetailsViewHolder(SingleItemBinding.inflate(LayoutInflater.from(parent.context),parent , false))
    }

    override fun onBindViewHolder(holder: BookDetailsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface BookDetailsClickListener{
        fun onEditBookClick(book: Book)
        fun onDeleteBookClick(book: Book)
    }
}