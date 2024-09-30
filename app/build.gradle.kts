plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("androidx.navigation.safeargs")
    id("kotlin-kapt")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "uz.prestige.livewater"
    compileSdk = 34

    defaultConfig {
        applicationId = "uz.prestige.livewater"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"

        ndk {
            abiFilters.clear()
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }


    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)



    //Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //RecyclerView
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.paging.runtime.ktx)

    implementation(libs.androidx.recyclerview)
    implementation(libs.adapterdelegates4)

    //ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx.v161)

    //LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.kotlin.codegen)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.kotlinx.coroutines.core.v150)


//    Animations
    implementation(libs.lottie)
    implementation(libs.converter.gson)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.shimmer)
    implementation(libs.androidx.swiperefreshlayout)

//    Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

//    Yandex map
    implementation(libs.maps.mobile)

}