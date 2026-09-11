package com.om.maintenance.translator.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maintenance_terms")
data class MaintenanceTerm(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val arabicTerm: String,
    val englishTerm: String,
    val description: String = "",
    val category: String = "عام",
    val createdAt: Long = System.currentTimeMillis(),
    val isCustom: Boolean = false
)
