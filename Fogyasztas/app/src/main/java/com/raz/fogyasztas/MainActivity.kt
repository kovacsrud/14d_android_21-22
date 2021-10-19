package com.raz.fogyasztas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.raz.fogyasztas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var fogyasztas=Fogyasztas(0.0f,0.0f,0.0f)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.fogyasztas=fogyasztas

        binding.gomb.setOnClickListener {
            fogyasztas.tankoltLiter=binding.tankoltLiterEdittext.text.toString().toFloat()
            fogyasztas.megtettKm=binding.megtettKMEdittext.text.toString().toFloat()

            Log.i("Liter",fogyasztas.tankoltLiter.toString())
            Log.i("Km",fogyasztas.megtettKm.toString())


            fogyasztas.atlagFogyasztas=fogyasztas.tankoltLiter/(fogyasztas.megtettKm/100)
            Log.i("Átlagfogyasztás",fogyasztas.atlagFogyasztas.toString())
            binding.invalidateAll()
        }



        //setContentView(R.layout.activity_main)
    }
}