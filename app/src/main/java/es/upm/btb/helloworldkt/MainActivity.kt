package es.upm.btb.helloworldkt

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.ActivityCompat
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import java.io.File
import com.google.android.material.bottomnavigation.BottomNavigationView


import es.upm.btb.helloworldkt.persistence.room.AppDatabase
import es.upm.btb.helloworldkt.persistence.room.LocationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.room.Room
import androidx.lifecycle.lifecycleScope

import com.firebase.ui.auth.AuthUI
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(), LocationListener {

    private val TAG = "btaMainActivity"
    private lateinit var locationManager: LocationManager
    var latestLocation: Location? = null
    private val locationPermissionCode = 2
    lateinit var database: AppDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      //  FirebaseApp.initializeApp(this)
/*
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {

 */
            Log.d(TAG, "onCreate: The activity is being created.")
            println("Hello world to test System.out standar output!")

            val navView: BottomNavigationView = findViewById(R.id.nav_view)
            navView.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.navigation_map -> {
                        if (latestLocation != null) {
                            val intent = Intent(this, OpenStreetMapActivity::class.java)
                            val bundle = Bundle()
                            bundle.putParcelable("location", latestLocation)
                            intent.putExtra("locationBundle", bundle)
                            startActivity(intent)
                        } else {
                            Log.e(TAG, "Location not set yet.")
                        }
                        true
                    }

                    R.id.navigation_list -> {
                        val intent = Intent(this, SecondActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)

            val userIdentifier = getUserIdentifier()
            if (userIdentifier == null) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "User ID: $userIdentifier", Toast.LENGTH_LONG).show()
            }

            val buttonUbi: Button = findViewById(R.id.Ubicaciones)
            buttonUbi.setOnClickListener {
                val intent = Intent(this, FourthActivity::class.java)
                startActivity(intent)
            }

            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    locationPermissionCode
                )
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
                Log.d(TAG, "ok requestLocationUpdates")
            }

            database =
                Room.databaseBuilder(applicationContext, AppDatabase::class.java, "coordinates")
                    .build()

        //}
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        5000,
                        5f,
                        this
                    )
                    Log.d(TAG, "ok requestLocationUpdates")
                }
            }
        }
    }


    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}



    private fun getUserIdentifier(): String? {
        val sharedPreferences = this.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userIdentifier", null)
    }


    private fun logout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        val sharedPreferences = this.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("userIdentifier")
        editor.apply()
        Toast.makeText(this, "User ID forgotten", Toast.LENGTH_SHORT).show()
    }




    override fun onLocationChanged(location: Location) {
        latestLocation = location
        val textView: TextView = findViewById(R.id.mainTextView)
        Toast.makeText(
            this,
            "Coordinates update! [${location.latitude}][${location.longitude}]",
            Toast.LENGTH_LONG
        ).show()

        val newLocation = LocationEntity(
            latitude = location.latitude,
            longitude = location.longitude,
            timestamp = System.currentTimeMillis()
        )
        lifecycleScope.launch(Dispatchers.IO) {
            database.locationDao().insertLocation(newLocation)
        }

        textView.text =
            "Latitude: [${location.latitude}], Longitude: [${location.longitude}], UserId: [${getUserIdentifier()}]"
        saveCoordinatesToFile(location.latitude, location.longitude)


    }


    private fun saveCoordinatesToFile(latitude: Double, longitude: Double) {
        val fileName = "location.csv"
        val file = File(filesDir, fileName)
        val timestamp = System.currentTimeMillis()
        file.appendText("hola$timestamp;$latitude;$longitude")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_logout -> {
                AlertDialog.Builder(this)
                    .setTitle("Confirm to logout")
                    .setMessage("Are you sure you want logout?")
                    .setPositiveButton("Yes") { dialog, which ->

                        logout()
                    }
                    .setNegativeButton("No", null)
                    .show()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }


    }


}