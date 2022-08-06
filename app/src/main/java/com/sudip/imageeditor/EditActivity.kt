package com.sudip.imageeditor

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class EditActivity : AppCompatActivity() {
    lateinit var image: ImageView
    lateinit var imgCrop: ImageView
    lateinit var imgInfo: ImageView
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        var imgUri: Uri = Uri.parse(intent.getStringExtra("imgUri"))

        image = findViewById(R.id.imgView)
        imgCrop = findViewById(R.id.imgCrop)
        imgInfo = findViewById(R.id.imgInfo)


        image.setImageURI(imgUri)

        imgCrop.setOnClickListener {
            cropImage.launch(
                options(uri = imgUri) {
                    setScaleType(CropImageView.ScaleType.FIT_CENTER)
                    setCropShape(CropImageView.CropShape.RECTANGLE)
                    setGuidelines(CropImageView.Guidelines.ON)
                    setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                })
        }
        imgInfo.setOnClickListener {
            val intent = Intent(this, ExifActivity::class.java)
            intent.putExtra("imgUri", imgUri.toString())
            startActivity(intent)
        }
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            image.setImageURI(uriContent)
            if (uriContent != null) {
                galleryAddPic(uriContent)
            }
        } else {
            // an error occurred
            val exception = result.error
            Log.d("error", exception.toString())
        }
    }

    private fun galleryAddPic(uri: Uri) {
        //Converting uri to Bitmap
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        val filename = "${System.currentTimeMillis()}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            this?.contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
                Log.d("output", fos.toString())

            }
        } else {
            //These for devices running on android < Q
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)

        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "Photo saved to gallery", Toast.LENGTH_LONG).show()

            Log.d("msg", "Photo saved")
        }

    }
}


