package com.RR.foodycookbook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import com.RR.foodycookbook.R
import com.RR.foodycookbook.fragment.DashboardFragment
import com.RR.foodycookbook.fragment.FavouriteFragment

class MainActivity : AppCompatActivity()
{
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var FavButton:Button
    lateinit var search_bar:SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coordinatorLayout=findViewById(R.id.coordinateLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frame)
        FavButton=findViewById(R.id.btnFavButton)
        search_bar=findViewById(R.id.search_bar)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Foody CookBook"

        supportFragmentManager.beginTransaction().replace(R.id.frame, DashboardFragment(search_bar)).commit()
        search_bar.visibility=View.VISIBLE
        FavButton.visibility=View.VISIBLE
        FavButton.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.frame,FavouriteFragment()).commit()
            search_bar.visibility=View.GONE
            FavButton.visibility=View.GONE
        }
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frame)

        when(frag){
            !is DashboardFragment ->supportFragmentManager.beginTransaction().replace(R.id.frame, DashboardFragment(search_bar)).commit()

            else->
            {
                ActivityCompat.finishAffinity(this@MainActivity)
            }
            //else->super.onBackPressed()
        }
    }
}