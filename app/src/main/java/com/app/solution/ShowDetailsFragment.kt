package com.app.solution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

class ShowDetailsFragment : Fragment() {

    // Declare the TextViews to display the ID card information
    private lateinit var textViewName: TextView
    private lateinit var textViewRollNumber: TextView
    private lateinit var textViewCourse: TextView
    private lateinit var textViewDepartment: TextView
    private lateinit var textViewGender: TextView
    private lateinit var buttonGoBack: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show, container, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // Initialize views
        textViewName = view.findViewById(R.id.textViewName)
        textViewRollNumber = view.findViewById(R.id.textViewRollNumber)
        textViewCourse = view.findViewById(R.id.textViewCourse)
        textViewDepartment = view.findViewById(R.id.textViewDepartment)
        textViewGender = view.findViewById(R.id.textViewGender)
        buttonGoBack = view.findViewById(R.id.buttonGoBack)

        // Retrieve data passed from the previous fragment (AddDetailsFragment)
        val args = arguments
        val name = args?.getString("name") ?: "N/A"
        val rollNumber = args?.getString("rollNumber") ?: "N/A"
        val course = args?.getString("course") ?: "N/A"
        val department = args?.getString("department") ?: "N/A"
        val gender = args?.getString("gender") ?: "N/A"

        // Set the data into the TextViews
        textViewName.text = "Name: $name"
        textViewRollNumber.text = "Roll Number: $rollNumber"
        textViewCourse.text = "Course: $course"
        textViewDepartment.text = "Department: $department"
        textViewGender.text = "Gender: $gender"

        // Handle Go Back button click
        buttonGoBack.setOnClickListener {
            // Replace the current fragment with AddDetailsFragment or go back to previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }
}
