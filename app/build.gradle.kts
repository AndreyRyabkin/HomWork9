plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "ru.netology.androidhw3"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.netology.androidhw3"
        minSdk = 23
        targetSdk = 35
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
    buildFeatures.viewBinding = true
}
val gson_version = "2.10.1"
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    val viewmodel_version: String = "2.8.7"
    val livedata_version: String = "2.8.7"
    val activity_version: String = "1.10.1"
    val recyclerview_version: String = "androidx.recyclerview:recyclerview-selection:1.1.0"

    dependencies {
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$viewmodel_version")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:$livedata_version")
        implementation("androidx.activity:activity-ktx:$activity_version")
        implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
        implementation("com.google.code.gson:gson:$gson_version")

    }

}