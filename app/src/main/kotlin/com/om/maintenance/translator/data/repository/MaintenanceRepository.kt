package com.om.maintenance.translator.data.repository

import com.om.maintenance.translator.data.dao.MaintenanceTermDao
import com.om.maintenance.translator.data.entity.MaintenanceTerm
import kotlinx.coroutines.flow.Flow

class MaintenanceRepository(private val maintenanceTermDao: MaintenanceTermDao) {
    
    fun getAllTerms(): Flow<List<MaintenanceTerm>> = maintenanceTermDao.getAllTerms()
    
    fun searchTerms(query: String): Flow<List<MaintenanceTerm>> = maintenanceTermDao.searchTerms(query)
    
    fun getTermsByCategory(category: String): Flow<List<MaintenanceTerm>> = 
        maintenanceTermDao.getTermsByCategory(category)
    
    suspend fun insertTerm(term: MaintenanceTerm): Long = maintenanceTermDao.insertTerm(term)
    
    suspend fun updateTerm(term: MaintenanceTerm) = maintenanceTermDao.updateTerm(term)
    
    suspend fun deleteTerm(term: MaintenanceTerm) = maintenanceTermDao.deleteTerm(term)
    
    suspend fun getTermById(id: Int): MaintenanceTerm? = maintenanceTermDao.getTermById(id)
    
    suspend fun getTermsCount(): Int = maintenanceTermDao.getTermsCount()
}
