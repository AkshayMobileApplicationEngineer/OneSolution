package com.app.solution

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.otpless.dto.HeadlessResponse
import com.otpless.main.OtplessManager
import com.otpless.main.OtplessView

class LoginActivity : AppCompatActivity() {

    private lateinit var otpView: OtplessView
    private lateinit var mobileNumberInput: EditText
    private lateinit var otpInput: EditText
    private lateinit var btnContinue: Button
    private lateinit var btnVerify: Button
    private lateinit var tvResendOtp: TextView
    private lateinit var viewFlipper: ViewFlipper

    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.primaryColor)

        // Initialize views
        mobileNumberInput = findViewById(R.id.mobile_number)
        otpInput = findViewById(R.id.otp_input)
        btnContinue = findViewById(R.id.btn_continue)
        btnVerify = findViewById(R.id.btn_verify)
        tvResendOtp = findViewById(R.id.tv_resend_otp)
        viewFlipper = findViewById(R.id.viewFlipper)

        // Initialize Otpless SDK
        otpView = OtplessManager.getInstance().getOtplessView(this)
        otpView.initHeadless("YOUR_APP_ID") // Replace with your actual Otpless App ID
        otpView.setHeadlessCallback(this::onHeadlessCallback)

        // Set up listeners for user interactions
        setupListeners()
    }

    private fun setupListeners() {
        btnContinue.setOnClickListener {
            handleMobileNumberInput()
        }

        btnVerify.setOnClickListener {
            handleOtpInput()
        }

        tvResendOtp.setOnClickListener {
            if (!isTimerRunning) {
                startCountdownTimer()
                resendOtp()
            } else {
                showToast("Please wait for the timer to finish.")
            }
        }
    }

    private fun onHeadlessCallback(response: HeadlessResponse) {
        if (response.getStatusCode() == 200) {
            when (response.getResponseType()) {
                "INITIATE" -> {
                    // OTP initiation successful
                    showToast("OTP sent successfully!")
                }
                "VERIFY" -> {
                    // OTP verification successful
                    showToast("OTP Verified successfully!")
                    navigateToHomeScreen()
                }
                "OTP_AUTO_READ" -> {
                    // Automatically fill OTP in the input field
                    val otp = response.getResponse()?.optString("otp")
                    otpInput.setText(otp)
                }
                else -> {
                    // Handle any unexpected response type
                    showToast("Unexpected response from Otpless.")
                }
            }
        } else {
            // Handle error response
            val errorMessage = response.getResponse()!!.optString("errorMessage", "An error occurred.")
            showToast("Error: $errorMessage")
        }
    }

    private fun handleMobileNumberInput() {
        val mobileNumber = mobileNumberInput.text.toString().trim()

        when {
            mobileNumber.isEmpty() -> showToast("Mobile number cannot be empty.")
            mobileNumber.length != 10 || !mobileNumber.all { it.isDigit() } -> showToast("Enter a valid 10-digit mobile number.")
            else -> {
                // Proceed to OTP screen
                viewFlipper.displayedChild = 1 // Navigate to OTP view in ViewFlipper

                // Start OTP process
                otpView.initiate(mobileNumber)
            }
        }
    }

    private fun handleOtpInput() {
        val otp = otpInput.text.toString().trim()

        when {
            otp.isEmpty() -> showToast("OTP cannot be empty.")
            otp.length != 6 || !otp.all { it.isDigit() } -> showToast("Enter a valid 6-digit OTP.")
            else -> {
                // Verify OTP using Otpless
                otpView.verify(otp)
            }
        }
    }

    private fun startCountdownTimer() {
        tvResendOtp.isEnabled = false
        isTimerRunning = true

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                tvResendOtp.text = "Resend OTP in ${secondsRemaining}s"
            }

            override fun onFinish() {
                tvResendOtp.isEnabled = true
                tvResendOtp.text = "Resend OTP"
                isTimerRunning = false
            }
        }.start()
    }

    private fun resendOtp() {
        // Trigger resend OTP logic
        val mobileNumber = mobileNumberInput.text.toString().trim()
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        otpView.initiate(mobileNumber)
        showToast("Resending OTP...")
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
