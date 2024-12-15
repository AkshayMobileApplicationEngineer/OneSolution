package com.app.solution

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SplashFragment : Fragment() {

    // Handler to post delayed task
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        // Post a delayed task to switch fragments after 2 seconds
        handler.postDelayed({
            // Check if the fragment is still attached to the activity before performing the transaction
            if (isAdded) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MainFragment())
                    .commit()
            }
        }, 2000)

        return view
    }

    override fun onDestroyView() {
        // Remove any pending tasks in case the fragment is destroyed before the task runs
        handler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}
