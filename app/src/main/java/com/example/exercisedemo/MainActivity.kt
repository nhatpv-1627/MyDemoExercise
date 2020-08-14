package com.example.exercisedemo

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var executorService: ThreadPoolExecutor

    private var adapter: ImageRecyclerAdapter? = null
    private var errorHandler = CoroutineExceptionHandler { _, throwable ->
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, "Caught $throwable", Toast.LENGTH_SHORT).show()
        }
    }

    private var isBounding = false
    private var downloadService: DownloadService? = null

    // create a connection
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            isBounding = true
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as DownloadService.DownloadBinder
            downloadService = binder.getService()
            downloadService?.notifyUiCallback = object : DownloadService.NotifyUiCallback {
                override fun notifyProcess(process: Pair<Int, Int>) {
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.uploaded_in_sum, process.first, process.second),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
            isBounding = false
        }

    }

    override fun onStart() {
        super.onStart()
        // bind to service
        Intent(this, DownloadService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        handleEvents()
        listenerHandler()
    }

    private fun handleEvents() {
        val intent = Intent(this, DownloadService::class.java)
        intent.putExtra(EXTRA_IMAGE_LIST, ListData(sampleData()))
        btnDownloadAll.setOnClickListener {
            startService(intent)
        }
    }

    /**
     * other method to get data from Message class
     * post(Runnable)
     * postAtTime(Runnable, long)
     * postDelayed(Runnable, Object, long)
     * */

    private fun listenerHandler() {
        handler = Handler(Looper.getMainLooper(), Handler.Callback { mess ->
            when (mess.what) {
                DOWNLOAD_IMAGE_SUCCESS_CODE -> {
                    Toast.makeText(
                        this@MainActivity,
                        "Image: ${mess.data.get(EXTRA_DOWNLOAD_SUCCESS)}, Downloaded ${executorService.completedTaskCount}/${executorService.taskCount} ",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
                DOWNLOAD_IMAGE_FAIL_CODE -> {
                    Toast.makeText(this@MainActivity, "An error has occurred", Toast.LENGTH_SHORT)
                        .show()
                    true
                }
                else -> false
            }
        })
    }

    private fun initViews() {
        executorService = ThreadPoolExecutor(
            CORE_THREAD_NUM, MAX_THREAD_NUM, THREAD_ALIVE_TIME, TimeUnit.SECONDS,
            LinkedBlockingQueue<Runnable>()
        )

        //val executorService = Executors.newFixedThreadPool(5) as ThreadPoolExecutor
        adapter = ImageRecyclerAdapter(lifecycleScope, sampleData(), errorHandler, {
            requestPermission {
                onImageItemClick(it)
            }
        }) {
            val runnable = DownloadRunnable(it, this@MainActivity, { path ->
                runOnUiThread {
                    // TODO handle with UI
                }
            }, handler)
            executorService.execute(runnable)
        }

        rvImages.adapter = adapter
    }

    private fun onImageItemClick(image: ImageData) =
        lifecycleScope.launch(Dispatchers.IO + errorHandler) {
            val imagePath = saveImageToExternalStorage(image, this@MainActivity)
            val txtPath = saveTextFileToCache(image.id, getCurrentTime(), this@MainActivity)
            val textContent = readTextFromFile(txtPath)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@MainActivity,
                    "Text Content: $textContent",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun requestPermission(onAllPermissionGranted: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onAllPermissionGranted()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_PERMISSION_CODE
            )
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
        isBounding = false
    }

    override fun onDestroy() {
        adapter?.deleteCacheData()
        super.onDestroy()
    }

    private fun sampleData() =
        listOf(
            ImageData(1, "https://gamerviet.vn/wp-content/uploads/2020/06/farcry-ava.jpg"),
            ImageData(
                2,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQ3O9pcU3dv4piQ6tVm8pqgTibAEk0owWeD1Q&usqp=CAU"
            ),
            ImageData(
                3,
                "https://gamek.mediacdn.vn/133514250583805952/2020/7/13/photo-1-15946543192861754529377.jpg"
            ),
            ImageData(
                4,
                "https://lh3.googleusercontent.com/qEzxi2OW7mv5DWGB6vlELfUIIQkabV3CDumckLU91enyATXaGEyOl5Tr_ZL0srsRN9t58D0FlA=w640-h400-e365"
            ),
            ImageData(
                5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcR5gjsUg-zrtFroJsX4nFJFEcl4XY9v8kaYrQ&usqp=CAU"
            ),
            ImageData(6, "https://vn-test-11.slatic.net/p/fc5801ccf5873562f7362a96e87d4ed9.jpg"),
            ImageData(7, "https://steamcdn-a.akamaihd.net/steam/apps/939960/header.jpg"),
            ImageData(
                8,
                "https://www.takadada.com/wp-content/uploads/2018/04/tr%C3%B2-c%C6%A1i-Far-Cry-5-1024x633.png"
            ),
            ImageData(
                9,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQYS8nmLIx-rZbBmIJ_143-EoxWkagjQ1pcMw&usqp=CAU"
            ),
            ImageData(10, "https://taiphanmempc.net/wp-content/uploads/2018/04/far-cry-5-2.jpg"),
            ImageData(
                11,
                "https://chiasecongnghe.net/wp-content/uploads/2019/03/far-cry-primal-pc1.jpg"
            ),
            ImageData(
                12,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRYbHeENMwyksZuOpUpKubYVShnJ0AtM2RCbQ&usqp=CAU"
            ),
            ImageData(
                13,
                "https://1.bp.blogspot.com/-ryWe9ALs-Yo/XJcAXhDyk_I/AAAAAAAAE-s/OY-PGJmgKlIhwr7iYewxxiqLO9l7mMjNgCLcBGAs/s1600/Far-cry-2.jpg"
            ),
            ImageData(
                14,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSia4s9lYEg-WwB9SqUCVh5A2vzXbWJj1oSow&usqp=CAU"
            ),
            ImageData(
                15,
                "https://store.ubi.com/on/demandware.static/-/Sites-masterCatalog/default/dwffb873b1/images/pdpbanner/575ffd98a3be1633568b4d6c.jpg"
            ),
            ImageData(16, "https://icongnghe.com/wp-content/uploads/2019/05/far-cry-4.jpg"),
            ImageData(
                17,
                "https://www.windowscentral.com/sites/wpcentral.com/files/styles/large/public/field/image/2018/03/far%20cry%205.jpg"
            ),
            ImageData(
                18,
                "https://i.a4vn.com/2011/8/4/gamescom-2011-chiem-nguong-phong-canh-long-lay-trong-farcry-3-1f1779.jpg"
            ),
            ImageData(
                19,
                "https://chillgame.net/wp-content/uploads/2020/04/tai-game-far-cry-3-780x450.jpg"
            ),
            ImageData(
                20,
                "https://media.wired.com/photos/5ac7c361624d1d6458b7898a/master/pass/FarCry5Review.jpg"
            ),
            ImageData(
                21,
                "https://www.pdvg.it/wp-content/uploads/2018/12/far-cry-new-dawn-reveal-trailer_n9eh.jpg"
            ),

            ImageData(
                22, "https://i.pinimg.com/236x/2d/b2/48/2db2483ecacc4f1da022cb283aed9385.jpg"
            ),

            ImageData(
                23, "https://i.pinimg.com/236x/6a/f5/19/6af5195f0015fd5afaf004e6842545e6.jpg"
            ),

            ImageData(
                24, "https://i.pinimg.com/236x/d6/24/0c/d6240ce5194df9221c8285a6bd621c64.jpg"
            ),

            ImageData(
                25, "https://i.pinimg.com/236x/36/56/52/365652a4442df270b7757bf52ec26faf.jpg"
            ),

            ImageData(
                26, "https://i.pinimg.com/236x/d6/53/3f/d6533fc7eba98ff6b8a54e08c8035841.jpg"
            ),

            ImageData(
                27, "https://i.pinimg.com/236x/f1/5e/f9/f15ef97cec7440983fc57b9cdd7b5bf4.jpg"
            ),

            ImageData(
                28, "https://i.pinimg.com/236x/7b/52/27/7b52275611556939edf8e9955bb60516.jpg"
            ),

            ImageData(
                29, "https://i.pinimg.com/236x/48/bd/fe/48bdfe67fdeeb66a616d377b3fd05790.jpg"
            ),

            ImageData(
                30, "https://i.pinimg.com/236x/87/fb/54/87fb54b144a1e9bb6a9bc9ba73e9430a.jpg"
            )
        )
}
