package com.example.exercisedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var adapter: ImageRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ImageRecyclerAdapter(sampleData())
        rvImages.adapter = adapter
    }

    private fun sampleData() =
        listOf(
            "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSia4s9lYEg-WwB9SqUCVh5A2vzXbWJj1oSow&usqp=CAU",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTrz8Hhm-0dhJSk29jkT0_7NMpRN8ZnTLzbIA&usqp=CAU",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_07/20476356_1142597785885275_1808459006299505396_n.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_07/20634918_132814590660087_307531375064383488_n_1.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_07/ghnghcb1498924076013.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_07/gnvb1498924649670.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_07/47585651_284531932246112_2319524595343373318_n.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_07/20245337_1137706686374385_5487629589063860599_n.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_07/55538069_2269196603400738_1403165742588755968_n.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_07/55719274_2270456423274756_4069031353142738944_n.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_07/52253082_2243648915955507_9114267721368862720_o.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_07/58950005_2293435560976842_1995073838693482496_o.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_06/18812488_467925666890882_1679508705827094528_n.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_06/44457255_313169076076016_494367870566978777_n.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_06/44573119_2107296622855811_6042124850338295381_n.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_06/z1368343314510_04d8b2e81e7a1c4817317f236d76d79e_1.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_06/42111177_1129334367221576_6819849037399719936_n.jpg",
            "https://znews-photo.zadn.vn/w660/Uploaded/cqdhmdxwp/2019_05_06/47425732_1177842502370762_3215275164178579456_n.jpg"
        )
}