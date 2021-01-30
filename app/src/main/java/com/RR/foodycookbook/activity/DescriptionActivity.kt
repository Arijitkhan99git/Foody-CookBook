package com.RR.foodycookbook.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.RR.foodycookbook.database.DishDataBase
import com.RR.foodycookbook.database.DishEntity
import com.RR.foodycookbook.R
import com.RR.foodycookbook.util.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {
    lateinit var destoolbar: Toolbar
    lateinit var txtDishName: TextView
    lateinit var imgDesDishImage: ImageView
    lateinit var txtAboutDish: TextView
    lateinit var btnAddToFavDish: Button
    lateinit var DesProgressLayout: RelativeLayout
    lateinit var DesprogressBar: ProgressBar
    var dishId:String="100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        destoolbar=findViewById(R.id.DesToolbar)
        txtDishName=findViewById(R.id.txtDishName)
        imgDesDishImage=findViewById(R.id.imgDesDishImage)
        txtAboutDish=findViewById(R.id.txtAboutDish)
        btnAddToFavDish=findViewById(R.id.btnAddToFavDish)
        DesProgressLayout=findViewById(R.id.DesProgressLayout)
        DesprogressBar=findViewById(R.id.DesProgressBar)

        setSupportActionBar(destoolbar)
        supportActionBar?.title = "Description"

        DesProgressLayout.visibility = View.VISIBLE
        DesprogressBar.visibility = View.VISIBLE



        if (intent != null) {
            dishId = intent?.getStringExtra("idMeal").toString()
        } else {
            Toast.makeText(this@DescriptionActivity, "Some unexpected error occurred", Toast.LENGTH_SHORT)
                .show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=$dishId"

       // val jsonParams = JSONObject()
     //  jsonParams.put("i", dishId)

        if (ConnectionManager().checkConnetivity(this@DescriptionActivity))
        {
            val jsonRequest = object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener {
                    try {
                            val dishJsonObject = it.getJSONArray("meals")

                            //iff success ProgressLayout Gone
                            DesProgressLayout.visibility = View.GONE

                            val data=dishJsonObject.getJSONObject(0)

                            val imageUrl=data.getString("strMealThumb")

                            Picasso.get().load(data.getString("strMealThumb"))
                                .error(R.drawable.default_foody_cover).into(imgDesDishImage)

                            txtDishName.text = data.getString("strMeal")
                            txtAboutDish.text = data.getString("strInstructions")

                            val dishEntity= DishEntity(
                                dishId.toInt(),
                                txtDishName.text.toString(),
                                txtAboutDish.text.toString(),
                                imageUrl
                            )

                            val checkFav=DBAsyncTask(applicationContext,dishEntity,1).execute()
                            val isFav=checkFav.get()

                            if (isFav)
                            {
                                btnAddToFavDish.text="Remove from Favourite"
                                val favColor= ContextCompat.getColor(applicationContext,R.color.colorFav)
                                btnAddToFavDish.setBackgroundColor(favColor)
                            }
                            else
                            {
                                btnAddToFavDish.text="Add to Favourite"
                                val nofavColor= ContextCompat.getColor(applicationContext,R.color.colorNoFav)
                                btnAddToFavDish.setBackgroundColor(nofavColor)
                            }

                            //added OnClickListner on Favourite Button
                            //check DBAsyncTask(applicationContext,bookEntity,1).execute().get()==isFav is not added on
                            // Favortes then add this item

                            btnAddToFavDish.setOnClickListener{

                                if(!DBAsyncTask(applicationContext,dishEntity,1).execute().get())
                                {
                                    val async=DBAsyncTask(applicationContext,dishEntity,2).execute()
                                    val result=async.get()

                                    if (result)
                                    {
                                        Toast.makeText(this@DescriptionActivity,"Added to Favourites",Toast.LENGTH_SHORT).show()

                                        btnAddToFavDish.text="Remove from Favourites"
                                        val favColor= ContextCompat.getColor(applicationContext,R.color.colorFav)
                                        btnAddToFavDish.setBackgroundColor(favColor)

                                    }
                                    else {
                                        Toast.makeText(this@DescriptionActivity,"Some error occurred !",Toast.LENGTH_SHORT).show()
                                    }

                                }
                                else
                                {
                                    val async=DBAsyncTask(applicationContext,dishEntity,3).execute()
                                    val result=async.get()

                                    if (result)
                                    {
                                        Toast.makeText(this@DescriptionActivity,"Remove from Favourites",Toast.LENGTH_SHORT).show()

                                        btnAddToFavDish.text="Add to Favourites"
                                        val nofavColor= ContextCompat.getColor(applicationContext,R.color.colorNoFav)
                                        btnAddToFavDish.setBackgroundColor(nofavColor)
                                    }
                                    else
                                    {
                                        Toast.makeText(this@DescriptionActivity,"Some error occurred !",Toast.LENGTH_SHORT).show()
                                    }
                                }

                            }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some error occurred !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Some error occurred !",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()

                    headers["Context-Type"] = "application/json"
                    headers["token"] = "1"

                    return headers
                }
            }
            queue.add(jsonRequest)

        }
        else
        {

            val dialog = AlertDialog.Builder(this@DescriptionActivity)

            dialog.setTitle("Failure")
            dialog.setMessage("Internet Connection not Found")

            dialog.setPositiveButton("Open Settings") { text, Listner ->
                //do Somethings
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                this@DescriptionActivity.finish()
            }
            dialog.setNegativeButton("Exit") { text, Listner ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }

            dialog.create()
            dialog.show()
        }
    }
    class DBAsyncTask(val context: Context, val dishEntity: DishEntity, val mode:Int):
        AsyncTask<Void, Void, Boolean>()
    {
        //databaseBuilder is a mandatory fun to create db variable
        val db= Room.databaseBuilder(context, DishDataBase::class.java,"dish-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode)
            {
                1-> {
                    //check DB if the book is added on favourite or not
                    val dish: DishEntity?=db.dishDao().getDishById(dishEntity.dish_id.toString())
                    db.close()

                    return dish!=null
                }

                2-> {
                    //save the book into DB as favourite
                    db.dishDao().insertDish(dishEntity)
                    return true

                }

                3-> {
                    //remove the book from DB
                    db.dishDao().deleteDish(dishEntity)
                    return true
                }
            }

            return false
        }

    }
}