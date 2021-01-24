package com.RR.foodycookbook.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.RR.foodycookbook.database.DishDataBase
import com.RR.foodycookbook.database.DishEntity
import com.RR.foodycookbook.adapter.FavouriteRecyclerAdapter
import com.RR.foodycookbook.R

class FavouriteFragment : Fragment() {
    lateinit var recyclerFavorite: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var recyclerFavAdapter: FavouriteRecyclerAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    var dbDishList= listOf<DishEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_favourite, container, false)

        recyclerFavorite=view.findViewById(R.id.recyclerFavourite)
        progressLayout=view.findViewById(R.id.progressLayoutFav)
        progressBar=view.findViewById(R.id.progressBarfav)
        progressLayout.visibility=View.VISIBLE

        layoutManager= LinearLayoutManager(activity)

        dbDishList=RetrieveFavourites(activity as Context).execute().get()

        if(activity!=null)
        {
            progressLayout.visibility=View.GONE
            recyclerFavAdapter= FavouriteRecyclerAdapter(activity as Context,dbDishList)

            recyclerFavorite.adapter=recyclerFavAdapter
            recyclerFavorite.layoutManager=layoutManager
        }
        return view
    }

    class RetrieveFavourites(val context: Context): AsyncTask<Void, Void, List<DishEntity>>()
    {
        override fun doInBackground(vararg p0: Void?): List<DishEntity> {
            val db= Room.databaseBuilder(context, DishDataBase::class.java,"dish-db").build()
            return db.dishDao().getAllDish()
        }

    }
}