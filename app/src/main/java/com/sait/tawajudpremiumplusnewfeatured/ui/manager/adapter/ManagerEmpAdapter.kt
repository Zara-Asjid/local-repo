package com.sait.tawajudpremiumplusnewfeatured.ui.manager.adapter;

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.sait.tawajudpremiumplusnewfeatured.databinding.RowEmployeeItemBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.listener.EmployeeItemClickListenerManager
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.Manager_EmployeeData

class ManagerEmpAdapter(
    private var employeeList: ArrayList<Manager_EmployeeData>,
    private val employeeItemClickListener: EmployeeItemClickListenerManager,
    private val mContext: Context
) : RecyclerView.Adapter<ManagerEmpAdapter.ViewHolder>(), Filterable {

    private var filteredList: ArrayList<Manager_EmployeeData> = employeeList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = RowEmployeeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employeeData = filteredList[position]

        holder.txtemployeeNo.text = employeeData.employeeNo
        holder.txtemployeeName.text = employeeData.employeeName
        holder.txtLetter.text = employeeData.employeeName.firstOrNull()?.toString()?.toUpperCase() ?: ""

        holder.rlItemlayout.setOnClickListener {
            employeeItemClickListener.onEmployeeItemClickManager(employeeData)
        }
    }

    class ViewHolder(private val viewBinding: RowEmployeeItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        val txtemployeeNo = viewBinding.txtEmpNo
        val txtemployeeName = viewBinding.txtEmpName
        val rlItemlayout = viewBinding.rlItemlayout
        val txtLetter = viewBinding.txtLetter
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = ArrayList<Manager_EmployeeData>()
                val searchQuery = constraint?.toString()?.toLowerCase()?.trim()

                if (searchQuery.isNullOrEmpty()) {
                    filteredResults.addAll(employeeList)
                } else {
                    for (employee in employeeList) {
                        if (employee.employeeName.toLowerCase().contains(searchQuery)||employee.employeeNo.toLowerCase().contains(searchQuery)) {
                            filteredResults.add(employee)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredResults
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<Manager_EmployeeData>
                notifyDataSetChanged()
            }
        }
    }
}