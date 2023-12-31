package bmi.calculator

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import bmi.calculator.R
import bmi.calculator.viewmodels.BmiDescriptionViewModel

class BMIDescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmidescription)

        val bmiViewModel: BmiDescriptionViewModel by viewModels()

        val categoryTextView = findViewById<TextView>(R.id.bmiTitleText)
        val descriptionTextView = findViewById<TextView>(R.id.bmiDescritpionText)

        val color = intent.getIntExtra(getString(R.string.color),0)
        categoryTextView.text = intent.getStringExtra(getString(R.string.category))
        categoryTextView.setTextColor(color)
        bmiViewModel.updateState(intent.getStringExtra(getString(R.string.category))!!,color,this)
        descriptionTextView.text = bmiViewModel.description

    }

}