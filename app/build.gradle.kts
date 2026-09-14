plugins { id("com.android.application") }

android {
    namespace = "dev.s24.fluidlauncher"
    compileSdk = 35
    defaultConfig {
        applicationId = "dev.s24.fluidlauncher"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes { release { isMinifyEnabled = false; proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro") } }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    lint { abortOnError = true; checkReleaseBuilds = true }
}
