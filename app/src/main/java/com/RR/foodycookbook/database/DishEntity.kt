package com.RR.foodycookbook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dishes")
data class DishEntity(
    @PrimaryKey val dish_id:Int,
    @ColumnInfo(name = "dish_name") val dishName:String,
    @ColumnInfo(name = "dish_desc") val dishDesc:String,
    @ColumnInfo(name = "dish_image") val dishImage:String
) {
}