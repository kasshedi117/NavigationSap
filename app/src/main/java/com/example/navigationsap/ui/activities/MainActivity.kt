package com.example.navigationsap.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.navigationsap.DAO.TravelDao
import com.example.navigationsap.DAO.TripDao
import com.example.navigationsap.R
import com.example.navigationsap.database.AppDataBase
import com.example.navigationsap.model.Address
import com.example.navigationsap.model.Travel
import com.example.navigationsap.model.Trip
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.serialization.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.io.IOException
import kotlinx.io.InputStream


class MainActivity : AppCompatActivity() {
    private lateinit var subscription: Disposable
    private var db: AppDataBase? = AppDataBase.INSTANCE
    private var tripDao: TripDao? = null
    private var travelDao: TravelDao? = null
    private lateinit var trip: Trip
    private var arrival: Address = Address("","","")

    @ImplicitReflectionSerializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auto_complete_text_view.visibility = View.VISIBLE

        getTripFromJson()
        init()


    }

    private fun init(){

        subscription = Observable.fromCallable {
            db = AppDataBase.getAppDataBase(context = this)
            tripDao = db?.tripDao()
            tripDao?.getAllTrips()
        }
            .concatMap { dbList ->
                if (dbList.isEmpty()) {
                    tripDao!!.insert(trip)
                    Observable.just(dbList)
                } else {
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

        initView()
    }

    private fun initView(){
        fromInfoTV.setText(trip.departure)
        dateFromInfoTV.setText(trip.date)
        timeFromInfoTV.setText(trip.time)
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
                auto_complete_text_view.visibility = View.GONE
                toLL.visibility = View.VISIBLE

                for(item in trip.listAddresses!!){
                    Log.i("Hedi", "item:  : $item")
                    if(item.arrival == selectedItem){
                        arrival = item
                    }
                }


                dateToInfoTV.setText(arrival.date)
                timeToInfoTV.setText(arrival.time)

                saveData()
                hideKeyboard()

                Toast.makeText(applicationContext, "Selected : $selectedItem", Toast.LENGTH_SHORT)
                    .show()
            }


        // Set a dismiss listener for auto complete text view
        auto_complete_text_view.setOnDismissListener {
            // Toast.makeText(applicationContext, "Suggestion closed.", Toast.LENGTH_SHORT).show()
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


    private fun saveData() {
        val departure = Address(trip.departure, trip.time, trip.date)
        val travel = Travel(0, departure, arrival)

        db = AppDataBase.getAppDataBase(context = this)
        travelDao = db?.travelDao()
        //Insert Case
        val thread = Thread {

            travelDao?.insert(travel)

            //fetch Records
            travelDao?.getAllTravels()?.forEach()
            {
                Log.i("Fetch Records", "Id:  : ${it.departure}")
                Log.i("Fetch Records", "Name:  : ${it.arrival}")
            }
        }
        thread.start()
    }

    private fun getTripFromJson() {
        try {
            val inputStream:InputStream = assets.open("address.json")
            val locations = inputStream.bufferedReader().use{it.readText()}
            trip = Gson().fromJson(locations, Trip::class.java)
        } catch (e: IOException) {

        }
    }

    fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            Log.i("Keyboard", "Hedi enter")
        }
        else {
            Log.i("Keyboard", "Hedi enter")
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }

}


