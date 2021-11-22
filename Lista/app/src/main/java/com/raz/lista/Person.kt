package com.raz.lista

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Person(
    var vezeteknev:String,
    var keresztnev:String,
    var szuletesiEv:Int
):Parcelable {

}
