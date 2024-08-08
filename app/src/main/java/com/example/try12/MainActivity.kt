package com.example.try12

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.try12.databinding.ActivityMainBinding
import com.example.try12.data.local.dao.BookDao
import com.example.try12.data.local.database.AppDatabase
import com.example.try12.data.local.entities.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), AddEditBookFragment.AddEditBookListener,
    BookDetailsAdapter.BookDetailsClickListener {

    private lateinit var binding: ActivityMainBinding
    private var dao: BookDao? = null
    private lateinit var adapter: BookDetailsAdapter

    private lateinit var searchQueryLiveData: MutableLiveData<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVars()
        attachUiListener()
        subscribeDataStreams()
    }

    private fun subscribeDataStreams() {
        searchQueryLiveData.observe(this) { query ->
            lifecycleScope.launch {
                adapter.submitList(dao?.getSearchedData(query)?.first())
            }
        }
        lifecycleScope.launch {
            dao?.getAllData()?.collect { mList ->

                //   adapter.submitList(mList)
//                binding.searchcView.setQuery("" , false)
//                binding.searchcView.clearFocus()

                lifecycleScope.launch {
                    adapter.submitList(
                        dao?.getSearchedData(searchQueryLiveData.value ?: "")?.first()
                    )
                }
            }
        }
    }

    private fun initVars() {
        dao = AppDatabase.getDatabase(this).bookDao()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BookDetailsAdapter(this)
        binding.recyclerView.adapter = adapter
        searchQueryLiveData = MutableLiveData("")
    }

    private fun attachUiListener() {
        binding.floatingActionButton.setOnClickListener {
            showBottomSheet()
        }
        binding.searchcView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null)
                    onQueryChanged(newText)
                return true
            }

        })
    }

    private fun onQueryChanged(query: String) {
        searchQueryLiveData.postValue(query)
//        lifecycleScope.launch {
//                adapter.submitList(dao?.getSearchedData(query)?.first())
//        }
    }


    private fun showBottomSheet(book: Book? = null) {
        val bottomSheet = AddEditBookFragment.newInstance(book)
        bottomSheet.show(supportFragmentManager, AddEditBookFragment.TAG)
    }

    override fun onSaveBtnClicked(isUpdate: Boolean, book: Book) {

        lifecycleScope.launch(Dispatchers.IO) {

            if (isUpdate)
                dao?.updateBook(book)
            else
                dao?.saveBook(book)
        }

    }

    override fun onEditBookClick(book: Book) {
        showBottomSheet(book)
    }

    override fun onDeleteBookClick(book: Book) {
        lifecycleScope.launch(Dispatchers.IO) {
            // dao?.deleteBook(book)
            dao?.deleteBookById(book.bId)
        }
    }

}