package org.gsm.autocrypttest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.gsm.autocrypttest.data.model.Data

@Database(
    entities = [
        Data::class
    ], version = 1
)
abstract class CentersDatabase : RoomDatabase() {
    abstract fun centerDao() : CentersDao
}