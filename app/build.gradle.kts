plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.story_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.story_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField(
            "String",
            "API_KEY",
            "\"${findProperty("API_KEY").toString()}\""
        )
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
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation("io.coil-kt:coil:2.4.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.activity:activity-ktx:1.8.0")

    //room
    implementation("androidx.room:room-runtime:2.6.0")
    implementation ("androidx.room:room-ktx:2.6.0")
    implementation("androidx.room:room-paging:2.6.0")
    ksp("androidx.room:room-compiler:2.6.0")

    val cameraxVersion = "1.2.3"
    //noinspection GradleDependency
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    //noinspection GradleDependency
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    //noinspection GradleDependency
    implementation("androidx.camera:camera-view:$cameraxVersion")

    //pagination
    implementation("androidx.paging:paging-runtime-ktx:3.1.0")
}