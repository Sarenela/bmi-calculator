import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bmi.calculator.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class BMIMeasurement(
    val date: String,
    val weight: Double,
    val height: Double,
    val bmi: Double,
    val unitSystem: String
)

class HistoryViewModel : ViewModel() {
    private val PREFS_NAME = "BMI_HISTORY"
    private val _historyList = MutableLiveData<List<BMIMeasurement>>()
    val historyList: LiveData<List<BMIMeasurement>> get() = _historyList

    init {
        _historyList.value = emptyList()
    }

    fun addMeasurement(measurement: BMIMeasurement, context: Context) {
        val currentList = _historyList.value.orEmpty().toMutableList()
        currentList.add(0, measurement)
        if (currentList.size > 10) {
            currentList.removeAt(10)
        }
        _historyList.value = currentList
        saveHistoryToSharedPreferences(context)
    }
    private fun saveHistoryToSharedPreferences(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val history = historyList.value
        val historyJson = Gson().toJson(history)
        prefs.edit().putString(context.getString(R.string.history), historyJson).apply()
    }

    fun loadHistoryFromSharedPreferences(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val historyJson = prefs.getString(context.getString(R.string.history), null)
        if (historyJson != null) {
            val history = Gson().fromJson<List<BMIMeasurement>>(historyJson, object : TypeToken<List<BMIMeasurement>>() {}.type)
            _historyList.value = history
        }
    }
}