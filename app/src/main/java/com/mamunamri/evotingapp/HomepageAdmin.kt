package com.mamunamri.evotingapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView


class HomepageAdmin : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage_admin)

        try {
            supportActionBar?.hide()
            findViewById<CardView>(R.id.admin_btn_data_calon_ketua)
                .setOnClickListener(this)
            findViewById<CardView>(R.id.admin_btn_data_pembuat)
                .setOnClickListener(this)
            findViewById<CardView>(R.id.admin_btn_data_siswa)
                .setOnClickListener(this)
            findViewById<CardView>(R.id.admin_btn_data_suara)
                .setOnClickListener(this)
            findViewById<View>(R.id.btn_logout)
                .setOnClickListener(this)
        }catch (e : Exception){

        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.admin_btn_data_calon_ketua -> {
                val intent = Intent(this, DataCalonKetuaActivity::class.java)
                startActivity(intent)
            }
            R.id.admin_btn_data_pembuat -> {
                val intent = Intent(this, InfoPembuatActivity::class.java)
                startActivity(intent)
            }
            R.id.admin_btn_data_siswa -> {
                val intent = Intent(this, DataSiswaActivity::class.java)
                startActivity(intent)
            }
            R.id.admin_btn_data_suara -> {
                val intent = Intent(this, DataSuaraActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_logout -> {
                val builder = AlertDialog.Builder(this)
                    .setMessage("Keluar ?")
                builder.setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    val setSession = setSession(this)
                    setSession.clearePref()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                })
                builder.setNegativeButton("Batal", DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                })
                builder.create()
                builder.show()
            }
        }
    }
}