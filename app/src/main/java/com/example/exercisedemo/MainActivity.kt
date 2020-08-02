package com.example.exercisedemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var adapter: ImageRecyclerAdapter? = null
    private var errorHandler = CoroutineExceptionHandler { _, throwable ->
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, "Caught $throwable", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = ImageRecyclerAdapter(lifecycleScope, sampleData(), errorHandler)
        rvImages.adapter = adapter
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
            )
        )
}