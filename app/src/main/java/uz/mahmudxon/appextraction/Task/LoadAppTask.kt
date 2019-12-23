package uz.mahmudxon.appextraction.Task

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.AsyncTask
import uz.mahmudxon.appextraction.Model.AppModel
import java.util.*
import kotlin.collections.ArrayList

class LoadAppTask(val packageManager: PackageManager) : AsyncTask<Void, Void, ArrayList<AppModel>>()
{

    private var listener : ((ArrayList<AppModel>) -> Unit) ?= null

    fun OnTaskFinishListener(data : ((ArrayList<AppModel>) -> Unit)) {
        listener = data
    }


    override fun doInBackground(vararg params: Void?): ArrayList<AppModel> {

        val data = ArrayList<AppModel>()


        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        var apps =  packageManager.queryIntentActivities(mainIntent, 0) //packageManager.getInstalledApplications(0)
       apps.sortWith(compareBy{it.loadLabel(packageManager).toString()})
        if (apps != null) {
            var count = 0
            for (app in apps)
            {
                val appInfo = app.activityInfo.applicationInfo
                data.add(AppModel(count++, appInfo.loadLabel(packageManager).toString(), appInfo.loadIcon(packageManager), appInfo.packageName, appInfo.sourceDir, false))
            }

        }

        return  data
    }

    override fun onPostExecute(result: ArrayList<AppModel>?) {
        result?.let { listener?.invoke(it) }
    }

}