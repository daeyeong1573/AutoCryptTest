package org.gsm.autocrypttest.repository

import org.gsm.autocrypttest.data.local.CentersDao
import org.gsm.autocrypttest.data.model.Data
import javax.inject.Inject

class CenterDatabaseRepository @Inject constructor(
    private val centersDao: CentersDao
) {
    suspend fun insertCenter(centersEntity: Data) = centersDao.insertData(centersEntity)
    suspend fun selectCenter(id:Int) = centersDao.getCenter(id)
    suspend fun selectLineCnt() = centersDao.getLinCount()
}