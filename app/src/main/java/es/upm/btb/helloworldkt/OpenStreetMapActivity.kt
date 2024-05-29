package es.upm.btb.helloworldkt

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline


class OpenStreetMapActivity : AppCompatActivity() {
    private val TAG = "btaOpenStreetMapActivity"
    private lateinit var map: MapView

    val parkingCoordinates = listOf(
        GeoPoint(40.38323792632855, -3.783621006318095), // Aviación Española
        GeoPoint(40.434154, -3.598831), // Estadio Metropolitano Sur
        GeoPoint(40.4844428688153, -3.66450251273440), // Fuente de la Mora
        GeoPoint(40.36521668890186, -3.737923481612698), // Islazul
        GeoPoint( 40.4941929509737, -3.72723600089653), // Pitis
        GeoPoint(40.428193768950756, -3.673778093084516), // Alcántara
        GeoPoint(40.426273740022, -3.70037418702346), // Arquitecto Ribera
        GeoPoint(40.4381779662151, -3.67683406459837), // Avenida de América
        GeoPoint(40.4112391063366, -3.73808787568741), // Avenida de Portugal
        GeoPoint(40.40709468568204, -3.7038875277131464), // Casino de la Reina
        GeoPoint(40.4242207643363, -3.67342185936768), // Felipe II
        GeoPoint(40.4296270729669, -3.70279096159205), //F uencarral
        GeoPoint(40.41840695632423, -3.7119903936183274), // Plaza de Oriente
        GeoPoint(40.4158677606471, -3.70689561392158), // Plaza Mayor
        GeoPoint(40.408797321706984, -3.6763215894089827), // Reyes Magos
        GeoPoint(40.4329452991722, -3.68611338542774), // Serrano I
        GeoPoint(40.4303442290635, -3.68850808858792), // Serrano II
        GeoPoint(40.4244268291578, -3.68965177313431), // Serrano III
        GeoPoint(40.41777541448619, -3.7002800119895505), // Sevilla
        GeoPoint(40.4272702030791, -3.68408892366904), // Velázquez - Ayala
        GeoPoint(40.4298019274054, -3.69323812827144), // Almagro
        GeoPoint(40.44429510303696, -3.6776974842015506), // Auditorio Nacional de Música
        GeoPoint(40.41845756968, -3.70704603703524), // Descalzas
        GeoPoint(40.407386591674,-3.71001515747525), // Glorieta Puerta de Toledo
        GeoPoint(40.4442297702559,-3.67765536310739), // Museo de la Ciudad
        GeoPoint(40.4254346221439,-3.6884680270103), // Plaza de Colón
        GeoPoint(40.4237811996075,-3.71152321262521), // Plaza de España
        GeoPoint(40.4145312089554,-3.70056009896723), // Santa Ana
        GeoPoint(40.38728599111143,-3.7063081042559407) //Usera
    )

    val parkingNames = listOf(
        "Aparcamiento disuasorio Aviación Española",
        "Aparcamiento disuasorio Estadio Metropolitano Sur",
        "Aparcamiento disuasorio Fuente de la Mora",
        "Aparcamiento disuasorio Islazul",
        "Aparcamiento disuasorio Pitis",
        "Aparcamiento mixto. Alcántara",
        "Aparcamiento mixto. Arquitecto Ribera",
        "Aparcamiento mixto. Avenida de América (intercambiador)",
        "Aparcamiento mixto. Avenida de Portugal",
        "Aparcamiento mixto. Casino de la Reina",
        "Aparcamiento mixto. Felipe II",
        "Aparcamiento mixto. Fuencarral",
        "Aparcamiento mixto. Plaza de Oriente",
        "Aparcamiento mixto. Plaza Mayor",
        "Aparcamiento mixto. Reyes Magos",
        "Aparcamiento mixto. Serrano I",
        "Aparcamiento mixto. Serrano II",
        "Aparcamiento mixto. Serrano III",
        "Aparcamiento mixto. Sevilla",
        "Aparcamiento mixto. Velázquez - Ayala",
        "Aparcamiento público. Almagro",
        "Aparcamiento público. Auditorio Nacional de Música (Príncipe de Vergara)",
        "Aparcamiento público. Descalzas",
        "Aparcamiento público. Glorieta Puerta de Toledo",
        "Aparcamiento público. Museo de la Ciudad",
        "Aparcamiento público. Plaza de Colón",
        "Aparcamiento público. Plaza de España",
        "Aparcamiento público. Santa Ana",
        "Aparcamiento público. Usera"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_street_map)

        Log.d(TAG, "onCreate: The activity is being created.");

        val bundle = intent.getBundleExtra("locationBundle")
        val location: Location? = bundle?.getParcelable("location")

        if (location != null) {
            Log.i(TAG, "onCreate: Location["+location.altitude+"]["+location.latitude+"]["+location.longitude+"][")

            Configuration.getInstance().load(applicationContext, getSharedPreferences("osm", MODE_PRIVATE))

            map = findViewById(R.id.map)
            map.setTileSource(TileSourceFactory.MAPNIK)
            map.controller.setZoom(18.0)

            val startPoint = GeoPoint(location.latitude, location.longitude)
            map.controller.setCenter(startPoint)

            addMarker(startPoint, "My current location")
            addMarkers(map,parkingCoordinates,parkingNames)
            //addMarkersAndRoute(map, parkingCoordinates, parkingNames)
        }

        val nearestParkingButton: Button = findViewById(R.id.nearestParkingButton)
        nearestParkingButton.setOnClickListener {
            location?.let {
                calculateDistancesToParkings(it.latitude, it.longitude)
            }
        }
    }

    private fun calculateDistancesToParkings(userLat: Double, userLon: Double) {
        val distances = mutableListOf<Pair<String, Double>>()

        for (i in parkingCoordinates.indices) {
            val parkingLat = parkingCoordinates[i].latitude
            val parkingLon = parkingCoordinates[i].longitude
            val distance = calculateEuclideanDistance(userLat, userLon, parkingLat, parkingLon)
            distances.add(Pair(parkingNames[i], distance))
        }

        distances.sortBy { it.second } // Ordenar por distancia

        val nearestParking = distances.firstOrNull()
        nearestParking?.let {
            Log.d(TAG, "Nearest Parking: ${it.first} at distance ${it.second}")
            Toast.makeText(this, "Nearest Parking: ${it.first}", Toast.LENGTH_LONG).show()
        }
    }

    private fun calculateEuclideanDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        return Math.sqrt(Math.pow(lat2 - lat1, 2.0) + Math.pow(lon2 - lon1, 2.0))
    }

    private fun addMarker(point: GeoPoint, title: String) {
        val marker = Marker(map)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = title
        map.overlays.add(marker)
        map.invalidate() // Reload map
        marker.icon = ContextCompat.getDrawable(this, R.drawable.location)
        map.overlays.add(marker)
    }

    fun addMarkers(mapView: MapView, locationsCoords: List<GeoPoint>, locationsNames: List<String>) {

        for (location in locationsCoords) {
            val marker = Marker(mapView)
            marker.position = location
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = "Marker at ${locationsNames.get(locationsCoords.indexOf(location))} ${location.latitude}, ${location.longitude}"
            marker.icon = ContextCompat.getDrawable(this, R.drawable.marker)
            mapView.overlays.add(marker)
        }
        mapView.invalidate() // Refresh the map to display the new markers
    }
//define route
    fun addMarkersAndRoute(mapView: MapView, locationsCoords: List<GeoPoint>, locationsNames: List<String>) {
        if (locationsCoords.size != locationsNames.size) {
            Log.e("addMarkersAndRoute", "Locations and names lists must have the same number of items.")
            return
        }

        val route = Polyline()
        route.setPoints(locationsCoords)
        route.color = ContextCompat.getColor(this, R.color.teal_700)
        mapView.overlays.add(route)

        for (location in locationsCoords) {
            val marker = Marker(mapView)
            marker.position = location
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            val locationIndex = locationsCoords.indexOf(location)
            marker.title = "Marker at ${locationsNames[locationIndex]} ${location.latitude}, ${location.longitude}"
            marker.icon = ContextCompat.getDrawable(this,R.drawable.marker)
            mapView.overlays.add(marker)
        }

        mapView.invalidate()
    }



    override fun onResume() {
        super.onResume()
        if (::map.isInitialized) {
            map.onResume()
        } else {
            Log.e("OpenStreetMapActivity", "map no está inicializado")
        }
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}
