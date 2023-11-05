package bmi.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var weightInput: EditText
    private lateinit var heightInput: EditText
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weightInput = findViewById(R.id.weight_input)
        heightInput = findViewById(R.id.height_input)
        resultTextView = findViewById(R.id.result_text) // Dodany TextView do wyświetlenia wyniku

        val calculateButton: Button = findViewById(R.id.calculate_button)
        calculateButton.setOnClickListener {
            val weightString = weightInput.text.toString()
            val heightString = heightInput.text.toString()

            if (weightString.isNotEmpty() && heightString.isNotEmpty()) {
                try {
                    val weight = weightString.toDouble()
                    val height = heightString.toDouble()

                    if (weight > 0 && height > 0) {
                        val bmi = calculateBMI(weight, height)
                        resultTextView.text = String.format("BMI: %.2f", bmi)
                    } else {
                        resultTextView.text = "Waga i wzrost muszą być większe od zera."
                    }
                } catch (e: NumberFormatException) {
                    resultTextView.text = "Wprowadzono nieprawidłowe dane."
                }
            } else {
                resultTextView.text = "Wprowadź wagę i wzrost."
            }
        }
    }

    private fun calculateBMI(weight: Double, height: Double): Double {
        val heightInMeters = height / 100
        return weight / (heightInMeters * heightInMeters)
    }
}