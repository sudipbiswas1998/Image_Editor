package com.sudip.imageeditor

import android.annotation.SuppressLint
import android.media.ExifInterface.TAG_APERTURE
import android.media.ExifInterface.TAG_ISO
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import org.w3c.dom.Text
import java.io.File
import java.io.IOException

class ExifActivity : AppCompatActivity() {
    lateinit var imgThumbnail: ImageView
    lateinit var txtFileLocation: TextView
    lateinit var txtFNumber: TextView
    lateinit var txtDateTime: TextView
    lateinit var txtFocalLength: TextView
    lateinit var txtExposure: TextView
    lateinit var txtIso: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exif)

        imgThumbnail = findViewById(R.id.imgThumbnail)
        txtFileLocation = findViewById(R.id.txtFileLocation)
        txtFNumber = findViewById(R.id.txtFNumber)
        txtDateTime = findViewById(R.id.txtDateTime)
        txtFocalLength = findViewById(R.id.txtFocalLength)
        txtExposure = findViewById(R.id.txtExposure)
        txtIso = findViewById(R.id.txtIso)

        var imgUri: Uri = Uri.parse(intent.getStringExtra("imgUri"))
        imgThumbnail.setImageURI(imgUri)

        var inputStream = contentResolver.openInputStream(imgUri)

        var file = File(imgUri.path)
        Log.d("file", file.toString())

        try {
            val exif = inputStream?.let { ExifInterface(it) }
            txtFileLocation.text = "File location: $file"
            txtDateTime.text =
                "Date & Time: " + exif?.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED)
                    .toString()
            txtFNumber.text =
                "FNumber: " + exif?.getAttribute(ExifInterface.TAG_F_NUMBER).toString()
            txtFocalLength.text =
                "Focal Length: " + exif?.getAttribute(ExifInterface.TAG_FOCAL_LENGTH).toString()
            txtExposure.text =
                "Exposure: " + exif?.getAttribute(ExifInterface.TAG_EXPOSURE_TIME).toString()
            txtIso.text =
                "ISO: " + exif?.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS).toString()
        } catch (e: IOException) {
            Log.d("error", e.toString())
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (ignored: IOException) {

                }
            }
        }
    }
}


