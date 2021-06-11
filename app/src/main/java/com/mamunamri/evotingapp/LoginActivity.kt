package com.mamunamri.evotingapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.DelicateCoroutinesApi


@DelicateCoroutinesApi
class LoginActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val supportActionBar = supportActionBar
        supportActionBar?.hide()

       try {
           val progressBar : ProgressBar = findViewById(R.id.progressBar)
           progressBar.visibility = View.GONE
       }catch (e:Exception){

       }
        findViewById<Button>(R.id.btn_regist).setOnClickListener(this)
        findViewById<Button>(R.id.btn_masuk).setOnClickListener(this)
        findViewById<CheckBox>(R.id.btn_login_show).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_masuk -> {
                signIn()
                return
            }
            R.id.btn_regist -> {
                startActivity(Intent(this, RegistrasiActivity::class.java))
                return
            }

            R.id.btn_login_show -> {
                showPassword()
            }
            else -> return
        }
    }

    private fun signIn() {
        val progressBar : ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val nisn: EditText = findViewById(R.id.et_nisn)
        val sandi: EditText = findViewById(R.id.et_sandi)
                if (nisn.text.toString().trim() == "" || sandi.text.toString().trim() == "") {
                    Toasty.warning(baseContext, "Field Kosong!", Toasty.LENGTH_SHORT).show()
                } else {
                    FirebaseDatabase.getInstance().getReference("/evote/users/${nisn.text.toString().trim()}").addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val child = snapshot.child("password")
                                val hash = child.value.toString()
                                val verifyer = BCrypt.verifyer()
                                    val charArray = sandi.text.toString().toCharArray()

                                    if (verifyer.verify(charArray, hash).verified) {
                                        val child2 = snapshot.child("aktif")
                                        val aktif = child2.value.toString()
                                        val child3 = snapshot.child("level")

                                        val level = child3.value.toString()
                                        when {
                                            aktif != "aktif" -> {
                                                Toasty.info(
                                                    this@LoginActivity,
                                                    "Aktivasi kepada Admin",
                                                    Toasty.LENGTH_SHORT
                                                ).show()
                                                progressBar.visibility = View.GONE
                                            }
                                            level == "admin" -> {
                                                val session = setSession(this@LoginActivity)
                                                session.setUserString(setSession.KEY_NISN, nisn.text.toString().trim())
                                                session.setUserBoolean(setSession.KEY_LOGIN, true)
                                                session.setUserString(setSession.KEY_LEVEL, level)
                                                progressBar.visibility = View.GONE
                                                val intent = Intent(this@LoginActivity, HomepageAdmin::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                startActivity(intent)
                                            }
                                            level == "siswa" -> {
                                                val session = setSession(this@LoginActivity)
                                                session.setUserString(setSession.KEY_NISN, nisn.text.toString().trim())
                                                session.setUserBoolean(setSession.KEY_LOGIN, true)
                                                session.setUserString(setSession.KEY_LEVEL, level)
                                                progressBar.visibility = View.GONE
                                                val intent = Intent(this@LoginActivity, HomepageSiswa::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                startActivity(intent)
                                            }
                                        }
                                    } else {
                                        progressBar.visibility = View.GONE
                                        Toasty.error(
                                            this@LoginActivity,
                                            "Sandi Salah",
                                            Toasty.LENGTH_SHORT
                                        ).show()
                                    }

                            } else {
                                progressBar.visibility = View.GONE
                                Toasty.error(
                                    this@LoginActivity,
                                    "Nisn Tidak Terdaftar",
                                    Toasty.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
    }

    private fun showPassword() {
        if (findViewById<CheckBox>(R.id.btn_login_show).isChecked) {
            val findViewById2 = findViewById<EditText>(R.id.et_sandi)
            findViewById2.inputType = 1
            return
        }else{
            val findViewById2 = findViewById<EditText>(R.id.et_sandi)
            findViewById2.inputType = 129
        }
    }

    /* access modifiers changed from: protected */
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    override fun onStart() {
        super.onStart()
        val session = setSession(this)
        val islogin = session.getUserBoolean(setSession.KEY_LOGIN)
        val level = session.getUserString(setSession.KEY_LEVEL)
        if (!islogin) {
            return
        }
        if (level == "admin") {
            startActivity(Intent(this, HomepageAdmin::class.java))
            finish()
            return
        }else{
            startActivity(Intent(this, HomepageSiswa::class.java))
            finish()
        }
    }
}