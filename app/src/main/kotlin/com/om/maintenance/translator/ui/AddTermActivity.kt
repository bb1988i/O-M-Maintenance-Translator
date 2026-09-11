package com.om.maintenance.translator.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.om.maintenance.translator.R
import com.om.maintenance.translator.data.database.MaintenanceDatabase
import com.om.maintenance.translator.data.entity.MaintenanceTerm
import com.om.maintenance.translator.data.repository.MaintenanceRepository
import com.om.maintenance.translator.databinding.ActivityAddTermBinding
import com.om.maintenance.translator.ui.viewmodel.MaintenanceViewModel
import com.om.maintenance.translator.ui.viewmodel.MaintenanceViewModelFactory

class AddTermActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAddTermBinding
    private val viewModel: MaintenanceViewModel by viewModels {
        val database = MaintenanceDatabase.getDatabase(this)
        val repository = MaintenanceRepository(database.maintenanceTermDao())
        MaintenanceViewModelFactory(repository)
    }
    
    private var editingTermId: Int? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTermBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
    }
    
    private fun setupUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        editingTermId = intent.getIntExtra("term_id", -1).takeIf { it != -1 }
        
        binding.addButton.setOnClickListener {
            validateAndSave()
        }
        
        binding.cancelButton.setOnClickListener {
            finish()
        }
    }
    
    private fun validateAndSave() {
        val arabicTerm = binding.arabicEditText.text.toString().trim()
        val englishTerm = binding.englishEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()
        val category = binding.categorySpinner.selectedItem.toString()
        
        if (arabicTerm.isEmpty() || englishTerm.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
            return
        }
        
        val term = MaintenanceTerm(
            arabicTerm = arabicTerm,
            englishTerm = englishTerm,
            description = description,
            category = category,
            isCustom = true
        )
        
        viewModel.addTerm(arabicTerm, englishTerm, description, category)
        
        Toast.makeText(this, getString(R.string.term_added_success), Toast.LENGTH_SHORT).show()
        finish()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
