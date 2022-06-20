package com.war_against_covid_19.safecampus

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import com.war_against_covid_19.safecampus.storagehelper.Storage
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity : AppCompatActivity() {
    var buildingname = arrayOf("Select Building","100 Antoinette","110 E. Warren Ave.","1200 Holden","3750 Woodward","50 East Canfield","5035 Woodward",
        "5057 Woodward","5415 Cass","5425 Woodward","5435 Woodward","5743 Woodward","5957 Woodward","5959 Woodward","60 W. Hancock St",
        "6000 Cass","77 W. Canfield","87 East Canfield","Academic/Administrative","Advanced Technology Education Center - Educational Outreach",
        "Alumni House","Anthony Wayne Drive Apartments","Applebaum Building","Art Building","Barbara Ann Karmanos Cancer Institute","Beaumont Dearborn",
        "Beecher House","Belcrest Apartments","Bioengineering","Biological Sciences","Bonstelle Theatre","Business, Mike Ilitch School of",
        "C. S. Mott Center for Human Growth and Development","Campus Health Center (CHC)","Chatsworth Suites","Chemistry","Children's Center",
        "Children's Hospital of Michigan","Cohn","Community Arts","Computing Services Center","Corporate","DeRoy Auditorium","Education, College of",
        "Elliman Building","Engineering Technology - Engineering","Engineering, College of","Facilities Planning & Management (FPM)","Faculty/Administration",
        "Freer House","General Lectures","Harper Hospital Professional Building","Harper University Hospital","Harwell Field","Hilberry Theatre","Hutzel Hospital",
        "Industry Innovation Center","Integrative Biosciences Center","Jacob House","Knapp Building","Kresge Eye Institute","Kresge Taylor Clinic","Lande Building",
        "Law Classroom","Law Library, Arthur Neef","Law School Building","Leon H. Atchison Residence Hall","Life Science","Linsell House","Mackenzie House",
        "Macomb Education Center - Educational Outreach","Manoogian Hall","Manufacturing Engineering","Matthaei Physical Education Center","Mazurek Medical Education Commons",
        "McGregor Memorial Conference Center","Mort Harris Recreation and Fitness Center","Mortuary Science","Multipurpose Indoor Facility","Old Main","Parking Structure 1",
        "Parking Structure 2","Parking Structure 3","Parking Structure 4","Parking Structure 5","Parking Structure 6","Parking Structure 7","Parking Structure 8",
        "Physics","Police Department (Public Safety)","Prentis Building","Purdy/Kresge Library","Rackham","Rands House","Receiving Hospital",
        "Rehabilitation Institute","Schaver","Schoolcraft Center - Educational Outreach","Science Hall","Scott Hall","Shapero Hall","Simons Building","Skillman","Social Work, School of",
        "Softball Stadium","St. Andrew's","Stadium Auxiliary","State Hall","STEM Innovation Learning Center (SILC)","Student Center","Studio One Apartments","Tech One Building",
        "Theatre Production Ctr","Thompson Home","Tierney Alumni House","Tolan Park Medical Building","Tom Adams Field","Towers Residential Suites",
        "Undergraduate Library, David Adamany","University Health Center","University Tower","VA Medical Center","Walter P. Reuther Library, Archives of Labor and Urban Affairs",
        "Welcome Center","Woodward Gardens","WSU Bookstore","WSUPG Clinic - Livonia","WSUPG Clinic - Monroe","WSUPG Clinic - Southfield","WSUPG Clinic - Troy",
        "Yousif B. Ghafari Hall")
    var selected_position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //checkCameraPermission()
        val text_email=findViewById<EditText>(R.id.text_email)
        val text_coursename=findViewById<EditText>(R.id.text_coursename)
        val button_login = findViewById<Button>(R.id.loginbutton)

        if (Storage.buildingname==null){
            Storage.buildingname="5057 Woodward"
        }
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        text_email.setText(sharedPref.getString("email","Email"))
        text_coursename.setText(sharedPref.getString("coursename","Event Name"))
        /*
        text_email.set*/
        var aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, buildingname)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(spinner)
        {
            adapter = aa
            setSelection(0, false)
            //onItemSelectedListener = this@MainActivity
            prompt = "Select the building"
            gravity = Gravity.CENTER
            onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    //Toast.makeText(context,"选中:${buildingname[position]}",Toast.LENGTH_LONG).show()
                    Storage.buildingname=buildingname[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }
        }
        //aa.set


        button_login.setOnClickListener {

            var i : Intent = Intent(this@MainActivity, FunctionChoose::class.java)
            //i.putExtra("address",)
            //Storage.buildingname=
                ""
            Storage.email=text_email.text.toString()//""
            //Toast.makeText(this, Storage.email, Toast.LENGTH_LONG).show()

            Storage.coursename=text_coursename.text.toString()//""
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString("email",text_email.text.toString())
                putString("coursename",text_coursename.text.toString())
                putString("buildingname",Storage.buildingname)
                apply()
            }

            startActivity(i)

        }


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkCameraPermission() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(Manifest.permission.CAMERA)
            requestPermissions(permissions,1)
            /*Intent().also {
                it.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                it.data = Uri.fromParts("package", packageName, null)
                startActivity(it)
                finish()
            }*/

        }
    }
}