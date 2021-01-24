package com.RR.foodycookbook.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DishDao {
    @Insert
    fun insertDish(bookEntity: DishEntity)

    @Delete
    fun deleteDish(bookEntity: DishEntity)

    @Query("SELECT * FROM dishes")
    fun getAllDish():List<DishEntity>

    @Query("SELECT * FROM dishes WHERE dish_id=:dishId")
    fun getDishById(dishId:String): DishEntity
}