import org.gradle.kotlin.dsl.support.kotlinCompilerOptions

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kapt.kotlin) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

//fun Project.androidCommomConfig() {
//    extensions.configure<com.android.build.gradle.BaseExtension> {
//        compileSdkVersion(33)
//        defaultConfig {
//            minSdk = 21
//            targetSdk = 33
//            buildToolsVersion = "33"
//            multiDexEnabled = true
//            versionName = "2.2.8"
//            versionCode = 81
//            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        }
//        buildTypes {
//            getByName("release"){
//                isDebuggable = false
//                isMinifyEnabled = true
//                proguardFiles(
//                    getDefaultProguardFile("proguard-android-optimize.txt"),
//                    "${rootProject.rootDir}/proguard-rules.pro",
//                    "proguard-rules.pro"
//                )
//            }
//            getByName("debug") {
//                isDebuggable = true
//                isMinifyEnabled = false
//                proguardFiles(
//                    getDefaultProguardFile("proguard-android-optimize.txt"),
//                    "${rootProject.rootDir}/proguard-rules.pro",
//                    "proguard-rules.pro"
//                )
//            }
//        }
//        resourcePrefix = project.project.name + "-"
//        lintOptions{
//            isAbortOnError = false
//        }
//        compileOptions{
//            sourceCompatibility = JavaVersion.VERSION_1_8
//            targetCompatibility = JavaVersion.VERSION_1_8
//        }
//
//
//    }
//}
//subprojects {
//    afterEvaluate{
//        if (plugins.hasPlugin("com.android.library") || plugins.hasPlugin("com.android.application")){
//            androidCommomConfig()
//        }
//    }
//}