package com.raz.lista

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.raz.lista.databinding.FragmentListBinding


class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: PersonAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var adatok= listOf(
            Person("Kiss","Elek",1999),
            Person("Kovács","Endre",1998),
            Person("Kálmán","Ubul",1997),
            Person("Kiss","Elek",1999),
            Person("Kovács","Endre",1998),
            Person("Kiss","Ubul",1997),
            Person("Kálmán","Elek",1999),
            Person("Kósa","Endre",1998),
            Person("Kiss","Ubul",1997),
            Person("Nagy","Elek",1999),
            Person("Veress","Endre",1998),
            Person("Kiss","Ubul",1997)

        )

        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_list,container,false)

        val navController=this.findNavController()

        adapter= PersonAdapter(requireContext(),adatok){
            itemDto:Person,position:Int->
            Log.i("Click","${itemDto.keresztnev}")
            navController.navigate(ListFragmentDirections.actionListFragmentToDetailFragment(itemDto))

        }
        layoutManager= LinearLayoutManager(requireContext())
        binding.lista.layoutManager=layoutManager
        binding.lista.adapter=adapter

        return binding.root
        //return inflater.inflate(R.layout.fragment_list, container, false)
    }


}