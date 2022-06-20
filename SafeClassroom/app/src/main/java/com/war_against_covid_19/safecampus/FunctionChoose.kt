package com.war_against_covid_19.safecampus

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.war_against_covid_19.safecampus.dbhelper.RegistrationHelper
import com.war_against_covid_19.safecampus.dbhelper.TrackingDB
import com.war_against_covid_19.safecampus.storagehelper.Storage
import java.time.LocalDate
import java.util.*


class FunctionChoose : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_function_choose2)
        val button_qr = findViewById<Button>(R.id.button_qrcode)
       button_qr.setOnClickListener {

            var i : Intent = Intent(this@FunctionChoose, QrCode::class.java)
            //i.putExtra("address",)
            startActivity(i)

        }
       val button_facemask = findViewById<Button>(R.id.button_facemask)
        button_facemask.setOnClickListener {

            var i : Intent = Intent(this@FunctionChoose, Facemask::class.java)
            //i.putExtra("address",)
            startActivity(i)

        }
        val dbHelper = RegistrationHelper(this)

        val db = dbHelper.writableDatabase
        val button_clearstorage = findViewById<Button>(R.id.button_clearstorage)
        button_clearstorage.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Warning")
            builder.setMessage("Sure to remove local data?")
            builder.setPositiveButton("yes") { dialog, which ->  dbHelper.clear(db) }
            builder.setNegativeButton("not now") { dialog, which ->  }
            val alert = builder.create()
            alert.show()
            //dbHelper.clear(db)
        }
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val button_email = findViewById<Button>(R.id.button_email)
        button_email.setOnClickListener {

           // var i : Intent = Intent(this@FunctionChoose, Facemask::class.java)
            //i.putExtra("address",)
           // startActivity(i)
            val dbHelper = RegistrationHelper(this)
            val db = dbHelper.writableDatabase
           /* val values = ContentValues().apply {
                put(TrackingDB.FeedEntry.COLUMN_NAME_ACCESS_ID, "gx5812")
                put(TrackingDB.FeedEntry.COLUMN_NAME_QR_CONDITION, "allowed")
            }
            val newRowId = db?.insert(TrackingDB.FeedEntry.TABLE_NAME, null, values)

*/


            val cursor = db.query(
                TrackingDB.FeedEntry.TABLE_NAME,   // The table to query
                null,null,null,null,null,null)
            val itemIds = mutableListOf<String>()
            with(cursor) {
                while (moveToNext()) {
                    val itemId = getString(getColumnIndexOrThrow(TrackingDB.FeedEntry.COLUMN_NAME_ACCESS_ID))
                    itemIds.add(itemId)
                }
            }
            //Toast.makeText(this, "result of qr: ${itemIds.toString()}", Toast.LENGTH_LONG).show()

            cursor.close()
            //val email_address=findViewById<EditText>(R.id.text_email)
          //  Toast.makeText(this, "result of email: ${Storage.email}", Toast.LENGTH_LONG).show()
            sendEmail(Storage.email,"Attendee List of ${Storage.coursename.toString()} at ${LocalDate.now().toString()}",itemIds.toString())



        }
        val switch_facemask=findViewById<Switch>(R.id.switch_facemask)

        switch_facemask.isChecked=Storage.facemask
        if(Storage.facemask) {
            button_qr.isVisible=false

        }
        else {
            button_facemask.isVisible=false
        }


        switch_facemask.setOnCheckedChangeListener { compoundButton, isChecked ->

            if(isChecked)
            {
                Storage.facemask=true

                    button_facemask.isVisible=true
                button_qr.isVisible=false

            }
            else
            {
                Storage.facemask=false

                    button_facemask.isVisible = false
                button_qr.isVisible=true

            }
        }
        val button_back=findViewById<Button>(R.id.button_backtomain)
        button_back.setOnClickListener{
            var i : Intent = Intent(this@FunctionChoose, MainActivity::class.java)
            //i.putExtra("address",)
            startActivity(i)
        }




    }
    private fun sendEmail(recipient: String, subject: String, message: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }
}