package bmi.calculator.viewmodels

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.ViewModel
import bmi.calculator.R


class BmiDescriptionViewModel: ViewModel() {

    var color: Int = Color.BLACK
    var category: String= "ERROR"
    var description: String ="ERROR"
    fun updateDescription(context: Context){
        description = chooseDescription(context)
    }

    fun updateState(bmiCategory: String, bmiColor: Int, context: Context){
        category = bmiCategory
        color = bmiColor
        description = chooseDescription(context)

    }

    fun chooseDescription(context: Context): String {
        return when (category) {
            context.getString(R.string.underweight)-> context.getString(R.string.underweight_description)
            context.getString(R.string.normal_weight) -> context.getString(R.string.normal_weight_description)
            context.getString(R.string.overweight) -> context.getString(R.string.overweight_description)
            else -> context.getString(R.string.obese_description)
        }
    }

}