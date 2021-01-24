package com.RR.foodycookbook.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.RR.foodycookbook.R
import com.RR.foodycookbook.activity.DescriptionActivity
import com.RR.foodycookbook.model.Dish
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class DashboardRecyclerAdapter(val context: Context,val itemList:ArrayList<Dish>):
        RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(), Filterable
{
    var filteredListFull=ArrayList<Dish>()
    class DashboardViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        var txtDishName: TextView =view.findViewById(R.id.txtDishName)
        val imgDishImage: ImageView =view.findViewById(R.id.imgDishImage)
        val llcontent: LinearLayout =view.findViewById(R.id.llcontent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        filteredListFull.clear()
        filteredListFull.addAll(itemList)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
       val dish=itemList[position]
        holder.txtDishName.text=dish.dishName

        Picasso.get().load(dish.dishImage).error(R.drawable.default_foody_cover).into(holder.imgDishImage)

        holder.llcontent.setOnClickListener {
            val intent= Intent(context, DescriptionActivity::class.java)
            intent.putExtra("idMeal",dish.dishId)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getFilter(): Filter {
        return newFilteredList
    }
    var newFilteredList=object :Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val tempFilteredList=ArrayList<Dish>()
            if (constraint==null || constraint.length==0)
            {
                tempFilteredList.addAll(filteredListFull)
            }
            else {
                val pattern = constraint.toString().toLowerCase(Locale.ROOT).trim()

                for (item in filteredListFull)
                {
//                    println("item ${item.dishName}")
                    if(item.dishName.contains(pattern,true))
                    {
                        tempFilteredList.add(item)
                    }
                }
            }

            val filterResults=FilterResults()
            filterResults.values=tempFilteredList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            itemList.clear()
            itemList.addAll(results?.values as ArrayList<Dish>)
            notifyDataSetChanged()
        }

    }

}