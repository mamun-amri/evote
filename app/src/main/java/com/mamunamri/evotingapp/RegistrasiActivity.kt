package com.mamunamri.evotingapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mamunamri.evotingapp.data.DataUsers
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*


@DelicateCoroutinesApi
class RegistrasiActivity : AppCompatActivity(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {

    lateinit var spJk: Spinner
    lateinit var spKelas: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)

        try {
            supportActionBar?.hide()

            val pb = findViewById<ProgressBar>(R.id.progressBar)
            pb.visibility = View.GONE
            findViewById<Button>(R.id.btn_kembali).setOnClickListener(this)
            findViewById<Button>(R.id.btn_daftar).setOnClickListener(this)
            spKelas = findViewById(R.id.et_kelas)
            spJk = findViewById(R.id.et_jk)

            spKelas.onItemSelectedListener = this
            spKelas.adapter = ArrayAdapter<Any?>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                resources.getStringArray(R.array.list_kelas)
            )

            spJk.onItemSelectedListener = this
            spJk.adapter = ArrayAdapter<Any?>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                resources.getStringArray(R.array.list_jk)
            )

        }catch (e:Exception){

        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_kembali -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.btn_daftar ->{
                signUp()
            }
        }
    }

    private fun signUp() {
        val et_nisn = findViewById<EditText>(R.id.et_nisn).text.toString()
        val et_nama = findViewById<EditText>(R.id.et_nama).text.toString()
        val et_no_telp = findViewById<EditText>(R.id.et_no_telp).text.toString()
        val et_password = findViewById<EditText>(R.id.et_password).text.toString()
        val kelas = spKelas.selectedItem.toString()
        val jk = spJk.selectedItem.toString()
        if (et_nisn != "" && et_nama != "" && et_no_telp != "") {
            if (et_password != "") {
                val pb = findViewById<ProgressBar>(R.id.progressBar)
                pb.visibility = View.VISIBLE

                FirebaseDatabase.getInstance().getReference("evote/users")
                    .child(et_nisn).addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()){
                                val hash = BCrypt.withDefaults().hashToString(12, et_password.toCharArray())
                                val data = DataUsers("tidak aktif",jk,kelas,"siswa",et_nama, et_nisn,et_no_telp,hash)
                                FirebaseDatabase.getInstance().getReference("evote/users").child(et_nisn).setValue(data)
                                    .addOnSuccessListener {

                                        pb.visibility = View.GONE
                                        GlobalScope.launch(context = Dispatchers.Main) {
                                            Toasty.success(
                                                this@RegistrasiActivity,
                                                "Daftar Berhasil!",
                                                Toasty.LENGTH_SHORT
                                            ).show()
                                            delay(2000)
                                            val intent = Intent(baseContext,
                                                LoginActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            startActivity(intent)
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toasty.error(
                                            this@RegistrasiActivity,
                                            "Daftar Gagal!",
                                            Toasty.LENGTH_SHORT
                                        ).show()
                                        pb.visibility = View.GONE
                                    }
                            }else{
                                Toasty.warning(
                                   this@RegistrasiActivity,
                                    "Nisn Sudah Terdaftar!",
                                    Toasty.LENGTH_SHORT
                                ).show()
                                pb.visibility = View.GONE
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
            }
        }else{
            Toasty.warning(this, "field harus diisi", Toasty.LENGTH_SHORT).show()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        parent?.getItemAtPosition(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}