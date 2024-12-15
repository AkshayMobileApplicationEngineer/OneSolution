package com.app.solution

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Button
import android.app.Dialog
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment() {

    private lateinit var fabAdd: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Ensuring the light status bar on compatible devices
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.background_dark)

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        fabAdd = view.findViewById(R.id.fabAdd)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fabAdd.setOnClickListener {
                showCustomDialog()
            }
        }

        return view
    }

    // Function to show the custom dialog
    private fun showCustomDialog() {
        // Create the dialog instance
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_custom_options) // Set custom layout

        // Set dialog dimensions (optional)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Initialize buttons
        val btnInsert: TextView = dialog.findViewById(R.id.btnInsert)
        btnInsert.setTextColor(resources.getColor(R.color.primaryColor))
        val btnGet: TextView = dialog.findViewById(R.id.btnGet
        )
        btnGet.setTextColor(resources.getColor(R.color.primaryColor))
        val btnPut: TextView = dialog.findViewById(R.id.btnPut)
        btnPut.setTextColor(resources.getColor(R.color.primaryColor))
        val btnDelete: TextView = dialog.findViewById(R.id.btnDelete)
        btnDelete.setTextColor(resources.getColor(R.color.primaryColor))

        // Set button click listeners
        btnInsert.setOnClickListener {
            showMessage("INSERT option selected")
            navigateToFragment(AddDetailsFragment())  // Navigate to INSERT fragment
            dialog.dismiss()  // Dismiss the dialog
        }

        btnGet.setOnClickListener {
            showMessage("GET option selected")
            navigateToFragment(ShowDetailsFragment())  // Navigate to GET fragment
            dialog.dismiss()  // Dismiss the dialog
        }

        btnPut.setOnClickListener {
            showMessage("PUT option selected")
            // Handle PUT logic
            dialog.dismiss()  // Dismiss the dialog
        }

        btnDelete.setOnClickListener {
            showMessage("DELETE option selected")
            // Handle DELETE logic
            dialog.dismiss()  // Dismiss the dialog
        }

        // Show the dialog
        dialog.show()
    }

    // Function to navigate between fragments
    private fun navigateToFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)  // This ensures that pressing back goes to the previous fragment
        transaction.commit()
    }

    // Function to show a Toast message
    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
