package com.raz.restapilist

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.raz.restapilist.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: BlogDataAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        layoutManager= LinearLayoutManager(this)


        BlogApi.retrofitService.getData().enqueue(object:
           retrofit2.Callback<List<BlogData>> {
            override fun onResponse(
                call: Call<List<BlogData>>,
                response: Response<List<BlogData>>
            ) {
                val data=response.body()
                adapter=BlogDataAdapter(this@MainActivity,data!!)
                binding.lista.adapter=adapter
                binding.lista.layoutManager=layoutManager
            }

            override fun onFailure(call: Call<List<BlogData>>, t: Throwable) {
                Log.e("Api hiba","Api hívás hiba!")
            }

        }
        )

    }
}