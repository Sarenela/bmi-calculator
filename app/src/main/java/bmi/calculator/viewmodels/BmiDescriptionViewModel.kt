package bmi.calculator.viewmodels

import android.graphics.Color
import androidx.lifecycle.ViewModel


class BmiDescriptionViewModel: ViewModel() {

    var color: Int = Color.BLACK
    var category: String= "ERROR"
    var description: String ="ERROR"
    fun updateDescription(){
        description = chooseDescription()
    }

    fun updateState(bmiCategory: String, bmiColor: Int){
        category = bmiCategory
        color = bmiColor
        description = chooseDescription()

    }

    fun chooseDescription(): String {
        return when (category) {
            "Underweight" -> "Individuals with a BMI below 18.5 are considered underweight. This category may indicate potential health risks such as nutrient deficiencies, a weakened immune system, or other health issues."
            "Normal weight" -> "Falling within this range is considered to be a healthy weight. It suggests a balanced proportion between weight and height, associated with a lower risk of weight-related health problems."
            "Overweight" -> "Individuals with a BMI in this range are considered overweight. This category indicates an excess of body weight compared to height and may predispose individuals to health issues like heart disease, diabetes, and other weight-related problems."
            else -> "This category indicates an excessive amount of body fat, posing a higher risk for various health conditions, including diabetes, high blood pressure, heart disease, certain cancers, and other obesity-related complications."
        }
    }

}