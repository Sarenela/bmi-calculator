package bmi.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import android.graphics.Color
import bmi.calculator.BMICategory
import bmi.calculator.BMIViewModel
import bmi.calculator.MetricBMIViewModel
import bmi.calculator.ImperialBMIViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButtonToggleGroup.OnButtonCheckedListener

class MainActivity : AppCompatActivity() {

    private lateinit var bmiViewModel: BMIViewModel
    private lateinit var switchSystemButton: ToggleButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchSystemButton = findViewById(R.id.switch_system_button)
        bmiViewModel = MetricBMIViewModel()  //metric by default

        OnClickChangeSystem()

        onButtonClickShowBMI()
    }


    private fun updateTextViewsWithBMIResults(calculatedBMI: Double) {

        val category = bmiViewModel.categorizeBMI(calculatedBMI)

        println(category.name)

        val resultValueTextView = findViewById<TextView>(R.id.result_value)
        val resultCategoryTextView = findViewById<TextView>(R.id.result_category)

        resultValueTextView.text = "BMI: $calculatedBMI"
        resultValueTextView.setTextColor(category.color)
        resultCategoryTextView.setTextColor(category.color)
        resultCategoryTextView.text = "${category.name}"
    }

    private fun onButtonClickShowBMI(){
        val calculateButton: Button = findViewById(R.id.calculate_button)
        calculateButton.setOnClickListener {
            val measurements = retrieveMeasurements()
            if (measurements != null) {
                val bmi = bmiViewModel.calculateBMI(measurements.first, measurements.second)
                updateTextViewsWithBMIResults(bmi)
            }
        }
    }

    private fun OnClickChangeSystem() {
        val weightInput = findViewById<EditText>(R.id.weight_input)
        val heightInput = findViewById<EditText>(R.id.height_input)

        switchSystemButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                bmiViewModel = ImperialBMIViewModel() // Change to Imperial System
                weightInput.hint = "Enter Weight (pounds)"
                heightInput.hint = "Enter Height (feet)"
            } else {
                bmiViewModel = MetricBMIViewModel() // Change to Metric System
                weightInput.hint = "Enter Weight (kg)"
                heightInput.hint = "Enter Height (cm)"
            }
            weightInput.text.clear()
            heightInput.text.clear()
        }
    }

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