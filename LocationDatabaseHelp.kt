// Dai, Andy (100726784)
// Assingmnet #2 Mobile App Development

package com.example.assignment2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class LocationDatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION ) {

    companion object{
        private const val DATABASE_NAME = "Toronto.db"
        private const val DATABASE_VERSION = 1

        // table name and column being stored
        private const val TABLE_LOCATION = "locations"
        private const val COLUMN_ID = "id"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_LATITUDE = "lat"
        private const val COLUMN_LONGITUDE = "long"
    }

    // function to create teh table for the database
    override fun onCreate(db: SQLiteDatabase) {

        val createTable = "CREATE TABLE $TABLE_LOCATION ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_ADDRESS TEXT, $COLUMN_LATITUDE REAL, $COLUMN_LONGITUDE REAL)"
        db.execSQL(createTable)
    }

    // function to update the database table
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS $TABLE_LOCATION")
        onCreate(db)
    }

    // adds new entry to the database inputted by the user
    fun addLoc(address: String, lat: Double, long: Double){

        // sets up database to write to it
        val db = this.writableDatabase

        // formats inputted values to be put in database
        val vals = ContentValues().apply {
            put(COLUMN_ADDRESS, address)
            put(COLUMN_LATITUDE, lat)
            put(COLUMN_LONGITUDE, long)
        }

        // inserts row into table of the database
        db.insert(TABLE_LOCATION, null, vals)
    }

    // function to delete an entry
    fun delLoc (address: String) {

        // sets up database to write to it
        val db = this.writableDatabase

        // deletes the entry that has the matched address
        db.delete(TABLE_LOCATION, "$COLUMN_ADDRESS = ?", arrayOf(address))
    }

    // function to update an address already in the database
    fun updateLoc(address: String, long: Double, lat: Double) {

        // sets up database to write to it
        val db = this.writableDatabase

        // formats inputted values to be put in database
        val vals = ContentValues().apply {
            put(COLUMN_LATITUDE, lat)
            put(COLUMN_LONGITUDE, long)
        }

        // updates the entry in the database that matches the address provided
        db.update(TABLE_LOCATION, vals, "$COLUMN_ADDRESS = ?", arrayOf(address))
    }

    // finds location that matches the address given
    fun findLocation(address: String): String {

        val db = this.readableDatabase
        val query = "SELECT $COLUMN_LATITUDE, $COLUMN_LONGITUDE FROM $TABLE_LOCATION WHERE $COLUMN_ADDRESS = ?"

        val output = db.rawQuery(query, arrayOf(address))

        // sets up a string builder to add each part of the entry together
        val stringBuilder = StringBuilder()

        do {

            // collects the entries that match and adds it into the string builder
            val add = output.getString(output.getColumnIndexOrThrow(COLUMN_ADDRESS))
            val lat = output.getDouble(output.getColumnIndexOrThrow(COLUMN_LATITUDE))
            val long = output.getDouble(output.getColumnIndexOrThrow(COLUMN_LONGITUDE))

            stringBuilder.append("$add $lat $long \n")
        } while (output.moveToNext())

        // releases resources
        output.close()
        db.close()

        return stringBuilder.toString()
    }


    // function to retrieve all entries within the database
    fun getAllLocation(): String {

        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_LOCATION"

        val cursor = db.rawQuery(query, null)

        // sets up a string builder to add each part of the entry together
        val stringBuilder = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                val add = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
                val lat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE))
                val long = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))

                stringBuilder.append("$add $lat $long \n")
            } while (cursor.moveToNext())

            // releases resources
            cursor.close()
            db.close()

        }
        return stringBuilder.toString()

    }
}