package bmi.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
class MainActivity : AppCompatActivity() {

    private lateinit var weightInput: EditText
    private lateinit var heightInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Assuming IDs of EditText fields in your layout are weight_input and height_input
        weightInput = findViewById(R.id.weight_input)
        heightInput = findViewById(R.id.height_input)

        val calculateButton = findViewById<Button>(R.id.calculate_button)
        calculateButton.setOnClickListener {
            calculateBMI()
        }
    }

    private fun calculateBMI() {
        val weightString = weightInput.text.toString()
        val heightString = heightInput.text.toString()

        if (weightString.isNotEmpty() && heightString.isNotEmpty()) {
            val weight = weightString.toDouble()
            val height = heightString.toDouble() / 100 // convert height to meters (assuming input is in centimeters)

            val bmi = calculateBMIValue(weight, height)
            // Perform action with the calculated BMI, like displaying it or using it further.
            // Example: Toast.makeText(this, "Your BMI is $bmi", Toast.LENGTH_SHORT).show()
        } else {
            // Handle empty input cases, show an error message, etc.
        }
    }

    private fun calculateBMIValue(weight: Double, height: Double): Double {
        return weight / (height * height)
    }
}