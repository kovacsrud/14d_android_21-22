package com.raz.blogapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.raz.blogapi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)

        BlogApi.retrofitService.getData().enqueue(object:
            retrofit2.Callback<BlogAdat> {
            override fun onResponse(call: Call<BlogAdat>, response: Response<BlogAdat>) {
                val valasz=response.body()
                binding.blogadat=valasz
            }

            override fun onFailure(call: Call<BlogAdat>, t: Throwable) {
                Log.e("Api hiba","Api hívás hiba")
            }

        }
        )

    }
}