package bmi.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import bmi.calculator.viewmodels.BmiDescriptionViewModel
import kotlinx.coroutines.launch

class BMIDescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmidescription)

        val viewModel: BmiDescriptionViewModel by viewModels()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->



                    //updateUI(uiState)



                    // Update UI elements
                }
            }
        }
    }

//    private fun updateUI(uiState: BMIDescriptionUiState) {
//        val resultValueTextView = findViewById<TextView>(R.id.result_value)
//        val resultCategoryTextView = findViewById<TextView>(R.id.result_category)
//
//        if(uiState.bmi!= null ) {
//            resultValueTextView.text = "BMI: ${uiState.bmi}"
//            resultValueTextView.setTextColor(uiState.color)
//            resultCategoryTextView.setTextColor(uiState.color)
//            resultCategoryTextView.text = "${uiState.category}"
//        }
//
//        // Update other UI components accordingly
//    }
}