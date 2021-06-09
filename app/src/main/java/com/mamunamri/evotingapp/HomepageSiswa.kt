package com.mamunamri.evotingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class HomepageSiswa : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage_siswa)

        try {
            supportActionBar?.hide()
            findViewById<View>(R.id.btn_logout)
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
        }
    }
}