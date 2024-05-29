package es.upm.btb.helloworldkt

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.ListView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.room.Room
import es.upm.btb.helloworldkt.externalService.APImodel
import es.upm.btb.helloworldkt.externalService.ApiService
import es.upm.btb.helloworldkt.externalService.Model
import es.upm.btb.helloworldkt.externalService.ParkingAdapter
import es.upm.btb.helloworldkt.persistence.room.AppDatabase
import es.upm.btb.helloworldkt.persistence.room.LocationEntity
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Callback
import retrofit2.Response


class SecondActivity : AppCompatActivity(), LocationListener {
    private val TAG = "btaSecondActivity"

    private lateinit var listView: ListView
    private lateinit var database: AppDatabase
    private lateinit var adapter: ParkingAdapter
    private lateinit var latestLocation: LocationEntity
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_second)


        Log.d(TAG, "onCreate: The activity is being created.");

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navigation_map -> {
                    val intent = Intent(this, OpenStreetMapActivity::class.java)
                    startActivity(intent)
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



        listView = findViewById(R.id.lvFileContents)


        // Init adapter
        adapter = ParkingAdapter(this, mutableListOf())
        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position)
            val myItem: Model? = selectedItem as? Model
            if (myItem != null)
                fullList(adapter, myItem.id)
        }

        // Init database
        database =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "coordinates").build()

        val toolbar: Toolbar = findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar)
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
    }

    override fun onResume() {
        super.onResume()


        if (!::adapter.isInitialized) {
            adapter = ParkingAdapter(this, mutableListOf())
            listView.adapter = adapter
        }

        filterby(0,0.0 , 0.0)


    }


    private fun fetchParking(
        adapter: ParkingAdapter,
        distancia: Long,
        latitud: Double,
        longitud: Double
    ) {

        val baseUrl = "https://datos.madrid.es/egob/catalogo/"
        val fullURL =
            "https://datos.madrid.es/egob/catalogo/202625-0-aparcamientos-publicos.json?latitud=" + latitud + "&longitud=" + longitud + "&distancia=" + distancia
        val api: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
        val call = api.getParkings(fullURL)
        call.enqueue(object : Callback<APImodel> {
            override fun onResponse(call: Call<APImodel>, response: Response<APImodel>) {
                if (response.isSuccessful) {
                    val routeModel = response.body()
                    if (routeModel != null)
                        runOnUiThread {
                            adapter.updateData(routeModel.model1.toMutableList())
                            //}
                        }
                    routeModel?.model1?.forEach {
                        Log.d("SecondActivity", "ID: ${it.id}")
                    }
                } else {
                    Log.e("SecondActivity", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<APImodel>, t: Throwable) {
                Log.e("SecondActivity", "Error: ${t.message}")
            }
        })
    }

    private fun fullList(adapter: ParkingAdapter, url: String) {
        val baseUrl = "https://datos.madrid.es/egob/catalogo/"
        val api: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
        val call = api.getParkings(url)
        call.enqueue(object : Callback<APImodel> {
            override fun onResponse(call: Call<APImodel>, response: Response<APImodel>) {
                if (response.isSuccessful) {
                    val routeModel = response.body()
                    if (routeModel != null)
                        runOnUiThread {
                            if (!routeModel.model1.isEmpty())
                                callDetail(routeModel.model1[0])
                        }
                    routeModel?.model1?.forEach {
                        Log.d("SecondActivity", "ID: ${it.id}")
                    }
                } else {
                    Log.e("SecondActivity", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<APImodel>, t: Throwable) {
                Log.e("SecondActivity", "Error: ${t.message}")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_second, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                filterby(0, 0.0, 0.0)
                true
            }

            R.id.action_near -> {
                if (::latestLocation.isInitialized)
                    filterby(600, latestLocation.longitude, latestLocation.latitude)
                else Log.e("SecondActivity", "latestLocation no está inicializada")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun callDetail(item2: Model) {

        val intent = Intent(this, ThirdActivity::class.java)
        intent.putExtra("Nombre", item2.title)
        intent.putExtra("Direccion Calle", item2.address?.streetAddress)
        intent.putExtra("Direccion Locality", item2.address?.locality)
        intent.putExtra("Codigo Postal", item2.address?.postalCode)
        intent.putExtra("Precio", item2.price)
        intent.putExtra("Horario", item2.organization?.organizationDesc)
        intent.putExtra("Latitude", item2.location?.longitude.toString())
        intent.putExtra("Longitude", item2.location?.latitude.toString())
        this.startActivity(intent)

    }


    private fun filterby(distancia: Long, longitud: Double, latitud: Double) {
        val searchView = layoutInflater.inflate(R.layout.activity_search, null)
        AlertDialog.Builder(this)
            .setTitle("Parking lot search")
            .setView(searchView)
            .setPositiveButton("Ok") { dialog, which ->
                val longitudTextView: TextView = searchView.findViewById(R.id.longitudEditText)
                val latitudTextView: TextView = searchView.findViewById(R.id.latitudEditText)
                val distanciaTextView: TextView = searchView.findViewById(R.id.distanciaEditText)
                fetchParking(
                    adapter,
                    distanciaTextView.text.toString().toLong(),
                    latitudTextView.text.toString().toDouble(),
                    longitudTextView.text.toString().toDouble()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()

        val longitudTextView: TextView = searchView.findViewById(R.id.longitudEditText)
        longitudTextView.text = longitud.toString()
        val latitudTextView: TextView = searchView.findViewById(R.id.latitudEditText)
        latitudTextView.text = latitud.toString()
        val distanciaTextView: TextView = searchView.findViewById(R.id.distanciaEditText)
        distanciaTextView.text = distancia.toString()
    }

    override fun onLocationChanged(location: Location) {
        latestLocation = LocationEntity(
            latitude = location.latitude,
            longitude = location.longitude,
            timestamp = System.currentTimeMillis()
        )

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
}