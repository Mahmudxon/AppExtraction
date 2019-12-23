package uz.mahmudxon.appextraction.Model

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable

data class AppModel (val id : Int, val name : String, val icon : Drawable, val packName : String, val basePath: String, var isChecked : Boolean)