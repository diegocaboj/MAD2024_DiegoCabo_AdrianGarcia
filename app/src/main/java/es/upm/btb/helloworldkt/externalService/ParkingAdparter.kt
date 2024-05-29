package es.upm.btb.helloworldkt.externalService

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import es.upm.btb.helloworldkt.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ParkingAdapter(
        context: Context,
        var coordinatesList: MutableList<Model>
    ) :
        ArrayAdapter<Model>(context, R.layout.listview_item, coordinatesList) {

        val inflater: LayoutInflater = LayoutInflater.from(context)


        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: inflater.inflate(R.layout.listview_item, parent, false)

            val nombreTextView: TextView = view.findViewById(R.id.tvNombre)
            val direccionTextView: TextView = view.findViewById(R.id.tvDireccion)

            try {
                val item = coordinatesList[position]
                nombreTextView.text = item.title
                direccionTextView.text = item.address?.streetAddress
                view.setTag(view.id, position)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("CoordinatesAdapter", "getView: Exception parsing coordinates.")
            }
            return view
        }

        private fun formatTimestamp(timestamp: Long): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return formatter.format(Date(timestamp))
        }

        private fun formatCoordinate(value: Double): String {
            return String.format("%.6f", value)
        }

        fun updateData(newData: MutableList<Model>) {
            this.coordinatesList.clear()
            this.coordinatesList.addAll(newData)
            notifyDataSetChanged()
        }


    }
