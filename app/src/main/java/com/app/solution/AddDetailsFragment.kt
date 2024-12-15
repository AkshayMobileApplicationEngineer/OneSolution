package com.app.solution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

class AddDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_details, container, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Initialize views
        val editTextName: EditText = view.findViewById(R.id.editTextName)
        val editTextRollNumber: EditText = view.findViewById(R.id.editTextRollNumber)
        val spinnerCourse: Spinner = view.findViewById(R.id.spinnerCourse)
        val spinnerDepartment: Spinner = view.findViewById(R.id.spinnerDepartment)
        val radioGroupGender: RadioGroup = view.findViewById(R.id.radioGroupGender)
        val submitButton: Button = view.findViewById(R.id.btnSubmit)

        // Populate Course Spinner
        val courses = listOf("Engineering", "Science", "Arts", "Commerce")
        val courseAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, courses)
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCourse.adapter = courseAdapter

        // Populate Department Spinner
        val departments = listOf("Computer Science", "Mechanical", "Physics", "Chemistry")
        val departmentAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, departments)
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDepartment.adapter = departmentAdapter

        // Set the submit button click listener
        submitButton.setOnClickListener {
            val name = editTextName.text.toString()
            val rollNumber = editTextRollNumber.text.toString()
            val course = spinnerCourse.selectedItem.toString()
            val department = spinnerDepartment.selectedItem.toString()
            val selectedGenderId = radioGroupGender.checkedRadioButtonId
            val gender = view.findViewById<RadioButton>(selectedGenderId)?.text.toString()

            // Create a Bundle to pass data
            val bundle = Bundle()
            bundle.putString("name", name)
            bundle.putString("rollNumber", rollNumber)
            bundle.putString("course", course)
            bundle.putString("department", department)
            bundle.putString("gender", gender)

            // Create ShowFragment and set arguments
            val showFragment = ShowDetailsFragment()
            showFragment.arguments = bundle

            // Replace the current fragment with ShowFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, showFragment)
                .addToBackStack(null)
                .commit()
        }


        return view
    }
}