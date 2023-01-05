// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Root build.gradle.kts
buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }
}
//plugins {
//    id ("com.android.application") version "7.4.0-rc03" apply false
//    //alias(libs.plugins.android.application)
//    id ("com.android.library") version "7.4.0-rc03" apply false
//    id ("org.jetbrains.kotlin.android") version "1.7.20" apply false
//}
