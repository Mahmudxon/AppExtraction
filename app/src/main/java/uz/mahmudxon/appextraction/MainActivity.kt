package uz.mahmudxon.appextraction

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import uz.mahmudxon.appextraction.Adapter.ListAdapter
import uz.mahmudxon.appextraction.Dialog.MyDialog
import uz.mahmudxon.appextraction.Fragment.ExttractionAppsFragment
import uz.mahmudxon.appextraction.Model.AppModel
import uz.mahmudxon.appextraction.Task.ExtractionTask
import uz.mahmudxon.appextraction.Task.LoadAppTask
import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var searchView: SearchView
    lateinit var adapter : ListAdapter
    lateinit var data : ArrayList<AppModel>
    var count = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        data = ArrayList()
        supportActionBar?.title = getString(R.string.app_name)

        val toggle = ActionBarDrawerToggle(this, layout, toolbar, R.string.open, R.string.close)


        val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions, 0)


        navigation_view.setNavigationItemSelectedListener {
            layout.closeDrawer(GravityCompat.START)


            when(it.itemId)
            {
                R.id.inst_apps-> startFragment()
                R.id.exit -> finish()
                R.id.share_this ->
                {

                    val myApp = applicationContext.applicationInfo


                    val path = myApp.sourceDir
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "*/*"
                    val file = File(path)

                        var tempFile = File("$externalCacheDir/ExtractedApk/${getString(myApp.labelRes).replace(" ", "-").toLowerCase()}.apk")
                        tempFile = File("${tempFile.path}/${getString(myApp.labelRes).replace(" ", "-").toLowerCase()}.apk")

                        file.copyTo(tempFile, true, 1024)
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile))
                        startActivity(Intent.createChooser(intent, "Share viva:"))

                }

                R.id.tch ->
                {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/joinchat/AAAAAFWbzYKIYTUe3KgyOg"))
                    startActivity(intent)
                }


            }



            return@setNavigationItemSelectedListener true
        }


        adapter = ListAdapter(ArrayList())
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
        loadData()
        adapter.onCheckedChangeListener = object : ListAdapter.OnCheckedChangeListener {
            override fun onCheckedChange(id: Int, isChecked: Boolean) {
                data[id].isChecked = isChecked

                if(count<0) count = 0

                if(isChecked) count++
                else count --

                    btn_ext.isEnabled = (count > 0)

            }
        }
        btn_ext.setOnClickListener {
            val datatemp = data;
            val task = ExtractionTask(data, this@MainActivity, count)
            task.execute()
            task.setOnTaskFinishListener {
//                for(app in data)
//                    app.isChecked = false

//                adapter.data = data

                if (!searchView?.isIconified())
                {
                    searchView?.setIconified(true)
                }


                count = 0
                adapter.notifyDataSetChanged()
                btn_ext.isEnabled = false
                val dialog = MyDialog(this@MainActivity)
                dialog.setCancelable(false)
                dialog.setOnNavtiveButtonClickListener {
                    dialog.cancel()
                    startFragment()
                }
                dialog.show()
            }
        }
        toggle.syncState()
    }


    private fun loadData()
    {
        progress_circular.visibility = View.VISIBLE
        val task = LoadAppTask(packageManager)
        task.OnTaskFinishListener {
            progress_circular?.visibility = View.GONE


           data = it
            if(!searchView?.isIconified()){
            adapter.data = it
            }
            else
            {
                var newText = searchView.query
                val temp = ArrayList<AppModel>()
                if(newText?.trim()?.length!! > 0)
                {
                    for (app in data)
                    {
                        if(app.name.startsWith(newText, true))
                        {
                            temp.add(app)
                        }
                    }
                    adapter.data = temp
                }
                else
                {
                    adapter.data = data
                }

            }
            adapter.notifyDataSetChanged()
        }
        task.execute()

    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

         searchView = menu!!.findItem(R.id.app_bar_search)!!.actionView as SearchView


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean {


                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val temp = ArrayList<AppModel>()
                if(newText?.trim()?.length!! > 0)
                {
                    for (app in data)
                    {

                        if(app.name.startsWith(newText, true))
                        {
                            temp.add(app)
                        }
                    }
                    adapter.data = temp
                }
                else
                {
                    adapter.data = data
                }

                adapter.notifyDataSetChanged()
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }


    override fun onBackPressed() {

        if (this.layout.isDrawerOpen(GravityCompat.START)) {
            this.layout.closeDrawer(GravityCompat.START)
            return
        }


        if (!searchView?.isIconified())
        {
            searchView?.setIconified(true)
            return
        }


        if(!supportFragmentManager.beginTransaction().isEmpty)
        {
          finishFragment()
            return
        }

        super.onBackPressed()
    }


    fun finishFragment()
    {
        supportFragmentManager.popBackStack()
        navigation_view.visibility = View.VISIBLE
    }

    fun startFragment()
    {
        navigation_view.visibility = View.GONE
        supportFragmentManager.beginTransaction().add(R.id.layout, ExttractionAppsFragment()).addToBackStack("salom").commit()
    }


}
