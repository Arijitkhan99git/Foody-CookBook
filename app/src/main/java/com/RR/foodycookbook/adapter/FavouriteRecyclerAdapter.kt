package com.RR.foodycookbook.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.RR.foodycookbook.database.DishEntity
import com.RR.foodycookbook.R
import com.RR.foodycookbook.activity.DescriptionActivity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter (val context: Context,val dishFavList:List<DishEntity>):
    RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>()
{
    class FavouriteViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val txtFavDishName: TextView =view.findViewById(R.id.txtFavDishName)
        val imgFavDishImage: ImageView =view.findViewById(R.id.imgFavDishImage)
        val llFavcontent: LinearLayout =view.findViewById(R.id.llFavcontent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.recycler_fav_single_row,parent,false)

        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val dish=dishFavList[position]

        holder.txtFavDishName.text=dish.dishName
        Picasso.get().load(dish.dishImage).error(R.drawable.default_foody_cover).into(holder.imgFavDishImage)

        val dishId=dish.dish_id.toString()

        holder.llFavcontent.setOnClickListener {
            val intent= Intent(context, DescriptionActivity::class.java)
            intent.putExtra("idMeal",dishId)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return dishFavList.size
    }
}