package org.gsm.autocrypttest.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.gsm.autocrypttest.data.model.Data
import org.gsm.autocrypttest.util.Constants


@Dao
interface CentersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(centersInfo : Data)

    @Query("SELECT * FROM ${Constants.TABLE_NAME} WHERE id = :id")
    suspend fun getCenter(id: Int) : Data

    @Query("SELECT COUNT(*) FROM ${Constants.TABLE_NAME}")
    suspend fun getLinCount() : Int

}