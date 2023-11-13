package bmi.calculator

import BMIMeasurement
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bmi.calculator.utils.HistoryDiffCallback

class HistoryAdapter:
    ListAdapter<BMIMeasurement, HistoryAdapter.MeasurementViewHolder>(HistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasurementViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.measurement, parent, false)
        return MeasurementViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MeasurementViewHolder, position: Int) {
        val measurement = getItem(position)
        holder.bind(measurement)
    }

    inner class MeasurementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val weightTextView: TextView = itemView.findViewById(R.id.weightTextView)
        private val heightTextView: TextView = itemView.findViewById(R.id.heightTextView)
        private val bmiTextView: TextView = itemView.findViewById(R.id.bmiTextView)

        fun bind(measurement: BMIMeasurement) {
            dateTextView.text = "Date: ${measurement.date}"
            weightTextView.text = "Weight: ${measurement.weight} ${if (measurement.unitSystem == "metric") "kg" else "lb"}"
            heightTextView.text = "Height: ${measurement.height} ${if (measurement.unitSystem == "metric") "cm" else "in"}"
            bmiTextView.text = "BMI: ${measurement.bmi}"
        }
    }
}