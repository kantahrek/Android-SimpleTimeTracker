import com.example.util.simpletimetracker.Deps
import com.example.util.simpletimetracker.applyAndroidLibrary

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

applyAndroidLibrary()

dependencies {
    implementation(project(":core"))
    implementation(Deps.Google.dagger)
    kapt(Deps.Kapt.dagger)
}
