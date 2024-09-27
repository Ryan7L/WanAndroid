import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    kotlin("kapt")
    id("kotlin-parcelize")
}
apply("${rootDir.path}/gradle/basic.gradle")
flavortask()
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
    packaging {
        resources {
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
    buildFeatures {
        buildConfig = true
    }
    configurations {
        implementation {
            exclude("com.intellij", "annotations")
        }
    }
}
kapt {
    generateStubs = true
}
dependencies {

    implementation(libs.startup)
    implementation(libs.cardview)
    implementation(libs.flexbox)
    implementation(libs.realtimeblurview)
    implementation(libs.banner)
    implementation(libs.cookiejar)
    implementation(libs.reveallayout)
    implementation(libs.keyboardcompat)
    implementation("com.daimajia.swipelayout:library:1.2.0@aar")
    implementation(libs.disklrucache)
    implementation(libs.multistateview)
    implementation(libs.heartview)
    implementation(libs.x5web)
    implementation(libs.webkit)
    implementation(libs.photoview)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.jsoup)
    implementation(libs.codex.core)
    implementation(libs.codex.decorator.gesture)
    implementation(libs.codex.decorator.beep)
    implementation(libs.codex.decorator.finder)
    implementation(libs.codex.decorator.frozen)
    implementation(libs.codex.decorator.vibrate)
    implementation(libs.codex.processor.zxing)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.palette)
    implementation(libs.cropimgview)
    implementation(libs.biometric)
    implementation(libs.consecutivescroller)
    implementation(libs.room.rxjava2)
    implementation(project(":basic_core"))
    kapt {
        api(libs.room.compiler)
        api(libs.lifecycle.compiler)
    }

    //该依赖用于解决一个报错问题：https://stackoverflow.com/questions/56639529/duplicate-class-com-google-common-util-concurrent-listenablefuture-found-in-modu
    implementation(libs.guavanolistenablefuture)

}

fun flavortask() {
    val keystoreFile: String
    val keystoreFilePwd: String
    val keystoreAlias: String
    val keystoreAliasPwd: String

    var cdKeyClass = ""
    var developerId = ""

    var wanPwdFormat = ""
    var wanPwdPatter = ""
    var wanPwdTypeCreateCdKey = ""
    var wanPwdTypeQq = ""
    var wanPwdTypeFestival = ""
    var wanPwdTypeUserPage = ""
    var wanPwdTypeCdKey = ""
    var wanPdTypeWeb = ""
    var wanPwdTypeAboutMe = ""
    val file = project.rootProject.file("local.properties")
    if (file.exists()) {
        val properties = Properties()
        properties.load(file.inputStream())
        keystoreFile = properties.getProperty("KEYSTORE_FILE") ?: ""
        keystoreFilePwd = properties.getProperty("KEYSTORE_FILE_PASSWORD") ?: ""
        keystoreAlias = properties.getProperty("KEYSTORE_ALIAS") ?: ""
        keystoreAliasPwd = properties.getProperty("KEYSTORE_ALIAS_PASSWORD") ?: ""

        cdKeyClass = properties.getProperty("CDKEY_CLASS") ?: ""
        developerId = properties.getProperty("DEVELOPER_ID") ?: ""

        wanPwdFormat = properties.getProperty("WANPWD_FORMAT") ?: ""
        wanPwdPatter = properties.getProperty("WANPWD_PATTERN") ?: ""
        wanPwdTypeCreateCdKey = properties.getProperty("WANPWD_TYPE_CREATE_CDKEY") ?: ""
        wanPwdTypeQq = properties.getProperty("WANPWD_TYPE_QQ") ?: ""
        wanPwdTypeFestival = properties.getProperty("WANPWD_TYPE_FESTIVAL") ?: ""
        wanPwdTypeUserPage = properties.getProperty("WANPWD_TYPE_USERPAGE") ?: ""
        wanPwdTypeCdKey = properties.getProperty("WANPWD_TYPE_CDKEY") ?: ""
        wanPdTypeWeb = properties.getProperty("WANPWD_TYPE_WEB") ?: ""
        wanPwdTypeAboutMe = properties.getProperty("WANPWD_TYPE_ABOUTME") ?: ""

    } else {
        keystoreFile = "${rootProject.projectDir}/demo.jks"
        keystoreFilePwd = "123456"
        keystoreAlias = "demo"
        keystoreAliasPwd = "123456"
    }
    android {
        signingConfigs {
            create("release") {
                if (keystoreFile.isNotEmpty()) {
                    storeFile = file(keystoreFile)
                    storePassword = keystoreFilePwd
                    keyAlias = keystoreAlias
                    keyPassword = keystoreAliasPwd
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
            buildConfigField("String", "WANPWD_FORMAT", "\"${wanPwdFormat}\"")
            buildConfigField("String", "WANPWD_PATTERN", "\"${wanPwdPatter}\"")
            buildConfigField("String", "WANPWD_TYPE_QQ", "\"${wanPwdTypeQq}\"")
            buildConfigField("String", "WANPWD_TYPE_FESTIVAL", "\"${wanPwdTypeFestival}\"")
            buildConfigField("String", "WANPWD_TYPE_USERPAGE", "\"${wanPwdTypeUserPage}\"")
            buildConfigField("String", "WANPWD_TYPE_CDKEY", "\"${wanPwdTypeCdKey}\"")
            buildConfigField("String", "WANPWD_TYPE_WEB", "\"${wanPdTypeWeb}\"")
            buildConfigField("String", "WANPWD_TYPE_ABOUTME", "\"${wanPwdTypeAboutMe}\"")
            buildConfigField(
                "String",
                "WANPWD_TYPE_CREATE_CDKEY",
                "\"${wanPwdTypeCreateCdKey}\""
            )
            buildConfigField("String", "CDKEY_CLASS", "\"${cdKeyClass}\"")
            buildConfigField("String", "DEVELOPER_ID", "\"${developerId}\"")
        }
    }
}

