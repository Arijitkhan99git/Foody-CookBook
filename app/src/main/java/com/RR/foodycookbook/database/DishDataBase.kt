package com.RR.foodycookbook.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DishEntity::class],version = 1)
abstract class DishDataBase:RoomDatabase() {
    abstract fun dishDao(): DishDao
}