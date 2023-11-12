package bmi.calculator
import BMIMeasurement
import HistoryViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import bmi.calculator.utils.BMICalculatorImperial
import bmi.calculator.utils.BMICalculatorMetric
import bmi.calculator.viewmodels.BMIUiState
import bmi.calculator.viewmodels.BmiDescriptionViewModel
import bmi.calculator.viewmodels.MainActivityViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

                    val historyViewModel: HistoryViewModel by viewModels()
                    val bmiDescriptionViewModel: BmiDescriptionViewModel by viewModels()

                    onButtonClickCalculateBMI(viewModel,historyViewModel)
                    updateUI(uiState)
                    onClickChangeSystem(viewModel)

                    burgerMenu()
                    onClickMenuOption()
                    onClickOpenDescription()

                }
            }
        }
    }

    private fun updateUI(uiState: BMIUiState) {
        val resultValueTextView = findViewById<TextView>(R.id.result_value)
        val resultCategoryTextView = findViewById<TextView>(R.id.result_category)

        if(uiState.bmi!= null ) {
            resultValueTextView.text = getString(R.string.bmi, String.format(getString(R.string.bmi_format), uiState.bmi))
            resultValueTextView.setTextColor(uiState.color)
            resultCategoryTextView.setTextColor(uiState.color)
            resultCategoryTextView.text = "${uiState.category}"
        }
    }

    private fun onButtonClickCalculateBMI(viewModel: MainActivityViewModel, historyViewModel: HistoryViewModel){
        val calculateButton: Button = findViewById(R.id.calculate_button)
        val warningField:TextView = findViewById(R.id.warning)

        calculateButton.setOnClickListener {
            val weightHeight = retrieveWeightHeight()
            if (weightHeight != null) {
                viewModel.calculateBMI(weightHeight.first,weightHeight.second,this)
                 val measurement = createMeasurement(viewModel, weightHeight.first, weightHeight.second)
                onCalculateAddToHistory(historyViewModel,measurement)
                warningField.text = getString(R.string.empty_str)
            }
            else{
                clearBMI()
                warningField.text= getString(R.string.warning)
                warningField.setTextColor(Color.RED)
            }
        }
    }

    private fun onClickChangeSystem(viewModel: MainActivityViewModel) {
        val weightInput = findViewById<EditText>(R.id.weight_input)
        val heightInput = findViewById<EditText>(R.id.height_input)
        val switchSystemButton= findViewById<ToggleButton>(R.id.switch_system_button)

        switchSystemButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked  && viewModel.uiState.value.bmiCalculator is BMICalculatorMetric) {
               viewModel.updateBMISystem(BMICalculatorImperial())
                weightInput.hint = getString(R.string.enter_weight_lb)
                heightInput.hint = getString(R.string.enter_height_in)
                weightInput.text.clear()
                heightInput.text.clear()
                clearBMI()

            }
            else  if( !isChecked && viewModel.uiState.value.bmiCalculator is BMICalculatorImperial) {
                viewModel.updateBMISystem(BMICalculatorMetric())
                weightInput.hint = getString(R.string.enter_weight_kg)
                heightInput.hint = getString(R.string.enter_height_cm)
                weightInput.text.clear()
                heightInput.text.clear()
                clearBMI()
            }
        }
    }
    private fun retrieveWeightHeight() :Pair<Double, Double>?{
        val weightInput = findViewById<EditText>(R.id.weight_input)
        val heightInput = findViewById<EditText>(R.id.height_input)
        val warningField = findViewById<TextView>(R.id.warning)
        val weightString = weightInput.text.toString()
        val heightString = heightInput.text.toString()

        if (weightString.isNotEmpty() && heightString.isNotEmpty())  {
            try {
                warningField.text = getString(R.string.empty_str)
                val weight = weightString.toDouble()
                val height = heightString.toDouble()

                if(weight!= 0.0 && height!=0.0)return Pair(weight,height)
            } catch (e: NumberFormatException) {}
        }
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

    private fun onClickOpenHistory(){
        startActivity( Intent(this, HistoryActivity::class.java))
    }
    private fun onClickOpenAboutAuthor(){
        startActivity(Intent(this, AboutAuthorActivity::class.java))
    }
    private fun onClickOpenDescription(){
        val categoryTextView: TextView = findViewById(R.id.result_category)

        categoryTextView.setOnClickListener {
            startActivity(createDescription())
        }
    }

   private fun createDescription(): Intent{
        val description = Intent(this,
            BMIDescriptionActivity::class.java)

        val resultCategoryText = findViewById<TextView>(R.id.result_category)
        val color = resultCategoryText.currentTextColor

        description.putExtra(getString(R.string.category), resultCategoryText.text)
        description.putExtra(getString(R.string.color), color)
        return description
    }

    private fun createMeasurement( mainActivityViewModel: MainActivityViewModel, weight:Double, height:Double): BMIMeasurement{
        val dateFormat = DateTimeFormatter.ofPattern(getString(R.string.date_format))
        val date = LocalDateTime.now().format(dateFormat)
        val bmi = mainActivityViewModel.uiState.value.bmi
        return BMIMeasurement(date,weight,height, bmi!!,mainActivityViewModel.getSystem(this))
    }
    private fun onCalculateAddToHistory(historyViewModel: HistoryViewModel, measurement:BMIMeasurement ){
        historyViewModel.addMeasurement(measurement,this )
    }
    private fun onClickMenuOption() {
        val navView = findViewById<NavigationView>(R.id.nav_view)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.history_option -> {
                    onClickOpenHistory()
                }
                R.id.about_author_option -> {
                    onClickOpenAboutAuthor()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun clearBMI(){
        val bmiTextViews = findViewById<TextView>(R.id.result_value)
        val category = findViewById<TextView>(R.id.result_category)

        bmiTextViews.text= getString(R.string.empty_str)
            category.text= getString(R.string.empty_str)
    }
}

