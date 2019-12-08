package com.example.navigationsap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.DatePickerDialog
import android.util.Log
import android.view.View
import android.widget.*
import com.example.navigationsap.model.Address
import com.example.navigationsap.model.Trip
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlinx.serialization.*
import kotlinx.serialization.json.JSON
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var subscription: Disposable
    private var db: AppDataBase? = null
    private var addressDao: AddressDao? = null
    @ImplicitReflectionSerializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val tripJson = """
            {
              "departure": "heidelberg",
              "time": "12:00",
              "date": "01/07/2019",
              "listAddresses": [
                {
                  "arrival": "saarbrucken",
                  "time": "12:30",
                  "date": "01/07/2019"
                },
                {
                  "arrival": "munich",
                  "time": "15:30",
                  "date": "01/07/2019"
                },
                {
                  "arrival": "koln",
                  "time": "13:30",
                  "date": "01/07/2019"
                }
              ]
            }

        """.trimIndent()
        Log.i("Hedi", "Hedi json $tripJson")


        val trip = Gson().fromJson(tripJson, Trip::class.java)
        Log.i("Hedi", "Hedi  trip $trip")

        subscription = Observable.fromCallable {
            db = AppDataBase.getAppDataBase(context = this)
            addressDao = db?.addressDao()
            addressDao?.getAllTrips()
        }
            .concatMap { dbList ->
                Log.i("Hedi", "Hedi dbList $dbList")
                if(dbList.isEmpty())
                    {
                        addressDao!!.insert(trip)
                        Observable.just(dbList)
                    }
                else {
                    Observable.just(dbList)
                }
            }

            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {} //onRetrieveHitsStart()  }
            .doOnTerminate {}// onLoadMoreFinish() }
            .subscribe(
                { result ->
                    onRetrieveSuccess(result as? List<Trip>)
                },
                { error -> Log.i("Hedi", "Hedi error ${error.message}") })

    }

    private fun onRetrieveSuccess(list: List<Trip>?) {
        Log.i("Hedi", "Hedi list $list")

     val addresses = list?.get(0)?.listAddresses

        val arrivalList:List<String> = addresses!!.map { it.arrival }


        // Initialize a new array adapter object
        val adapter = ArrayAdapter<String>(
            this, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
             // Array
            arrivalList
        )

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // set on-click listener
        dateBTN.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                    dateTV.setText(mYear)
                },
                year,
                month,
                day
            )

            dpd.show()
        }

            // fromTV.setText(getLocationsJson())

        // Set the AutoCompleteTextView adapter
        auto_complete_text_view.setAdapter(adapter)


        // Auto complete threshold
        // The minimum number of characters to type to show the drop down
        auto_complete_text_view.threshold = 0


        // Set an item click listener for auto complete text view
        auto_complete_text_view.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Display the clicked item using toast
                Toast.makeText(applicationContext, "Selected : $selectedItem", Toast.LENGTH_SHORT)
                    .show()
            }


        // Set a dismiss listener for auto complete text view
        auto_complete_text_view.setOnDismissListener {
            Toast.makeText(applicationContext, "Suggestion closed.", Toast.LENGTH_SHORT).show()
        }


        // Set a click listener for root layout
        root_layout.setOnClickListener {
            val text = auto_complete_text_view.text
            Toast.makeText(applicationContext, "Inputted : $text", Toast.LENGTH_SHORT).show()
        }


        // Set a focus change listener for auto complete text view
        auto_complete_text_view.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                // Display the suggestion dropdown on focus
                auto_complete_text_view.showDropDown()
            }
        }
    }



    @ImplicitReflectionSerializer
    private fun getLocationsJson(): String? {
        var locations: String? = ""
        try {
            val inputStream: InputStream = assets.open("cities-germany.json")
            // val  inputStream:InputStream = assets.open("test.json")
            val jsonData = JSON.stringify(inputStream.bufferedReader().use { it.readText() })
            // locations = inputStream.bufferedReader().use{it.readText()}

            println("lala")
            println("lal")
            println("la")
            println("l")
            println("la")
            println("la")
            println("lala")
            println("laa")
            println(jsonData)
        } catch (e: IOException) {

        }

        return locations
    }

}

