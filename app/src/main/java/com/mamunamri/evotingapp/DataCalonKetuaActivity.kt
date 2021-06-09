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
import com.mamunamri.evotingapp.data.DataKetua
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.list_data_ketua.view.*


class DataCalonKetuaActivity : AppCompatActivity() {

    var adapter = GroupieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_calon_ketua)

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
                                adapter.add(DataItem(data, this@DataCalonKetuaActivity))
                            }
                        }
                        val recyclerView = findViewById<RecyclerView>(R.id.recylerview)
                        recyclerView.adapter = adapter
                    } else {
                        Toasty.info(this@DataCalonKetuaActivity, "Data Kosong!", Toasty.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}

class DataItem(val data : DataKetua, val context: Context) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.list_data_ketua
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_nisn.text = data.nisn
        viewHolder.itemView.tv_nama.text = data.nama
        viewHolder.itemView.tv_kelas.text = data.no_urut

        val img = viewHolder.itemView.findViewById<ImageView>(R.id.img_ketua)
        Glide.with(viewHolder.itemView.context)
            .load(data.fotouri)
            .into(img)

        viewHolder.itemView.btn_hapus.setOnClickListener {
            val builder = AlertDialog.Builder(context)
                .setMessage("hapus data ? ")
            builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                FirebaseDatabase.getInstance().getReference("evote/calon_ketua").child(data.nisn.toString()).removeValue()
                    .addOnCompleteListener {
                        FirebaseStorage.getInstance()
                            .getReference("/evote/calon_ketua/${data.foto}").delete()
                        Toasty.success(
                            context,
                            "data nisn ${data.nisn} terhapus",
                            Toasty.LENGTH_SHORT
                        ).show()

                    }
            })
            builder.setNegativeButton("Batal", DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
            })
            builder.create()
            builder.show()
        }

        viewHolder.itemView.btn_edit.setOnClickListener {
            val intent = Intent(context, TambahDataCalonKetuaActivity::class.java)
            intent.putExtra("edit", "edit")
            intent.putExtra("nisninten", data.nisn)
            context.startActivity(intent)
        }

    }
}