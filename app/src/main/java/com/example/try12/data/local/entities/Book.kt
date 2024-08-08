package com.example.try12.data.local.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "book_table")
data class Book(
    @PrimaryKey(autoGenerate = true) val bId : Int,
    @ColumnInfo("book_name") val name : String,
    @ColumnInfo("book_author") val author : String,
    @ColumnInfo("rating") val rating : Int
) : Parcelable
