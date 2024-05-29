package es.upm.btb.helloworldkt

import android.content.Intent
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.gson.GsonBuilder
import es.upm.btb.helloworldkt.persistence.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import es.upm.btb.helloworldkt.externalService.ApiService
import es.upm.btb.helloworldkt.externalService.ParkingAdapter


class ThirdActivity : AppCompatActivity() {
    private val TAG = "btaThirdActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

            val nombre = intent.getStringExtra("Nombre")
            val direccionCalle = intent.getStringExtra("Direccion Calle")
            val direccionLocalidad = intent.getStringExtra("Direccion Locality")
            val postalCode = intent.getStringExtra("Codigo Postal")
            val precio = intent.getLongExtra("Precio", 0)
            val descripcion = intent.getStringExtra("Horario")
            val latitude = intent.getStringExtra("Latitude")
            val longitude = intent.getStringExtra("Longitude")

            Log.d(TAG, "Nombre: $nombre")
            Log.d(TAG, "Calle: $direccionCalle")

            Log.d(TAG, "Precio: $precio")
            Log.d(TAG, "Horario: $descripcion")
            Log.d(TAG, "Latitud: $latitude")
            Log.d(TAG, "Longitud: $longitude")

            findViewById<TextView>(R.id.textView2).text = nombre
            findViewById<TextView>(R.id.textView4).text = direccionCalle
            findViewById<TextView>(R.id.textView6).text = direccionLocalidad + " " + postalCode
            findViewById<TextView>(R.id.textView8).text = precio.toString()
            findViewById<TextView>(R.id.textView10).text = descripcion
            findViewById<TextView>(R.id.textView12).text = latitude.toString()
            findViewById<TextView>(R.id.textView14).text = longitude.toString()


            val otherButton: Button = findViewById(R.id.thirdButton)
            otherButton.setOnClickListener {
                val intent = Intent(this, SecondActivity::class.java)
                this.startActivity(intent)
            }
        }

    }


