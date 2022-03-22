package com.krishna.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.krishna.bookhub.R
import com.krishna.bookhub.adapter.DashboardRecyclerAdapter
import com.krishna.bookhub.model.Book
import com.krishna.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    lateinit var progresLayout: RelativeLayout

    lateinit var progressBar: ProgressBar

    var ratingComparator = Comparator<Book>{book1, book2 ->
       if( book1.bookRating.compareTo(book2.bookRating, true)==0){
           book1.bookName.compareTo(book2.bookName, true)
       }else{
           book1.bookRating.compareTo(book2.bookRating, true)
       }
    }

    var bookInfoList = arrayListOf<Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)

        progresLayout = view.findViewById(R.id.progressLayout)

        progressBar = view.findViewById(R.id.progressBar)

        progresLayout.visibility = View.VISIBLE


        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books"

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                    try {

                        progresLayout.visibility = View.GONE
                        val success = it.getBoolean("success")
                        if (success) {

                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {

                                val jsonObject = data.getJSONObject(i)

                                val bookObject = Book(
                                    jsonObject.getString("book_id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("author"),
                                    jsonObject.getString("rating"),
                                    jsonObject.getString("price"),
                                    jsonObject.getString("image")
                                )
                                bookInfoList.add(bookObject)

                            }
                            layoutManager = LinearLayoutManager(activity)

                            recyclerAdapter =
                                DashboardRecyclerAdapter(activity as Context, bookInfoList)

                            recyclerDashboard.adapter = recyclerAdapter

                            recyclerDashboard.layoutManager = layoutManager

                        } else {

                            Toast.makeText(
                                activity as Context,
                                "some error Ocuured",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    } catch (e: JSONException) {

                        Toast.makeText(
                            activity as Context,
                            "Some Unexpected error occured!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }, Response.ErrorListener {
                    if (activity != null) {

                        Toast.makeText(
                            activity as Context,
                            "Volley error occured",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }) {

                    //here we will send headers
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "0f3097da6dee50"
                        return headers
                    }

                }


            queue.add(jsonObjectRequest)
        } else {

            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("open Setting") { text, listener ->

                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()

            }
            dialog.setNegativeButton("Exit") { text, listener ->

                ActivityCompat.finishAffinity(activity as Activity)


            }
            dialog.create()
            dialog.show()
        }



        return view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if(id== R.id.action_sort){
            Collections.sort(bookInfoList, ratingComparator)
            bookInfoList.reverse()
            recyclerAdapter.notifyDataSetChanged()

        }
        return super.onOptionsItemSelected(item)
    }

}
