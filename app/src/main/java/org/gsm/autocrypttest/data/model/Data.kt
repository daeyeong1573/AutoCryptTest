package org.gsm.autocrypttest.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import org.gsm.autocrypttest.util.Constants

@Entity(tableName = Constants.TABLE_NAME)
@Parcelize
data class Data(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var address: String,
    var centerName: String,
    var centerType: String,
    var facilityName: String,
    var lat: String,
    var lng: String,
    var phoneNumber: String,
    var updatedAt: String,
):Parcelable

