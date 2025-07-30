plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "macom.inote"
    compileSdk = 34

    defaultConfig {
        applicationId = "macom.inote"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
}
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}
dependencies {
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.gson)

    implementation(libs.androidx.room.runtime)
    //使用Kotlin标注处理工具ksp
    ksp(libs.androidx.room.compiler)
    //可选项,使用Kotlin扩展和协程
    implementation(libs.androidx.room.ktx)
    //可选项，使用RxJava3支持Room
    implementation(libs.androidx.room.rxjava3)
    //retrofit框架
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //增加RxJava库的依赖
    implementation("io.reactivex.rxjava3:rxjava:3.1.5")
    //增加在Android对RxJava库的支持
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    //增加Retrofit支持RxJava3的CallAdapter
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")

//    implementation("androidx.compose.material3:material3:1.2.0") // 或更高版本


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}