package com.mamunamri.evotingapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class HomepageSiswa : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage_siswa)

        try {
            supportActionBar?.hide()
            findViewById<View>(R.id.btn_logout)
                .setOnClickListener(this)
            findViewById<CardView>(R.id.siswa_btn_data_siswa)
                .setOnClickListener(this)
            findViewById<CardView>(R.id.siswa_btn_data_suara)
                .setOnClickListener(this)
            findViewById<CardView>(R.id.siswa_btn_data_calon_ketua)
                .setOnClickListener(this)
            findViewById<CardView>(R.id.siswa_btn_data_pembuat)
                .setOnClickListener(this)
        }catch (e : Exception){

        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_logout -> {
                val setSession = setSession(this)
                setSession.clearePref()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

            R.id.siswa_btn_data_siswa -> {
                startActivity(Intent(this,SiswaDataSiswaActivity::class.java))
            }

            R.id.siswa_btn_data_pembuat -> {
                startActivity(Intent(this,InfoPembuatActivity::class.java))
            }

            R.id.siswa_btn_data_suara -> {
                startActivity(Intent(this,DataSuaraActivity::class.java))
            }

            R.id.siswa_btn_data_calon_ketua -> {
                startActivity(Intent(this,SiswaDataCalonKetuaActivity::class.java))
            }
        }
    }
}