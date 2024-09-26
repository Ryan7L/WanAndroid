import com.android.build.api.dsl.ApplicationExtension
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    kotlin("kapt")
    id("kotlin-parcelize")
}
apply("${rootDir.path}/basic.gradle")
task()
android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.incremental" to "true",
                )
            }
        }
        renderscriptTargetApi = 21
        renderscriptSupportModeEnabled = true
        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
        }
        resourcePrefix = ""
        viewBinding {
            enable = true
        }
        namespace = "per.goweii.wanandroid"
    }
    packagingOptions {
        resources {
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
}
kapt {
    generateStubs = true
}
dependencies {

    implementation("androidx.startup:startup-runtime:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android:flexbox:1.0.0")
    implementation("com.github.mmin18:realtimeblurview:1.1.2")
    implementation("com.youth.banner:banner:1.4.10")
    implementation("com.github.franmontiel:PersistentCookieJar:v1.0.1")
    implementation("com.github.goweii:RevealLayout:1.3.4")
    implementation("com.github.goweii:KeyboardCompat:1.0.0")
    implementation("com.daimajia.swipelayout:library:1.2.0@aar")
    implementation("com.jakewharton:disklrucache:2.0.2")
    implementation("com.github.Kennyc1012:MultiStateView:1.3.2")
    implementation("com.github.goweii:HeartView:1.0.0")
    implementation("com.tencent.tbs:tbssdk:44286")
    implementation("androidx.webkit:webkit:1.2.0")
    implementation("com.github.chrisbanes:PhotoView:2.1.3")
    implementation("androidx.room:room-runtime:2.2.5")
    implementation("androidx.room:room-ktx:2.2.5")
    implementation("org.jsoup:jsoup:1.13.1")
    implementation("per.goweii.codex:core:1.0.0")
    implementation("per.goweii.codex:decorator-gesture:1.0.0")
    implementation("per.goweii.codex:decorator-beep:1.0.0")
    implementation("per.goweii.codex:decorator-finder-wechat:1.0.0")
    implementation("per.goweii.codex:decorator-frozen:1.0.0")
    implementation("per.goweii.codex:decorator-vibrate:1.0.0")
    implementation("per.goweii.codex:processor-zxing:1.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
    implementation("androidx.palette:palette:1.0.0")
    implementation("com.github.goweii:CropImageView:1.2.1")
    implementation("androidx.biometric:biometric:1.2.0-alpha01")
    implementation("com.github.donkingliang:ConsecutiveScroller:4.6.4")
    implementation("androidx.room:room-rxjava2:2.2.5")
    implementation(project(":basic_core"))
    kapt {
        api("androidx.room:room-compiler:2.2.5")
        api("androidx.lifecycle:lifecycle-compiler:2.2.0")
    }
    //该依赖用于解决一个报错问题：https://stackoverflow.com/questions/56639529/duplicate-class-com-google-common-util-concurrent-listenablefuture-found-in-modu
    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")
}
fun task() {
    var KEYSTORE_FILE = ""
    var KEYSTORE_FILE_PASSWORD = ""
    var KEYSTORE_ALIAS = ""
    var KEYSTORE_ALIAS_PASSWORD = ""

    var CDKEY_CLASS = ""
    var DEVELOPER_ID = ""

    var WANPWD_FORMAT = ""
    var WANPWD_PATTERN = ""
    var WANPWD_TYPE_CREATE_CDKEY = ""
    var WANPWD_TYPE_QQ = ""
    var WANPWD_TYPE_FESTIVAL = ""
    var WANPWD_TYPE_USERPAGE = ""
    var WANPWD_TYPE_CDKEY = ""
    var WANPWD_TYPE_WEB = ""
    var WANPWD_TYPE_ABOUTME = ""
    val file = project.rootProject.file("local.properties")
    if (file.exists()) {
        val properties = Properties()
        properties.load(file.inputStream())
        KEYSTORE_FILE = properties.getProperty("KEYSTORE_FILE") ?: ""
        KEYSTORE_FILE_PASSWORD = properties.getProperty("KEYSTORE_FILE_PASSWORD") ?: ""
        KEYSTORE_ALIAS = properties.getProperty("KEYSTORE_ALIAS") ?: ""
        KEYSTORE_ALIAS_PASSWORD = properties.getProperty("KEYSTORE_ALIAS_PASSWORD") ?: ""

        CDKEY_CLASS = properties.getProperty("CDKEY_CLASS") ?: ""
        DEVELOPER_ID = properties.getProperty("DEVELOPER_ID") ?: ""

        WANPWD_FORMAT = properties.getProperty("WANPWD_FORMAT") ?: ""
        WANPWD_PATTERN = properties.getProperty("WANPWD_PATTERN") ?: ""
        WANPWD_TYPE_CREATE_CDKEY = properties.getProperty("WANPWD_TYPE_CREATE_CDKEY") ?: ""
        WANPWD_TYPE_QQ = properties.getProperty("WANPWD_TYPE_QQ") ?: ""
        WANPWD_TYPE_FESTIVAL = properties.getProperty("WANPWD_TYPE_FESTIVAL") ?: ""
        WANPWD_TYPE_USERPAGE = properties.getProperty("WANPWD_TYPE_USERPAGE") ?: ""
        WANPWD_TYPE_CDKEY = properties.getProperty("WANPWD_TYPE_CDKEY") ?: ""
        WANPWD_TYPE_WEB = properties.getProperty("WANPWD_TYPE_WEB") ?: ""
        WANPWD_TYPE_ABOUTME = properties.getProperty("WANPWD_TYPE_ABOUTME") ?: ""

    } else {
        KEYSTORE_FILE = "${rootProject.projectDir}/demo.jks"
        KEYSTORE_FILE_PASSWORD = "123456"
        KEYSTORE_ALIAS = "demo"
        KEYSTORE_ALIAS_PASSWORD = "123456"
    }
    android {
        signingConfigs {
            create("release") {
                if (KEYSTORE_FILE.isNotEmpty()) {
                    storeFile = file(KEYSTORE_FILE)
                    storePassword = KEYSTORE_FILE_PASSWORD
                    keyAlias = KEYSTORE_ALIAS
                    keyPassword = KEYSTORE_ALIAS_PASSWORD
                }
            }
//            create("debug") {
//                if (KEYSTORE_FILE.isNotEmpty()) {
//                    storeFile = file(KEYSTORE_FILE)
//                    storePassword = KEYSTORE_FILE_PASSWORD
//                    keyAlias = KEYSTORE_ALIAS
//                    keyPassword = KEYSTORE_ALIAS_PASSWORD
//                }
//            }
        }
        buildTypes {
            release {
                isShrinkResources = false
                signingConfig = signingConfigs.getByName("release")
            }
            debug {
                isShrinkResources = false
                signingConfig = signingConfigs.getByName("debug")
            }
        }
        flavorDimensions.add("applicationId")
        productFlavors {
            create("per") {
                dimension = "applicationId"
                applicationId = "per.goweii.wanandroid"
            }
            create("com") {
                dimension = "applicationId"
                applicationId = "com.goweii.wanandroid"
            }
        }
        productFlavors.all {
            buildConfigField("String", "APPID_BUGLY", "\"\"")
            buildConfigField("String", "APPKEY_BUGLY", "\"\"")
            buildConfigField("String", "WANPWD_FORMAT", "\"${WANPWD_FORMAT}\"")
            buildConfigField("String", "WANPWD_PATTERN", "\"${WANPWD_PATTERN}\"")
            buildConfigField("String", "WANPWD_TYPE_QQ", "\"${WANPWD_TYPE_QQ}\"")
            buildConfigField("String", "WANPWD_TYPE_FESTIVAL", "\"${WANPWD_TYPE_FESTIVAL}\"")
            buildConfigField("String", "WANPWD_TYPE_USERPAGE", "\"${WANPWD_TYPE_USERPAGE}\"")
            buildConfigField("String", "WANPWD_TYPE_CDKEY", "\"${WANPWD_TYPE_CDKEY}\"")
            buildConfigField("String", "WANPWD_TYPE_WEB", "\"${WANPWD_TYPE_WEB}\"")
            buildConfigField("String", "WANPWD_TYPE_ABOUTME", "\"${WANPWD_TYPE_ABOUTME}\"")
            buildConfigField(
                "String",
                "WANPWD_TYPE_CREATE_CDKEY",
                "\"${WANPWD_TYPE_CREATE_CDKEY}\""
            )
            buildConfigField("String", "CDKEY_CLASS", "\"${CDKEY_CLASS}\"")
            buildConfigField("String", "DEVELOPER_ID", "\"${DEVELOPER_ID}\"")
        }
    }
}

