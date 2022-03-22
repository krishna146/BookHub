package com.krishna.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.krishna.bookhub.R
import com.krishna.bookhub.activity.DescriptionActivity
import com.krishna.bookhub.database.BookEntity
import com.krishna.bookhub.model.Book
import com.squareup.picasso.Picasso

class FavouritesRecyclerAdapter(val context: Context, val bookList:List<BookEntity>) :
    RecyclerView.Adapter<FavouritesRecyclerAdapter.FavouritesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourites_single_row, parent, false)
        return FavouritesViewHolder(view)

    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val book = bookList[position]
        holder.txtFavBookTitle.text = book.bookName
        holder.txtFavBookAuthor.text = book.bookAuthor
        holder.txtFavBookPrice.text = book.bookPrice
        holder.txtFavBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgFavBookImage)
        holder.llFavContent.setOnClickListener{
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id",book.book_id.toString())
            context.startActivity(intent)

        }
    }

    class FavouritesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llFavContent: LinearLayout = view.findViewById(R.id.llFavContent)
        val imgFavBookImage: ImageView = view.findViewById(R.id.imgFavBookImage)
        val txtFavBookTitle: TextView = view.findViewById(R.id.txtFavBookTitle)
        val txtFavBookAuthor: TextView = view.findViewById(R.id.txtFavBookAuthor)
        val txtFavBookPrice: TextView = view.findViewById(R.id.txtFavBookPrice)
        val txtFavBookRating: TextView = view.findViewById(R.id.txtFavBookRating)


    }


}