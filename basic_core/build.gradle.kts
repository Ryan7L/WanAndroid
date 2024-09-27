plugins {
    alias(libs.plugins.android.library)
    kotlin("kapt")
//    alias(libs.plugins.kotlin.android)

}
apply("${rootDir.path}/basic.gradle")
android {
    namespace = "per.goweii.basic.core"
}

dependencies {
    api(project(":basic_ui"))
    implementation(libs.multidex)
    api(libs.localbroadcastmanager)
    api(libs.exentbus)
    api(libs.gson)
    api(libs.glide)

    api(libs.glide.okhttp3) {
        isTransitive = false
    }
    api(libs.rx.http) {
        exclude(group = "com.google.code.gson")
    }
    api(libs.anypermission)
    api(libs.swipeback)
    api(libs.lazyfragment)
    kapt {
        api(libs.glide.compiler)
    }

}
