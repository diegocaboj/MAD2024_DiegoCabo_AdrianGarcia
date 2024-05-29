package es.upm.btb.helloworldkt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import es.upm.btb.helloworldkt.persistence.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FifthActivity  : AppCompatActivity() {
    private val TAG = "btaFifthActivity"
    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth)

        val timestamp = intent.getLongExtra("timestamp", 0)

        val deleteButton: Button = findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirm delete")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes") { dialog, which ->
                    database =
                        Room.databaseBuilder(
                            applicationContext,
                            AppDatabase::class.java,
                            "coordinates"
                        )
                            .build()
                    lifecycleScope.launch(Dispatchers.IO) {
                        Log.d(
                            TAG,
                            "Number of items in database before delete " + database.locationDao()
                                .getCount() + "."
                        );
                        database.locationDao().deleteLocationByTimestamp(timestamp)
                        Log.d(
                            TAG,
                            "Number of items in database after delete " + database.locationDao()
                                .getCount() + "."
                        );
                        withContext(Dispatchers.Main) {
                            finish()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }

        val otherButton: Button = findViewById(R.id.thirdButton)
        otherButton.setOnClickListener {
            val intent = Intent(this, FourthActivity::class.java)
            this.startActivity(intent)

        }
    }
}
