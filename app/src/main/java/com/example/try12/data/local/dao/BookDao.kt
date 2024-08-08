package com.example.try12.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.try12.data.local.entities.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("DELETE FROM book_table WHERE bId = :bId")
    suspend fun deleteBookById(bId : Int)

    @Query("SELECT * FROM book_table")
    fun getAllData() : Flow<List<Book>>

    @Query("SELECT * FROM book_table WHERE book_name LIKE :query || '%'")
    fun getSearchFromStartData(query : String) : Flow<List<Book>>

    @Query("SELECT * FROM book_table WHERE book_name LIKE '%' || :query")
    fun getSearchFromEndData(query : String) : Flow<List<Book>>
    @Query("SELECT * FROM book_table WHERE book_name LIKE '%' || :query || '%' or book_author LIKE '%' || :query || '%' or rating LIKE '%' || :query || '%'")
    fun getSearchedData(query : String) : Flow<List<Book>>

    @Query("SELECT * FROM book_table WHERE book_name NOT LIKE '%' || :query || '%'")
    fun getSearchedExceptQueryData(query : String) : Flow<List<Book>>

}