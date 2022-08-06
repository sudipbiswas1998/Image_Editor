package com.sudip.imageeditor

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Gallery
import android.widget.ImageView
import android.widget.Toast
import com.github.dhaval2404.imagepicker.ImagePicker

class MainActivity : AppCompatActivity() {
    lateinit var imgCamera: ImageView
    lateinit var imgGallery: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imgCamera = findViewById(R.id.imgCamera)
        imgGallery = findViewById(R.id.imgGallery)

        imgGallery.setOnClickListener {
            ImagePicker.Companion.with(this).galleryOnly().start()
        }
        imgCamera.setOnClickListener {
            ImagePicker.Companion.with(this).cameraOnly().start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val imgUri: Uri? = data?.data
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("imgUri", imgUri.toString())
            startActivity(intent)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}


