plugins {
    alias(libs.plugins.android.library)
//    alias(libs.plugins.kotlin.android)
}
apply("${rootDir.path}/basic.gradle")
android {
    namespace = "per.goweii.basic.utils"

}

dependencies {
    api(libs.ponyo.log)
    api(libs.ponyo.crash)

}
