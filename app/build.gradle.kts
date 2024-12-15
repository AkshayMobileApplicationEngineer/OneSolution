plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

}

android {
    namespace = "com.app.solution"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.app.solution"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.espresso.core)
   
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)





    implementation ("com.google.firebase:firebase-auth:22.3.0"); // Firebase Authentication
    implementation ("com.google.firebase:firebase-messaging:24.1.0") // Firebase Cloud Messaging for push notifications


    implementation ("com.facebook.shimmer:shimmer:0.5.0")// Shimmer effect for loading screens
    implementation ("com.intuit.sdp:sdp-android:1.0.6")//Scaled pixel units for responsive design



// Room Database

    implementation ("androidx.room:room-runtime:2.6.1") // Room for local database storage
    implementation ("androidx.room:room-common:2.6.1") // Common Room components
    annotationProcessor ("androidx.room:room-compiler:2.6.1")// Annotation processor for Room

}