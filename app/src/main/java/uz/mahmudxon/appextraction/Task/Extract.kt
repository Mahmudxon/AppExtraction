package uz.mahmudxon.appextraction.Task

import android.os.Environment
import uz.mahmudxon.appextraction.Model.AppModel
import java.io.File

class Extract {
    companion object {
        fun ExtractApp(app : AppModel)
        {
            val originalFile = File(app.basePath)
            val newFile = File("${Environment.getExternalStorageDirectory().absolutePath}/Absolyut/Apps/${app.name.replace(" ", "")}.apk")
            originalFile.copyTo(newFile, true, 1024)
        }


        fun getExtractionApps(): Array<out File>? {
            val dir = File(Environment.getExternalStorageDirectory().absolutePath)
            return dir.listFiles()
        }
    }
}