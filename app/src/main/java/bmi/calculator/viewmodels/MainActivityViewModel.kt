package bmi.calculator.viewmodels
import android.graphics.Color
import androidx.lifecycle.ViewModel
import bmi.calculator.utils.BMICalculator
import bmi.calculator.utils.BMICalculatorMetric
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BMIUiState(
    val bmi: Double? = null,
    val color: Int = Color.BLACK,
    val bmiCalculator: BMICalculator = BMICalculatorMetric(),
    val category: String? = null
)
class MainActivityViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BMIUiState())
    val uiState: StateFlow<BMIUiState> = _uiState.asStateFlow()

    fun updateBMISystem(bmiSystem: BMICalculator){
        _uiState.update { currentState: BMIUiState ->
            currentState.copy(
                bmiCalculator = bmiSystem
            )
        }
    }

    fun calculateBMI(weight: Double, height: Double){
        var calculatedBmi =_uiState.value.bmiCalculator.calculateBMI(weight,height)

        _uiState.update { currentState: BMIUiState ->
            currentState.copy(
                bmi = calculatedBmi,
                color = getColor(calculatedBmi),
                category = getCategory(calculatedBmi),
            )
        }
    }

    fun getSystem():String{
        if (_uiState.value.bmiCalculator is BMICalculatorMetric ) return "metric"
        else return "imperial"
    }

    fun getCategory(bmi: Double): String {
        var category =""
        when {
            bmi < 18.5 -> category = "Underweight"

            bmi in 18.5..24.9 -> category = "Normal weight"

            bmi in 25.0..29.9 -> category = "Overweight"

            else -> category = "Obesity"
        }
        return category
    }

    fun getColor(bmi: Double):Int{
        var color :Int = Color.BLACK
        when {
            bmi < 18.5 -> color =  Color.BLUE

            bmi in 18.5..24.9 -> color = Color.GREEN

            bmi in 25.0..29.9 -> color = Color.YELLOW

            else -> color = Color.RED
        }
        return color
    }


}