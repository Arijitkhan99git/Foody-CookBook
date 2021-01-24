package com.RR.foodycookbook.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.RR.foodycookbook.R
import com.RR.foodycookbook.adapter.DashboardRecyclerAdapter
import com.RR.foodycookbook.model.Dish
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import com.RR.foodycookbook.util.ConnectionManager


class DashboardFragment(val search_bar: androidx.appcompat.widget.SearchView) : Fragment() {
    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var btnDInternet: Button
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    val DishInfoList = arrayListOf<Dish>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.dashboard_menu, container, false)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)

        layoutManager = LinearLayoutManager(activity)

        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)

        recyclerDashboard.layoutManager = layoutManager
        recyclerAdapter =DashboardRecyclerAdapter(activity as Context, DishInfoList)
        recyclerDashboard.adapter = recyclerAdapter

        //show the progeess Layout
        progressLayout.visibility=View.VISIBLE
        search_bar.setOnQueryTextListener(
                object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        recyclerAdapter.filter.filter(newText)
                        return false
                    }
                }
        )
        val queue =Volley.newRequestQueue(activity as Context)
        val url="https://www.themealdb.com/api/json/v1/1/random.php"

        if (ConnectionManager().checkConnetivity(activity as Context))
        {
                DishInfoList.clear()

                val jsonObjectRequest =
                object : JsonObjectRequest(
                    Method.GET, url, null, Response.Listener {
                        //here we will handle the response

                        try {
                            progressLayout.visibility = View.GONE

                            println("Resonse is $it")
                            //if get Response is success fetch the data from JSONArray
                            val data = it.getJSONArray("meals")

                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)

                                val dishObject = Dish(
                                    bookJsonObject.getString("idMeal"),
                                    bookJsonObject.getString("strMeal"),
                                    bookJsonObject.getString("strMealThumb")
                                )

                                DishInfoList.add(dishObject)
                                recyclerAdapter.notifyDataSetChanged()
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(
                                activity as Context,
                                "some Unexpected error occoured",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    Response.ErrorListener {
                        //here we will handle the errors
                        // println("error is $it")
                        if (activity != null) {
                            Toast.makeText(
                                activity as Context,
                                "Volley error occoured",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })

                //implements gerHeaders Method
                {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()

                        headers["Content-type"] = "application/json"
                        headers["token"] = "1"
                        return headers
                    }
                }

            for (j in 1..10)
            {
            queue.add(jsonObjectRequest)
            }

        }
        else
        {
            val dialog = AlertDialog.Builder(activity as Context)

            dialog.setTitle("Failure")
            dialog.setMessage("Internet Connection not Found")

            dialog.setPositiveButton("Open Settings") { text, Listner ->
                //do Somethings
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, Listner ->
                ActivityCompat.finishAffinity(activity as Activity)
            }

            dialog.create()
            dialog.show()
        }

        return view
    }


}