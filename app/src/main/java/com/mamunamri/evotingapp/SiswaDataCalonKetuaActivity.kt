package com.mamunamri.evotingapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.mamunamri.evotingapp.data.DataChart
import com.mamunamri.evotingapp.data.DataKetua
import com.mamunamri.evotingapp.data.DataSuara
import com.mamunamri.evotingapp.setSession.Companion.KEY_NISN
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.list_data_ketua.view.*
import kotlinx.android.synthetic.main.list_data_ketua.view.tv_kelas
import kotlinx.android.synthetic.main.list_data_ketua.view.tv_nama
import kotlinx.android.synthetic.main.list_data_ketua.view.tv_nisn
import kotlinx.android.synthetic.main.list_pemilihan_ketua.view.*


class SiswaDataCalonKetuaActivity : AppCompatActivity() {

    var adapter = GroupieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.siswa_activity_data_calon_ketua)

        try {
            supportActionBar?.hide()
            val progressBar : ProgressBar = findViewById(R.id.progressBar)
            progressBar.visibility = View.GONE
            fetchData()
            findViewById<TextView>(R.id.btn_tambah).setOnClickListener{
                startActivity(Intent(this,TambahDataCalonKetuaActivity::class.java))
                finish()
            }
        }catch (e : Exception){

        }
    }

    private fun fetchData() {
        val progressBar : ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        FirebaseDatabase.getInstance().getReference("evote/calon_ketua")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val data = it.getValue(DataKetua::class.java)
                            if(data != null){
                                adapter.add(SiswaDataItem(data, this@SiswaDataCalonKetuaActivity))
                            }
                        }
                        val recyclerView = findViewById<RecyclerView>(R.id.recylerview)
                        recyclerView.adapter = adapter
                    } else {
                        Toasty.info(this@SiswaDataCalonKetuaActivity, "Data Kosong!", Toasty.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}

class SiswaDataItem(val data : DataKetua, val context: Context) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.list_pemilihan_ketua
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_nisn.text = data.nisn
        viewHolder.itemView.tv_nama.text = data.nama
        viewHolder.itemView.tv_kelas.text = data.no_urut

        val img = viewHolder.itemView.findViewById<ImageView>(R.id.img_ketua)
        Glide.with(viewHolder.itemView.context)
            .load(data.fotouri)
            .into(img)

        val session = setSession(context)
        val id = session.getUserString(KEY_NISN)
        viewHolder.itemView.btn_coblos.setOnClickListener {
            val builder = AlertDialog.Builder(context)
                .setMessage("yakin pilih ? ")
            builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                val datas = DataSuara(data.nisn,id,data.no_urut)
                FirebaseDatabase.getInstance().getReference("evote/suara").child(data.nisn.toString()).setValue(datas)
                    .addOnSuccessListener {
                        FirebaseDatabase.getInstance().getReference("evote/chart").child(data.nisn.toString())
                            .addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()){
                                        val count = snapshot.childrenCount.toInt()
                                        val dataChart = DataChart(data.nama,data.nisn,data.no_urut,count)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                    }
            })
            builder.setNegativeButton("Batal", DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
            })
            builder.create()
            builder.show()
        }

        viewHolder.itemView.btn_detail.setOnClickListener {
            val intent = Intent(context, DetailCalonKetuaActivity::class.java)
            intent.putExtra("nisninten", data.nisn)
            context.startActivity(intent)
        }

    }
}