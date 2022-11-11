package org.gsm.autocrypttest.data.model

import com.naver.maps.geometry.LatLng

data class DbMarker(
    val latLng: LatLng,
    val id : Int,
    val centerType : String
)
