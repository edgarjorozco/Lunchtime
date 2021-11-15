package com.edgarjorozco.lunchtime.presentation.placedetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.edgarjorozco.lunchtime.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceDetailActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_fragment)
        val placeId = intent.getStringExtra(PlaceDetailFragment.ARG_PLACE_ID)
        val fragment = PlaceDetailFragment.newInstance(placeId)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.place_detail_fragment_container, fragment)
            .commit()
    }

    companion object {
        fun getCallingIntent(context: Context, placeId: String): Intent {
            val intent = Intent(context, PlaceDetailActivity::class.java).apply {
                putExtra(PlaceDetailFragment.ARG_PLACE_ID, placeId)
            }
            return intent
        }
    }
}