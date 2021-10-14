package com.raz.szamolas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var a_ertek:EditText=findViewById(R.id.a_ertek)
        var b_ertek:EditText=findViewById(R.id.b_ertek)
        var eredmeny_szoveg:TextView=findViewById(R.id.eredmeny)
        var gomb: Button =findViewById(R.id.gomb)

        gomb.setOnClickListener {

            try{
                var eredmeny=a_ertek.text.toString().toInt()+b_ertek.text.toString().toInt()
                eredmeny_szoveg.text=eredmeny.toString()
            } catch (ex:Exception){
                Log.e("Hiba!",ex.message.toString())
                Toast.makeText(this,ex.message,Toast.LENGTH_SHORT).show()
            }

        }








    }
}