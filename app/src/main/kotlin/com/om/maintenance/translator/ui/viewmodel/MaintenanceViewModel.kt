package com.om.maintenance.translator.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.om.maintenance.translator.data.entity.MaintenanceTerm
import com.om.maintenance.translator.data.repository.MaintenanceRepository
import kotlinx.coroutines.launch

class MaintenanceViewModel(private val repository: MaintenanceRepository) : ViewModel() {
    
    val allTerms = repository.getAllTerms().asLiveData()
    
    fun searchTerms(query: String) = repository.searchTerms(query).asLiveData()
    
    fun getTermsByCategory(category: String) = repository.getTermsByCategory(category).asLiveData()
    
    fun addTerm(arabicTerm: String, englishTerm: String, description: String = "", category: String = "عام") {
        viewModelScope.launch {
            val term = MaintenanceTerm(
                arabicTerm = arabicTerm,
                englishTerm = englishTerm,
                description = description,
                category = category,
                isCustom = true
            )
            repository.insertTerm(term)
        }
    }
    
    fun updateTerm(term: MaintenanceTerm) {
        viewModelScope.launch {
            repository.updateTerm(term)
        }
    }
    
    fun deleteTerm(term: MaintenanceTerm) {
        viewModelScope.launch {
            repository.deleteTerm(term)
        }
    }
    
    fun getTermsCount() {
        viewModelScope.launch {
            repository.getTermsCount()
        }
    }
}

class MaintenanceViewModelFactory(private val repository: MaintenanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MaintenanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MaintenanceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
