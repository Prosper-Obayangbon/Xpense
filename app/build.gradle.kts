import org.gradle.internal.impldep.com.amazonaws.PredefinedClientConfigurations.defaultConfig
import org.gradle.internal.impldep.com.jcraft.jsch.ConfigRepository.defaultConfig

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.kapt)
    alias(libs.plugins.kotlin.compose)
    //alias(libs.plugins.hilt.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.room)

}

android {
    namespace = "uk.ac.tees.mad.d3424757.xpenseapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "uk.ac.tees.mad.d3424757.xpenseapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }


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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.play.services.auth)




    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.auth)


//    //Dagger - Hilt
//    implementation(libs.hilt.android.v2405)
//    kapt(libs.hilt.android.compiler.v2405)
//    implementation(libs.androidx.hilt.lifecycle.viewmodel)
//    kapt(libs.androidx.hilt.compiler.v100)
//    implementation(libs.androidx.hilt.navigation.compose.v100)

    //material icons - use with caution!
    // implementation "androidx.compose.material:material-icons-extended:$compose_version"
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)


    implementation(platform(libs.firebase.bom)) // Use the latest version
    implementation(libs.google.firebase.auth.ktx)






    // Coil
    implementation(libs.coil.compose)

    // Retrofit
    implementation(libs.retrofit)

    // OkHttp
    implementation(libs.okhttp)

    // JSON Converter
    implementation(libs.converter.gson)



    //Datastorage/Shared Prefs
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.lifecycle.viewmodel.ktx.v251)
    implementation(libs.androidx.constraintlayout.compose)

    // Kotlin KAPT dependency
    implementation(libs.kotlin.stdlib.jdk7)
    kapt(libs.kotlin.annotation.processing.gradle)




    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.room.runtime)
    implementation(libs.sqlite.bundled)

    implementation(libs.kotlin.stdlib)

    kapt(libs.androidx.room.compiler.v261)








}
kapt {
    correctErrorTypes = true
}
room {
    schemaDirectory("$projectDir/schemas")
}


