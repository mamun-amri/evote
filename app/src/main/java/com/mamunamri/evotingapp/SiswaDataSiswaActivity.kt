package com.mamunamri.evotingapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mamunamri.evotingapp.data.DataUsers
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.list_data_siswa.view.*

class SiswaDataSiswaActivity : AppCompatActivity(), View.OnClickListener {
    var adapter = GroupieAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siswa_data_siswa)

        try {
            supportActionBar?.hide()
            val progressBar : ProgressBar = findViewById(R.id.progressBar)
            progressBar.visibility = View.GONE
            fetchDataSiswa()
            findViewById<RelativeLayout>(R.id.btn_validasi).setOnClickListener(this)
            findViewById<RelativeLayout>(R.id.btn_kelas1).setOnClickListener(this)
            findViewById<RelativeLayout>(R.id.btn_kelas2).setOnClickListener(this)
            findViewById<RelativeLayout>(R.id.btn_kelas3).setOnClickListener(this)
            findViewById<RelativeLayout>(R.id.btn_semua).setOnClickListener(this)
            fetchCount()
        }catch (e : Exception){

        }

    }

    private fun fetchCount() {
        FirebaseDatabase.getInstance().getReference("evote/users").orderByChild("kelas").equalTo("kelas vii")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.childrenCount.toString()
                    findViewById<TextView>(R.id.tv_kls_1).setText(data)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        FirebaseDatabase.getInstance().getReference("evote/users").orderByChild("kelas").equalTo("kelas viii")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.childrenCount.toString()
                    findViewById<TextView>(R.id.tv_kls_2).setText(data)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        FirebaseDatabase.getInstance().getReference("evote/users").orderByChild("kelas").equalTo("kelas ix")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.childrenCount.toString()
                    findViewById<TextView>(R.id.tv_kls_3).setText(data)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        FirebaseDatabase.getInstance().getReference("evote/users").orderByChild("level").equalTo("siswa")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.childrenCount.toString()
                    findViewById<TextView>(R.id.tv_all).setText(data)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        FirebaseDatabase.getInstance().getReference("evote/users").orderByChild("aktif").equalTo("tidak aktif")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.childrenCount.toString()
                    findViewById<TextView>(R.id.validasi).setText(data)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun onClick(v: View) {
        val btnSemua = findViewById<RelativeLayout>(R.id.btn_semua)
        val btn1 = findViewById<RelativeLayout>(R.id.btn_kelas1)
        val btn2 = findViewById<RelativeLayout>(R.id.btn_kelas2)
        val btn3 = findViewById<RelativeLayout>(R.id.btn_kelas3)
        val btnvalidasi = findViewById<RelativeLayout>(R.id.btn_validasi)

        when (v.id) {
            R.id.btn_semua -> {
                fetchDataSiswa()
                btnSemua.setBackgroundResource(R.drawable.btn_angel_bulat_red)
                btn1.setBackgroundResource(R.drawable.btn_angel_bulat)
                btn2.setBackgroundResource(R.drawable.btn_angel_bulat)
                btn3.setBackgroundResource(R.drawable.btn_angel_bulat)
                btnvalidasi.setBackgroundResource(R.drawable.btn_angel_bulat)
            }
            R.id.btn_kelas1 -> {
                fetchDataBy("kelas", "kelas vii")
                btnSemua.setBackgroundResource(R.drawable.btn_angel_bulat)
                btn1.setBackgroundResource(R.drawable.btn_angel_bulat_red)
                btn2.setBackgroundResource(R.drawable.btn_angel_bulat)
                btn3.setBackgroundResource(R.drawable.btn_angel_bulat)
                btnvalidasi.setBackgroundResource(R.drawable.btn_angel_bulat)
            }

            R.id.btn_kelas2 -> {
                fetchDataBy("kelas", "kelas viii")
                btnSemua.setBackgroundResource(R.drawable.btn_angel_bulat)
                btn1.setBackgroundResource(R.drawable.btn_angel_bulat)
                btn2.setBackgroundResource(R.drawable.btn_angel_bulat_red)
                btn3.setBackgroundResource(R.drawable.btn_angel_bulat)
                btnvalidasi.setBackgroundResource(R.drawable.btn_angel_bulat)
            }
            R.id.btn_kelas3 -> {
                fetchDataBy("kelas", "kelas ix")
                btnSemua.setBackgroundResource(R.drawable.btn_angel_bulat)
                btn1.setBackgroundResource(R.drawable.btn_angel_bulat)
                btn2.setBackgroundResource(R.drawable.btn_angel_bulat)
                btn3.setBackgroundResource(R.drawable.btn_angel_bulat_red)
                btnvalidasi.setBackgroundResource(R.drawable.btn_angel_bulat)
            }

            else -> {
                fetchDataBy("aktif", "tidak aktif")
                btnSemua.setBackgroundResource(R.drawable.btn_angel_bulat)
                btn1.setBackgroundResource(R.drawable.btn_angel_bulat)
                btn2.setBackgroundResource(R.drawable.btn_angel_bulat)
                btn3.setBackgroundResource(R.drawable.btn_angel_bulat)
                btnvalidasi.setBackgroundResource(R.drawable.btn_angel_bulat_red)
            }
        }
    }

    private fun fetchDataSiswa() {
        val progressBar : ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        FirebaseDatabase.getInstance().getReference("evote/users").orderByChild("level").equalTo("siswa")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    adapter.clear()
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val data = it.getValue(DataUsers::class.java)
                            if(data != null){
                                adapter.add(SiswaDataItemSiswa(data, this@SiswaDataSiswaActivity))
                            }
                        }
                        val recyclerView = findViewById<RecyclerView>(R.id.recylerview)
                        recyclerView.adapter = adapter
                        progressBar.visibility = View.GONE
                    } else {
                        Toasty.info(this@SiswaDataSiswaActivity, "Data Kosong!", Toasty.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun fetchDataBy(path: String, by: String) {
        val pb = findViewById<ProgressBar>(R.id.progressBar)
        pb.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference("evote/users").orderByChild(path).equalTo(by)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    adapter.clear()
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val data = it.getValue(DataUsers::class.java)!!
                            adapter.add(SiswaDataItemSiswa(data,this@SiswaDataSiswaActivity))
                        }
                        pb.visibility = View.GONE
                        val recyclerView = findViewById<RecyclerView>(R.id.recylerview)
                        recyclerView.adapter = adapter
                        pb.visibility = View.GONE
                    } else {
                        pb.visibility = View.GONE
                        Toasty.info(this@SiswaDataSiswaActivity, "Data Kosong!", Toasty.LENGTH_SHORT).show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}


class SiswaDataItemSiswa(val data : DataUsers, val context: Context) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.list_siswa_data_siswa
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_nisn.text = data.nisn
        viewHolder.itemView.tv_nama.text = data.nama
        viewHolder.itemView.tv_kelas.text = data.kelas
    }
}
