package com.war_against_covid_19.safecampus

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.war_against_covid_19.safecampus.dbhelper.RegistrationHelper
import com.war_against_covid_19.safecampus.dbhelper.TrackingDB
import com.war_against_covid_19.safecampus.storagehelper.Storage
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class QrCode : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    //val mediaPlayer = MediaPlayer.create(this, R.raw.beep)
    val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)
    fun beep(duration: Int)
    {
        toneG.startTone(ToneGenerator.TONE_DTMF_S, duration)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            toneG.release()
        }, (duration + 50).toLong())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val facemask_required=Storage.facemask
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        val dbHelper = RegistrationHelper(this)

        val db = dbHelper.writableDatabase

        codeScanner = CodeScanner(this, scannerView)


        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        //webaccess()

        codeScanner.decodeCallback = DecodeCallback {

            kotlin.concurrent.thread{
                val url = it.text
                val res = try { java.net.URL(url).readText() } catch (ex: Exception) { return@thread }
               // runOnUiThread { println("res: $res") }
                val tg = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)


                    if ((res.length>25000)and (res.indexOf(Storage.buildingname)!=res.lastIndexOf(Storage.buildingname))) {
                        //mediaPlayer.start()
                        tg.startTone(ToneGenerator.TONE_PROP_BEEP)


                        runOnUiThread {
                            //beep(100)
                            Toast.makeText(
                                this,
                                "Scan result:Allowed:${Storage.buildingname}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        val values = ContentValues().apply {
                            put(
                                TrackingDB.FeedEntry.COLUMN_NAME_ACCESS_ID,
                                it.text.substringAfter('=', "error")
                            )
                            put(TrackingDB.FeedEntry.COLUMN_NAME_QR_CONDITION, "allowed")
                        }
                        val newRowId = db?.insert(TrackingDB.FeedEntry.TABLE_NAME, null, values)
                        if(facemask_required) {
                            var i: Intent = Intent(this@QrCode, Facemask::class.java)
                            //i.putExtra("address",)
                            startActivity(i)
                        }
                    }
                    else{
                        runOnUiThread {
                            Toast.makeText(
                                this,
                                "Scan result:Not Allowed: ${Storage.buildingname}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    //Toast.makeText(this, "Scan result: ${res}", Toast.LENGTH_LONG).show()

            }
          //  runOnUiThread {


            //    Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
           // }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }



    }
    fun webaccess(){
        val doc: Document = Jsoup.connect("www.baidu.com").get()
    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}