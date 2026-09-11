package com.om.maintenance.translator.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.om.maintenance.translator.R
import com.om.maintenance.translator.data.entity.MaintenanceTerm
import com.om.maintenance.translator.databinding.ItemMaintenanceTermBinding

class MaintenanceAdapter(
    private val onDeleteClick: (MaintenanceTerm) -> Unit,
    private val onEditClick: (MaintenanceTerm) -> Unit
) : ListAdapter<MaintenanceTerm, MaintenanceAdapter.MaintenanceViewHolder>(DiffCallback()) {

    inner class MaintenanceViewHolder(private val binding: ItemMaintenanceTermBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(term: MaintenanceTerm) {
            binding.apply {
                arabicTermTv.text = term.arabicTerm
                englishTermTv.text = term.englishTerm
                descriptionTv.text = term.description.takeIf { it.isNotEmpty() } ?: "بدون وصف"
                categoryTv.text = term.category
                
                // Copy Arabic Term
                copyArabicBtn.setOnClickListener {
                    copyToClipboard(binding.root.context, term.arabicTerm, "المصطلح العربي")
                }
                
                // Copy English Term
                copyEnglishBtn.setOnClickListener {
                    copyToClipboard(binding.root.context, term.englishTerm, "المصطلح الإنجليزي")
                }
                
                // Delete Button
                deleteBtn.setOnClickListener {
                    onDeleteClick(term)
                }
                
                // Edit Button
                editBtn.setOnClickListener {
                    onEditClick(term)
                }
            }
        }
        
        private fun copyToClipboard(context: Context, text: String, label: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(label, text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaintenanceViewHolder {
        val binding = ItemMaintenanceTermBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MaintenanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaintenanceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<MaintenanceTerm>() {
        override fun areItemsTheSame(oldItem: MaintenanceTerm, newItem: MaintenanceTerm) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MaintenanceTerm, newItem: MaintenanceTerm) =
            oldItem == newItem
    }
}
