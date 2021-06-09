package com.mamunamri.evotingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mamunamri.evotingapp.data.DataKetua

class DetailCalonKetuaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_calon_ketua)

        try {
            supportActionBar?.hide()
            findViewById<TextClock>(R.id.btn_kembali).setOnClickListener {
                onBackPressed()
            }
            val id = intent.getStringExtra("nisninten")!!
            fetchData(id)
        }catch (e : Exception){

        }
    }

    private fun fetchData(id: String) {
        FirebaseDatabase.getInstance().getReference("evote/calon_ketua/$id")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val data = snapshot.getValue(DataKetua::class.java)!!
                        findViewById<TextView>(R.id.tv_nisn).text = data.nisn
                        findViewById<TextView>(R.id.tv_nama).text = data.nama
                        findViewById<TextView>(R.id.tv_visi).text = data.visi
                        findViewById<TextView>(R.id.tv_misi).text = data.misi
                        findViewById<TextView>(R.id.tv_prestasi).text = data.prestasi
                        val img = findViewById<ImageView>(R.id.img_ketua)
                        Glide.with(this@DetailCalonKetuaActivity)
                            .load(data.fotouri)
                            .into(img)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}