package com.app.quizparlour.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import com.app.solution.MainActivity
import com.app.solution.R

import com.google.android.material.textfield.TextInputEditText

import com.skydoves.elasticviews.ElasticButton

class LoginFragment : Fragment() {

    private val SECRET_KEY_NODE = "secret_key"
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var btnContinue: ElasticButton
    private lateinit var btnVerify: ElasticButton
    private lateinit var mobileNumberInput: TextInputEditText
    private lateinit var otpInput: TextInputEditText
    private lateinit var tvResendOtp: TextView

    private var isTimerRunning = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)

        // Initialize views
        viewFlipper = rootView.findViewById(R.id.view_flipper)
        btnContinue = rootView.findViewById(R.id.btn_continue)
        btnVerify = rootView.findViewById(R.id.btn_verify)
        mobileNumberInput = rootView.findViewById(R.id.mobile_number)
        otpInput = rootView.findViewById(R.id.otp_input)
        tvResendOtp = rootView.findViewById(R.id.tv_resend_otp)

        // Handle "Continue" button click
        btnContinue.setOnClickListener {
            handleMobileNumberInput()
        }

        // Handle "Verify OTP" button click
        btnVerify.setOnClickListener {
            handleOtpInput()
        }

        // Handle "Resend OTP" click with countdown timer
        tvResendOtp.setOnClickListener {
            if (!isTimerRunning) {
                startCountdownTimer()
                resendOtp()
            } else {
                showToast("Please wait for the timer to finish.")
            }
        }

        return rootView
    }

    private fun handleMobileNumberInput() {
//        val secretKeyRef = FirebaseDatabase.getInstance().getReference(SECRET_KEY_NODE)
//        secretKeyRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val secretKeyValue = snapshot.getValue(String::class.java)
//                if (secretKeyValue != null) {
//                    Log.e("secret", "Value of secret key: $secretKeyValue")
//                    Toast.makeText(requireContext(), "Secret Key: $secretKeyValue", Toast.LENGTH_SHORT).show()
//                } else {
//                    Log.d("secret", "Secret key is null")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("Firebase12", "Failed to read value.", error.toException())
//            }
//        })

        val mobileNumber = mobileNumberInput.text.toString().trim()
        val deviceId = fetchDeviceId() // Retrieve the device ID

        when {
            mobileNumber.isEmpty() -> showToast("Mobile number cannot be empty.")
            mobileNumber.length != 10 || !mobileNumber.all { it.isDigit() } -> showToast("Enter a valid 10-digit mobile number.")
            else -> {
                // Move to OTP input screen
                viewFlipper.showNext()

                // Display mobile number and device ID in a toast
                showToast("Mobile Number: $mobileNumber\nDevice ID: $deviceId")
            }
        }
    }

    private fun handleOtpInput() {
        val otp = otpInput.text.toString().trim()

        // Check if the OTP is valid
        when {
            otp.isEmpty() -> showToast("OTP cannot be empty.")
            otp.length != 6 || !otp.all { it.isDigit() } -> showToast("Enter a valid 6-digit OTP.")
            else -> {
                val internalIntent = Intent(requireContext(), MainActivity::class.java)
                showToast("OTP verified successfully!")
                startActivity(internalIntent)
                requireActivity().finish()
            }
        }
    }

    private fun startCountdownTimer() {
        // Disable Resend OTP TextView
        tvResendOtp.isEnabled = false
        isTimerRunning = true

        // Start a countdown timer for 30 seconds
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                tvResendOtp.text = "Resend OTP in ${secondsRemaining}s"
            }

            override fun onFinish() {
                // Enable Resend OTP TextView
                tvResendOtp.isEnabled = true
                tvResendOtp.text = "Resend OTP"
                isTimerRunning = false
            }
        }.start()
    }

    private fun resendOtp() {
        // Logic to resend OTP (call API or backend)
        showToast("OTP resent successfully!")
    }

    // Function to get the device ID
    private fun fetchDeviceId(): String {
        return Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
