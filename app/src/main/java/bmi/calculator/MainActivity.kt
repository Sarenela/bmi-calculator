package bmi.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var switchSystemButton: ToggleButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: MainActivityViewModel by viewModels()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    var bmi = uiState.bmi
                    var category = uiState.category
                    var color = uiState.color

                    onButtonClickCalculateBMI(viewModel)

                    // Update UI elements
                }
            }
        }




    }


    private fun updateTextViewsWithBMIResults(uiState: BMIUiState ) {

        val resultValueTextView = findViewById<TextView>(R.id.result_value)
        val resultCategoryTextView = findViewById<TextView>(R.id.result_category)

        resultValueTextView.text = "BMI: ${uiState.bmi}"
        resultValueTextView.setTextColor(uiState.color)
        resultCategoryTextView.setTextColor(uiState.color)
        resultCategoryTextView.text = "${uiState.category}"
    }

    private fun onButtonClickCalculateBMI(viewModel: MainActivityViewModel){
        val calculateButton: Button = findViewById(R.id.calculate_button)
        calculateButton.setOnClickListener {
            val measurements = retrieveMeasurements()
            if (measurements != null) {
                viewModel.calculateBMI(measurements.first,measurements.second)
            }
            updateTextViewsWithBMIResults(viewModel.uiState.value)
        }
    }

//    private fun OnClickChangeSystem() {
//        val weightInput = findViewById<EditText>(R.id.weight_input)
//        val heightInput = findViewById<EditText>(R.id.height_input)
//
//        switchSystemButton.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                bmiViewModel = BMICalculatorImperial() // Change to Imperial System
//                weightInput.hint = "Enter Weight (pounds)"
//                heightInput.hint = "Enter Height (inches)"
//            } else {
//                bmiViewModel = BMICalculatorMetric() // Change to Metric System
//                weightInput.hint = "Enter Weight (kg)"
//                heightInput.hint = "Enter Height (cm)"
//            }
//            weightInput.text.clear()
//            heightInput.text.clear()
//        }
//    }

    private fun retrieveMeasurements() :Pair<Double, Double>?{
        val weightInput = findViewById<EditText>(R.id.weight_input)
        val heightInput = findViewById<EditText>(R.id.height_input)

        val weightString = weightInput.text.toString()
        val heightString = heightInput.text.toString()

        if (weightString.isNotEmpty() && heightString.isNotEmpty())  {
            try {
                val weight = weightString.toDouble()
                val height = heightString.toDouble()

                // Check if weight and height are positive
                if (weight > 0 && height > 0) {
                    return Pair(weight,height)

                } else {
                    // Weight or height is not positive
                    // Handle the case when weight or height is not positive (e.g., show an error message)
                }
            } catch (e: NumberFormatException) {
                // Handle the case when the input is not a valid number
                // For example, weight or height contains invalid characters
            }
        } //else nothing
        return null
    }

}