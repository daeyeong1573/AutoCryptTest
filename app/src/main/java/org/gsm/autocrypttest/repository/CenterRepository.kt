package org.gsm.autocrypttest.repository

import kotlinx.coroutines.flow.flow
import org.gsm.autocrypttest.data.network.CenterApi
import java.util.concurrent.Flow
import javax.inject.Inject

class CenterRepository @Inject constructor(
    private val centerApi : CenterApi
) {
    suspend fun getCenter(page:Int,key : String) = centerApi.getCenters(page = page, key = key)

}