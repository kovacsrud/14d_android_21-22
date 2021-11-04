package com.raz.szamlalo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PracticeViewModel:ViewModel() {

    var ertek=MutableLiveData<Int>()
    init {
        ertek.value=0
    }

    fun Novel(){
        ertek.value=ertek.value?.plus(1)
    }
    fun Csokkent(){
        ertek.value=ertek.value?.minus(1)
    }

}