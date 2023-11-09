package bmi.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var switchSystemButton: ToggleButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var menuButton: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val viewModel: MainActivityViewModel by viewModels()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->


                    onButtonClickCalculateBMI(viewModel)
                    updateUI(uiState)
                    onClickChangeSystem(viewModel)

                    burgerMenu()
                    onClickMenuOption()



                    // Update UI elements
                }
            }
        }
    }

    private fun updateUI(uiState: BMIUiState) {
        val resultValueTextView = findViewById<TextView>(R.id.result_value)
        val resultCategoryTextView = findViewById<TextView>(R.id.result_category)

        if(uiState.bmi!= null ) {
            resultValueTextView.text = "BMI: ${uiState.bmi}"
            resultValueTextView.setTextColor(uiState.color)
            resultCategoryTextView.setTextColor(uiState.color)
            resultCategoryTextView.text = "${uiState.category}"
        }

        // Update other UI components accordingly
    }


    private fun onButtonClickCalculateBMI(viewModel: MainActivityViewModel){
        val calculateButton: Button = findViewById(R.id.calculate_button)
        calculateButton.setOnClickListener {
            val measurements = retrieveMeasurements()
            if (measurements != null) {
                viewModel.calculateBMI(measurements.first,measurements.second)
            }
        }
    }

    private fun onClickChangeSystem(viewModel: MainActivityViewModel) {
        val weightInput = findViewById<EditText>(R.id.weight_input)
        val heightInput = findViewById<EditText>(R.id.height_input)
        val switchSystemButton= findViewById<ToggleButton>(R.id.switch_system_button)

        switchSystemButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
               viewModel.updateBMISystem(BMICalculatorImperial())
                weightInput.hint = "Enter Weight (pounds)"
                heightInput.hint = "Enter Height (inches)"
            } else {
                viewModel.updateBMISystem(BMICalculatorMetric())
                weightInput.hint = "Enter Weight (kg)"
                heightInput.hint = "Enter Height (cm)"
            }
            weightInput.text.clear()
            heightInput.text.clear()
        /// clear bmi
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

    private fun burgerMenu() {
        drawerLayout = findViewById(R.id.drawer_layout)

        val burgerMenuButton = findViewById<Button>(R.id.burgerMenuButton)

        burgerMenuButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

//    private fun openHistory(){
//        val history = Intent(this,
//            HistoryActivity::class.java)
//
//        startActivity(history)
//    }

    private fun openAboutAuthor(){
        startActivity(Intent(this, AboutAuthorActivity::class.java))
    }

//    private fun openDescription(){
//        val description = Intent(this,
//            BMIDescriptionActivity::class.java)
//
//        val resultCategoryText = findViewById<TextView>(R.id.categoryText)
//        val color = resultCategoryText.currentTextColor
//
//        description.putExtra("category", resultCategoryText.text)
//        description.putExtra("color", color)
//        startActivity(description)
//
//    }
    private fun onClickMenuOption() {
        val navView = findViewById<NavigationView>(R.id.nav_view)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.history_option -> {
                    //openHistory()
                }
                R.id.about_author_option -> {
                    openAboutAuthor()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }


}