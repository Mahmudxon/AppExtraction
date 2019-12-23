package uz.mahmudxon.appextraction.Adapter

import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import uz.mahmudxon.appextraction.Model.AppModel
import uz.mahmudxon.appextraction.R
import java.io.File

class ListAdapter (var data : ArrayList<AppModel>) : RecyclerView.Adapter<ListAdapter.ViewHolder>()
{
    var onCheckedChangeListener : OnCheckedChangeListener ?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {

        fun bind ()
        {
            itemView.apply {
                item_check.isChecked = data[adapterPosition].isChecked
                var width : Float = ((File(data[adapterPosition].basePath).length().toFloat()/1024)/1024)
                item_name.text = data[adapterPosition].name
                item_img.setImageDrawable(data[adapterPosition].icon)
                item_width.text = "%.2f Mb".format(width)

                item_check.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener
                {
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                        data[adapterPosition].isChecked = isChecked
                        onCheckedChangeListener?.onCheckedChange(data[adapterPosition].id, isChecked)
                    }


                })
                setOnClickListener {
                    item_check.isChecked = !item_check.isChecked
                }
            }
        }
    }

    interface OnCheckedChangeListener
    {
        fun onCheckedChange(id : Int, isChecked : Boolean)
    }
}