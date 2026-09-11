package com.om.maintenance.translator.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.om.maintenance.translator.data.entity.MaintenanceTerm
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceTermDao {
    
    @Insert
    suspend fun insertTerm(term: MaintenanceTerm): Long
    
    @Update
    suspend fun updateTerm(term: MaintenanceTerm)
    
    @Delete
    suspend fun deleteTerm(term: MaintenanceTerm)
    
    @Query("SELECT * FROM maintenance_terms ORDER BY createdAt DESC")
    fun getAllTerms(): Flow<List<MaintenanceTerm>>
    
    @Query("SELECT * FROM maintenance_terms WHERE id = :id")
    suspend fun getTermById(id: Int): MaintenanceTerm?
    
    @Query("""
        SELECT * FROM maintenance_terms 
        WHERE arabicTerm LIKE '%' || :query || '%' 
        OR englishTerm LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%'
        ORDER BY 
            CASE WHEN arabicTerm LIKE :query || '%' THEN 0 ELSE 1 END,
            createdAt DESC
    """)
    fun searchTerms(query: String): Flow<List<MaintenanceTerm>>
    
    @Query("SELECT * FROM maintenance_terms WHERE category = :category ORDER BY createdAt DESC")
    fun getTermsByCategory(category: String): Flow<List<MaintenanceTerm>>
    
    @Query("SELECT COUNT(*) FROM maintenance_terms")
    suspend fun getTermsCount(): Int
    
    @Query("DELETE FROM maintenance_terms WHERE id = :id")
    suspend fun deleteTermById(id: Int)
}
