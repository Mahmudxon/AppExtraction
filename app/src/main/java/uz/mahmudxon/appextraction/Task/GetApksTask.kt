package uz.mahmudxon.appextraction.Task

import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import uz.mahmudxon.appextraction.Model.AppModel
import java.io.File

class GetApksTask(val context: Context) : AsyncTask<Void, Void, ArrayList<AppModel>>()
{

    private var listener : ((ArrayList<AppModel>) -> Unit) ?= null

    fun OnTaskFinishListener(data : ((ArrayList<AppModel>) -> Unit)) {
        listener = data
    }


    override fun doInBackground(vararg params: Void?): ArrayList<AppModel> {
        val data = ArrayList<AppModel>()
        var count = 0
        var dir = File("${Environment.getExternalStorageDirectory().absolutePath}/Absolyut/Apps")

        if(dir != null && dir.exists() && dir.isDirectory)
        {
            if(dir.listFiles() != null){
                for (apk in dir.listFiles())
                {

                    if(apk.path.endsWith(".apk", true))
                    {
                        val pm = context.packageManager
                        var packageInfo = pm.getPackageArchiveInfo(apk.path, PackageManager.GET_ACTIVITIES)
                        if(packageInfo != null) {
                            val appInfo = packageInfo.applicationInfo
                            if (Build.VERSION.SDK_INT >= 8)
                            {
                                appInfo.sourceDir = apk.path
                                appInfo.publicSourceDir = apk.path
                            }



                            data.add(AppModel(count++, appInfo.loadLabel(pm).toString(), appInfo.loadIcon(pm), appInfo.packageName, apk.path, false))
                        }
                    }
                }

            }
        }



        return data
  }

    override fun onPostExecute(result: ArrayList<AppModel>?) {
        result?.let { this.listener?.invoke(it) }
    }
}