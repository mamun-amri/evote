package com.mamunamri.evotingapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.mamunamri.evotingapp.data.DataKetua
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*


class TambahDataCalonKetuaActivity : AppCompatActivity(), View.OnClickListener {

    private var nisnInten = ""
    private var photoNameLama = ""
    private var photoUri: Uri? = null
    private var photoUriLama = ""
    private val requesPhotoCode = 101

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_data_calon_ketua)

        try {
            supportActionBar?.hide()
            findViewById<ImageButton>(R.id.img_ketua).setOnClickListener(this)
            findViewById<Button>(R.id.simpan).setOnClickListener(this)
            findViewById<Button>(R.id.btn_kembali).setOnClickListener(this)
        }catch (e:Exception){

        }

        nisnInten = intent.getStringExtra("nisninten").toString()
        try {
            if (intent.getStringExtra("edit").equals("edit")) {
                val nisn = findViewById<EditText>(R.id.et_nisn)
                val nama = findViewById<EditText>(R.id.et_nama)
                val noUrut = findViewById<EditText>(R.id.et_no_urut)
                val visi = findViewById<EditText>(R.id.et_visi)
                val misi = findViewById<EditText>(R.id.et_misi)
                val prestasi = findViewById<EditText>(R.id.et_prestasi)
                val periode = findViewById<EditText>(R.id.et_periode)
                nisn.isEnabled = false
                    FirebaseDatabase.getInstance().getReference("evote/calon_ketua/" + nisnInten)
                        .addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val data = snapshot.getValue(DataKetua::class.java)!!
                                    nisn.setText(data.nisn)
                                    nama.setText(data.nama)
                                    noUrut.setText(data.no_urut)
                                    visi.setText(data.visi)
                                    misi.setText(data.misi)
                                    prestasi.setText(data.prestasi)
                                    periode.setText(data.periode)
                                    photoUriLama = data.fotouri.toString()
                                    photoNameLama = data.foto.toString()
                                    val img = findViewById<ImageButton>(R.id.img_ketua)
                                    Glide.with(this@TambahDataCalonKetuaActivity)
                                        .load(data.fotouri)
                                        .into(img)
                                }else{
                                    Toasty.info(this@TambahDataCalonKetuaActivity, "Data Tidak ditemukan!", Toasty.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })
            } else {
                val findViewById = findViewById<View>(R.id.et_nisn)
                (findViewById as EditText).isEnabled = true
            }
        } catch (e: java.lang.Exception) {
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.img_ketua -> {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_PICK
                startActivityForResult(intent,requesPhotoCode)
            }
            R.id.btn_kembali -> {
                onBackPressed()
            }

            R.id.simpan ->{
                val nisn = findViewById<EditText>(R.id.et_nisn).text.toString()
                val nama = findViewById<EditText>(R.id.et_nama).text.toString()
                val noUrut = findViewById<EditText>(R.id.et_no_urut).text.toString()
                val visi = findViewById<EditText>(R.id.et_visi).text.toString()
                val misi = findViewById<EditText>(R.id.et_misi).text.toString()
                val prestasi = findViewById<EditText>(R.id.et_prestasi).text.toString()
                if (nisn == "" || nama == "" || noUrut == "" || visi == "" || misi == "" || prestasi == "") {
                    Toasty.warning(this, "Field Kosog!", Toasty.LENGTH_SHORT).show()
                } else if (photoUri != null) {
//                    Toasty.warning(this, "Uri != Kosog!", Toasty.LENGTH_SHORT).show()
                    do_upload()
                } else {
//                    Toasty.warning(this, "uri null", Toasty.LENGTH_SHORT).show()
                    do_upload()
                }
            }
        }
    }

    private fun do_upload() {
        val nama = findViewById<EditText>(R.id.et_nama).text.toString()
        val sdf = SimpleDateFormat("Mdy", Locale("IN","ID"))
        val date = sdf.format(Date()).toString()
        val fileName: String = nama+date
        val imageRef = FirebaseStorage.getInstance().getReference("/evote/calon_ketua/$fileName")
        val progress = ProgressDialog(this)
        progress.setTitle("Upload Data...")
        progress.show()
        if(photoUri != null){
//            Toasty.warning(this, "uri baru", Toasty.LENGTH_SHORT).show()
            imageRef.putFile(photoUri!!)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener {
                        val url = it.toString()
                        progress.dismiss()
                        insertDatabase(url, fileName)
                    }
                }
                .addOnProgressListener {
                    val float = 100.0F * it.bytesTransferred / it.totalByteCount
                    progress.setMessage("proses ${String.format("%.2f",float)}%")
                }
        }else{
//            Toasty.warning(this, "uri lama = $photoUriLama", Toasty.LENGTH_SHORT).show()
            insertDatabase(photoUriLama, photoNameLama)
        }
    }

    fun insertDatabase(url: String?, foto: String?) {
        val nisn = findViewById<EditText>(R.id.et_nisn).text.toString()
        val nama = findViewById<EditText>(R.id.et_nama).text.toString()
        val noUrut = findViewById<EditText>(R.id.et_no_urut).text.toString()
        val visi = findViewById<EditText>(R.id.et_visi).text.toString()
        val misi = findViewById<EditText>(R.id.et_misi).text.toString()
        val prestasi = findViewById<EditText>(R.id.et_prestasi).text.toString()
        val periode = findViewById<EditText>(R.id.et_periode).text.toString()
        val data = DataKetua(foto,url,misi,nama,nisn,noUrut,periode,prestasi,visi)

//        Toasty.warning(this, "data lama = $data", Toasty.LENGTH_LONG).show()

        FirebaseDatabase.getInstance().getReference("evote/calon_ketua/").child(nisn).setValue(data)
            .addOnSuccessListener {
                if (photoUri == null) {
                    intent.removeExtra("nisninten")
                    Toasty.success(this, "Update Data Berhasil", Toasty.LENGTH_SHORT)
                        .show()
                } else {
                    Toasty.success(this, "Tambah Data Berhasil", Toasty.LENGTH_SHORT)
                        .show()
                }
                onBackPressed()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == requesPhotoCode && resultCode == Activity.RESULT_OK && data != null){
            photoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,photoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            findViewById<ImageButton>(R.id.img_ketua).setBackgroundDrawable(bitmapDrawable)
        }
    }

    override fun onStart() {
        super.onStart()
        intent.removeExtra("editKetua")
    }
}