package uz.mahmudxon.appextraction.Dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import uz.mahmudxon.appextraction.R

class MyDialog : AlertDialog {

    private var listener : ((Boolean) -> Unit) ?= null

    fun setOnNavtiveButtonClickListener(data : ((Boolean) -> Unit)) {
        listener = data
    }

constructor(context: Context) : super(context)
{
    setButton(DialogInterface.BUTTON_POSITIVE, "Ok", object : DialogInterface.OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            cancel()
        }

    })
    setButton(DialogInterface.BUTTON_NEUTRAL, "Show Apk(s)", object : DialogInterface.OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            listener?.invoke(true)
        }

    })
    setView(LayoutInflater.from(context).inflate(R.layout.dialog_layout, null, false))
}



}