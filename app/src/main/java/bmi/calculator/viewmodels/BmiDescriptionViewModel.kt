package bmi.calculator.viewmodels

import android.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BMIDescriptionUiState(
    val color: Int = Color.BLACK,
    val category: String? = null,
    val description: String? = null
)
class BmiDescriptionViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(BMIDescriptionUiState())
    val uiState: StateFlow<BMIDescriptionUiState> = _uiState.asStateFlow()

    fun updateDescription(){
        _uiState.update { currentState: BMIDescriptionUiState ->
            currentState.copy(
                description = getDescription()
            )
        }
    }

    fun updateState(bmiCategory: String, bmiColor: Int){
        _uiState.update { currentState: BMIDescriptionUiState ->
            currentState.copy(
                category = bmiCategory,
                color = bmiColor,
                description = getDescription()
            )
        }
    }

    fun getDescription(): String{
        val category = uiState.value.category
        when {
            category == "Underweight" -> return "Individuals with a BMI below 18.5 are considered underweight. This category may indicate potential health risks such as nutrient deficiencies, a weakened immune system, or other health issues."

            category == "Normal weight" -> return "Falling within this range is considered to be a healthy weight. It suggests a balanced proportion between weight and height, associated with a lower risk of weight-related health problems."

            category == "Overweight" -> return "Individuals with a BMI in this range are considered overweight. This category indicates an excess of body weight compared to height and may predispose individuals to health issues like heart disease, diabetes, and other weight-related problems."

            else -> return "This category indicates an excessive amount of body fat, posing a higher risk for various health conditions, including diabetes, high blood pressure, heart disease, certain cancers, and other obesity-related complications."
        }
    }


}