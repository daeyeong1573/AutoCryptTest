package org.gsm.autocrypttest.data.network

import org.gsm.autocrypttest.data.model.ResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CenterApi {

    @GET("15077586/v1/centers")
    suspend fun getCenters(
        @Query("page") page :Int,
        @Query("perPage") perPage :Int = 10,
        @Query("serviceKey") key : String
    ):Response<ResponseData>

}