// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

}

buildscript {
    repositories {
        google()  // Ensure this is added
        mavenCentral()
    }
    dependencies {
        classpath ("com.google.gms:google-services:4.3.15") ; // Make sure this line is added (latest version)
    }
}