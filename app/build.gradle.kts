plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.weatherappjavaactivity"
    compileSdk = 35 // Або інша актуальна версія

    defaultConfig {
        applicationId = "com.example.weatherappjavaactivity"
        minSdk = 23 // Або твій вибір
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions { // Важливо для Java 8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    // Включаємо View Binding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.appcompat:appcompat:1.6.1") // Перевір останню версію
    implementation("com.google.android.material:material:1.11.0") // Перевір останню версію
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Перевір останню версію

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2") // Перевір останню версію

    // Retrofit & Gson Converter
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // ViewModel & LiveData (Java версії)
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0") // Перевір останню версію
    implementation("androidx.lifecycle:lifecycle-livedata:2.7.0") // Перевір останню версію
    // implementation("androidx.lifecycle:lifecycle-common-java8:2.7.0") // Опціонально

    // Опціонально: Glide для іконок
    // implementation("com.github.bumptech.glide:glide:4.16.0")
    // annotationProcessor("com.github.bumptech.glide:compiler:4.16.0") // Потрібно ksp або kapt плагін для процесора анотацій
}

