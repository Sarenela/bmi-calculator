package bmi.calculator
import android.graphics.Color
import androidx.lifecycle.ViewModel

class BMICategory {

    var name: String = ""
    var description: String = ""
    var color:Int = Color.BLACK

}

abstract class BMIViewModel : ViewModel() {
    abstract fun calculateBMI(weight: Double, height: Double): Double
    fun categorizeBMI(bmi: Double): BMICategory {
        val category = BMICategory()
        when {
            bmi < 18.5 -> {
                category.name = "Underweight"
                category.description =
                    "BMI below 18.5\nIndividuals with a BMI below 18.5 are considered underweight. This category may indicate potential health risks such as nutrient deficiencies, a weakened immune system, or other health issues."
                category.color = Color.BLUE
            }

            bmi in 18.5..24.9 -> {
                category.name = "Normal weight"
                category.description =
                    "BMI between 18.5 and 24.9\nFalling within this range is considered to be a healthy weight. It suggests a balanced proportion between weight and height, associated with a lower risk of weight-related health problems."
                category.color = Color.GREEN
            }

            bmi in 25.0..29.9 -> {
                category.name = "Overweight"
                category.description =
                    "BMI between 25 and 29.9\nIndividuals with a BMI in this range are considered overweight. This category indicates an excess of body weight compared to height and may predispose individuals to health issues like heart disease, diabetes, and other weight-related problems."
                category.color = Color.YELLOW
            }

            else -> {
                category.name = "Obesity"
                category.description = "BMI of 30 or higher\nThis category indicates an excessive amount of body fat, posing a higher risk for various health conditions, including diabetes, high blood pressure, heart disease, certain cancers, and other obesity-related complications."
                category.color = Color.RED
            }
        }
        return category
    }
}

class MetricBMIViewModel : BMIViewModel() {
    override fun calculateBMI(weight: Double, height: Double): Double {
        // Calculate BMI using metric system formula
        val heightInMeters = height / 100
        return weight / (heightInMeters * heightInMeters)
    }
}

class ImperialBMIViewModel : BMIViewModel() {
    override fun calculateBMI(weight: Double, height: Double): Double {
        // Calculate BMI using imperial system formula
        return (weight / (height * height)) * 703
    }
}