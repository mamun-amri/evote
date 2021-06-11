package com.mamunamri.evotingapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.mamunamri.evotingapp.data.DataChart
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.list_data_siswa.view.*
import kotlinx.android.synthetic.main.list_data_siswa.view.tv_kelas
import kotlinx.android.synthetic.main.list_data_siswa.view.tv_nama
import kotlinx.android.synthetic.main.list_data_siswa.view.tv_nisn
import kotlinx.android.synthetic.main.list_data_suara.view.*

class DataSuaraActivity : AppCompatActivity() {
    var adapter = GroupieAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_suara)
        try {
            supportActionBar?.hide()
            fetchData()
        }catch (e:Exception){

        }
    }

    private fun fetchData() {
        val progressBar : ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        FirebaseDatabase.getInstance().getReference("evote/chart").orderByChild("suara")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    adapter.clear()
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val data = it.getValue(DataChart::class.java)
                            if(data != null){
                                adapter.add(DataItemSuara(data, this@DataSuaraActivity))
                            }
                        }
                        val recyclerView = findViewById<RecyclerView>(R.id.recylerview)
                        recyclerView.adapter = adapter
                        progressBar.visibility = View.GONE
                    } else {
                        Toasty.info(this@DataSuaraActivity, "Data Kosong!", Toasty.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}

class DataItemSuara(val data : DataChart, val context: Context) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.list_data_suara
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_nisn.text = data.nisn
        viewHolder.itemView.tv_nama.text = data.nama
        viewHolder.itemView.tv_kelas.text = data.no_urut
        viewHolder.itemView.tv_suara.text = data.suara.toString()
    }
}