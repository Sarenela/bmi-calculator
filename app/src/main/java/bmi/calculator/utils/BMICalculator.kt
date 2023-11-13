package bmi.calculator.utils


abstract class BMICalculator {
    abstract fun calculateBMI(weight: Double, height: Double): Double

}

class BMICalculatorMetric : BMICalculator() {
    override fun calculateBMI(weight: Double, height: Double): Double {
        // Calculate BMI using metric system formula
        val heightInMeters = height / 100
        return weight / (heightInMeters * heightInMeters)
    }
}

class BMICalculatorImperial : BMICalculator(){
    override fun calculateBMI(weight: Double, height: Double): Double {
        // Calculate BMI using imperial system formula
        return (weight / (height * height)) * 703
    }
}