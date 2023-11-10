package bmi.calculator
import BMIMeasurement
import HistoryViewModel
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            resultValueTextView.text =  "BMI: ${String.format("%.2f", uiState.bmi)}"
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
                viewModel.calculateBMI(weightHeight.first,weightHeight.second)
                 val measurement = createMeasurement(viewModel, weightHeight.first, weightHeight.second)
                onCalculateAddToHistory(historyViewModel,measurement)
                warningField.text = ""
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
                weightInput.hint = "Enter Weight (lb)"
                heightInput.hint = "Enter Height (in)"
                weightInput.text.clear()
                heightInput.text.clear()

            }
            else  if( !isChecked && viewModel.uiState.value.bmiCalculator is BMICalculatorImperial) {
                viewModel.updateBMISystem(BMICalculatorMetric())
                weightInput.hint = "Enter Weight (kg)"
                heightInput.hint = "Enter Height (cm)"
                weightInput.text.clear()
                heightInput.text.clear()
            }
        }
    }
    private fun retrieveWeightHeight() :Pair<Double, Double>?{
        val weightInput = findViewById<EditText>(R.id.weight_input)
        val heightInput = findViewById<EditText>(R.id.height_input)

        val weightString = weightInput.text.toString()
        val heightString = heightInput.text.toString()

        if (weightString.isNotEmpty() && heightString.isNotEmpty())  {
            try {
                val weight = weightString.toDouble()
                val height = heightString.toDouble()

                return Pair(weight,height)
            } catch (e: NumberFormatException) {}
        }
        else{
            val warningField = findViewById<TextView>(R.id.warning)
            warningField.text= "Please put in weight and height values"
            warningField.setTextColor(Color.RED)
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

        description.putExtra("category", resultCategoryText.text)
        description.putExtra("color", color)
        return description
    }

    private fun createMeasurement( mainActivityViewModel: MainActivityViewModel, weight:Double, height:Double): BMIMeasurement{
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val date = LocalDateTime.now().format(dateFormat)
        val bmi = mainActivityViewModel.uiState.value.bmi
        return BMIMeasurement(date,weight,height, bmi!!,mainActivityViewModel.getSystem())
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
}