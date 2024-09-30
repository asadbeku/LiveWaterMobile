// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.androidx.navigation.navigation.safe.args.gradle.plugin)
        classpath(libs.androidx.navigation.navigation.safe.args.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}


true // Needed to make the Suppress annotation work for the plugins block