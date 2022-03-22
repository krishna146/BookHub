package com.krishna.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.krishna.bookhub.R
import com.krishna.bookhub.adapter.FavouritesRecyclerAdapter
import com.krishna.bookhub.database.BookDatabase
import com.krishna.bookhub.database.BookEntity
import com.krishna.bookhub.model.Book

class FavouritesFragment : Fragment() {

    lateinit var recyclerFavourites: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var favouritesRecyclerAdapter: FavouritesRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    var dbBookList = listOf<BookEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favoutites, container, false)

        recyclerFavourites = view.findViewById(R.id.recyclerFavourites)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        layoutManager = GridLayoutManager(activity as Context, 2)
        dbBookList = RetrieveFavourites(activity as Context).execute().get()
        if (activity != null) {
            progressLayout.visibility = View.GONE
            favouritesRecyclerAdapter = FavouritesRecyclerAdapter(activity as Context, dbBookList)
            recyclerFavourites.adapter = favouritesRecyclerAdapter
            recyclerFavourites.layoutManager = layoutManager


        }
        return view
    }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void, Void, List<BookEntity>>() {
        override fun doInBackground(vararg p0: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()
            return db.bookDao().getAllBooks()
        }


    }

}
