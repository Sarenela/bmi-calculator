package com.example.bmi_calculator.historyUtils

import BMIMeasurement
import androidx.recyclerview.widget.DiffUtil

class HistoryDiffCallback : DiffUtil.ItemCallback<BMIMeasurement>() {
    override fun areItemsTheSame(oldItem: BMIMeasurement, newItem: BMIMeasurement): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: BMIMeasurement, newItem: BMIMeasurement): Boolean {
        return oldItem == newItem
    }
}