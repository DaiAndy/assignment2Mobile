// Dai, Andy (100726784)
// Assingmnet #2 Mobile App Development


package com.example.assignment2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var db: LocationDatabaseHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = LocationDatabaseHelper(this)

        val add = findViewById<Button>(R.id.AddButton)
        val delete = findViewById<Button>(R.id.DeleteButton)
        val update = findViewById<Button>(R.id.UpdateButton)
        val query = findViewById<Button>(R.id.SearchButton)
        val view = findViewById<Button>(R.id.AllButton)
        val address = findViewById<TextInputEditText>(R.id.AddressInput)
        val long = findViewById<TextInputEditText>(R.id.LongInput)
        val lat = findViewById<TextInputEditText>(R.id.LatInput)
        val result = findViewById<TextView>(R.id.result)


        // adds the entry to the database inputted by user
        add.setOnClickListener {
            val addressText = address.text.toString()
            val latText = lat.text.toString()
            val longText = long.text.toString()

            val latVal = latText.toDouble()
            val longVal = longText.toDouble()
            db.addLoc(addressText, latVal, longVal)
            Toast.makeText(this, "Added $addressText to the database", Toast.LENGTH_LONG).show()
        }

        // deletes entry that matches the address inputted if exists
        delete.setOnClickListener {
            val addressText = address.text.toString()

            db.delLoc(addressText)
            Toast.makeText(this, "Your address has been deleted if existed.", Toast.LENGTH_LONG).show()
        }

        // updates entry if it exists
        update.setOnClickListener {
            val addressText = address.text.toString()
            val latText = lat.text.toString()
            val longText = long.text.toString()

            val latVal = latText.toDouble()
            val longVal = longText.toDouble()
            db.updateLoc(addressText, latVal, longVal)
            Toast.makeText(this, "Your address has been updates if existed.", Toast.LENGTH_LONG).show()
        }

        // search function on click
        query.setOnClickListener {
            val addressText = address.text.toString()

            val output = db.findLocation(addressText)
            val outputText = output.toString()
            result.text = outputText
        }

        // shows all entries in database
        view.setOnClickListener {
            val locations = db.getAllLocation()
            result.text = locations
        }
    }
}