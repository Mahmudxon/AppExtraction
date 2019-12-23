package uz.mahmudxon.appextraction.Fragment


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_exttraction_apps.*
import kotlinx.android.synthetic.main.fragment_exttraction_apps.view.*
import uz.mahmudxon.appextraction.Adapter.ListAdapter
import uz.mahmudxon.appextraction.Adapter.ListAdapter.OnCheckedChangeListener
import uz.mahmudxon.appextraction.BuildConfig
import uz.mahmudxon.appextraction.MainActivity
import uz.mahmudxon.appextraction.Model.AppModel

import uz.mahmudxon.appextraction.R
import uz.mahmudxon.appextraction.Task.GetApksTask
import java.io.File

class ExttractionAppsFragment : Fragment() {
lateinit var adapter: ListAdapter
    lateinit var data:ArrayList<AppModel>
    var count = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exttraction_apps, container, false)

        view.back_activity.setOnClickListener {
            (activity as MainActivity).finishFragment()
        }

        view.trash_id.setOnClickListener {
             //  Nothing   ;-)
        }


        data = ArrayList()
        adapter = ListAdapter(data)
        view.list_fr.adapter = adapter
        adapter.onCheckedChangeListener = object : OnCheckedChangeListener
        {
            override fun onCheckedChange(id: Int, isChecked: Boolean) {
                data[id].isChecked = isChecked

                if(count < 0) count = 0

                if(isChecked) count ++
                else count --


                view.btn_share.isEnabled = count > 0
            }

        }



        view.list_fr.layoutManager = LinearLayoutManager(requireContext())
        view.progress_circular_fr.visibility = View.VISIBLE
        val task = GetApksTask(requireContext())
        task.OnTaskFinishListener {
            view.progress_circular_fr?.visibility = View.GONE
            data = it
            adapter.data = data
            adapter.notifyDataSetChanged()
        }

        task.execute()

        view.btn_share.setOnClickListener {
          val tempUri = ArrayList<Uri>()

            for (app in data) {
                if (app.isChecked) {
                    app.isChecked = true


                    Log.i("MyLog", app.basePath)
                    tempUri.add(
                       // if (Build.VERSION.SDK_INT >= 7) {
                            FileProvider.getUriForFile(requireContext(), "uz.mahmudxon.appextraction.provider", File(app.basePath))

                            //Uri.parse(Uri.fromFile(File(app.basePath)).toString().replace("file:", "content"))
                       // } else {
                        //    Uri.fromFile(File(app.basePath))
                       // }
                  )


                  //  tempUri.add(Uri.parse("${app.basePath}"))

                }
            }

            val intent = Intent()
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.action = Intent.ACTION_SEND_MULTIPLE
            intent.type = "*/*"
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, tempUri)
                startActivity(intent)
            adapter.data = data
            adapter.notifyDataSetChanged()


        }
        return view
    }


}
