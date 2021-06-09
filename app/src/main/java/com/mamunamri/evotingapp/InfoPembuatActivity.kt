package com.mamunamri.evotingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class InfoPembuatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_pembuat)
        try {
            supportActionBar?.hide()
        }catch (e : Exception){

        }
    }
}