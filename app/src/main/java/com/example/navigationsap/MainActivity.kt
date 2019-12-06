package com.example.navigationsap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.DatePickerDialog
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize a new array with elements
        val addresses = arrayOf(
            "Walldorf","Rohrbach","Kirchheim","Sandhausen","St ilgen",
            "Weststadt","Neuenheim", "BismarketPlace"
        )


        // Initialize a new array adapter object
        val adapter = ArrayAdapter<String>(
            this, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            addresses // Array
        )

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // set on-click listener
        dateBTN.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, mDay ->
                dateTV.setText(mYear)
            }, year, month, day)

            dpd.show()
        }

        // fromTV.setText(getLocationsJson())

        // Set the AutoCompleteTextView adapter
        auto_complete_text_view.setAdapter(adapter)


        // Auto complete threshold
        // The minimum number of characters to type to show the drop down
        auto_complete_text_view.threshold = 0


        // Set an item click listener for auto complete text view
        auto_complete_text_view.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
            // Display the clicked item using toast
            Toast.makeText(applicationContext,"Selected : $selectedItem",Toast.LENGTH_SHORT).show()
        }


        // Set a dismiss listener for auto complete text view
        auto_complete_text_view.setOnDismissListener {
            Toast.makeText(applicationContext,"Suggestion closed.",Toast.LENGTH_SHORT).show()
        }


        // Set a click listener for root layout
        root_layout.setOnClickListener{
            val text = auto_complete_text_view.text
            Toast.makeText(applicationContext,"Inputted : $text",Toast.LENGTH_SHORT).show()
        }


        // Set a focus change listener for auto complete text view
        auto_complete_text_view.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            if(b){
                // Display the suggestion dropdown on focus
                auto_complete_text_view.showDropDown()
            }
        }


    }

    private fun getLocationsJson(): String? {
        var locations: String? = ""
        try {
            val  inputStream:InputStream = assets.open("cities-germany.json")
            locations = inputStream.bufferedReader().use{it.readText()}

        } catch (e: IOException) {

        }

        return locations
    }

}

