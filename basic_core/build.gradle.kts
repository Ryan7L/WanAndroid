plugins {
    alias(libs.plugins.android.library)
    kotlin("kapt")
}
apply("${rootDir.path}/basic.gradle")
android {
    namespace = "per.goweii.basic.core"
}

dependencies {
    api (project(":basic_ui"))
    implementation ("androidx.multidex:multidex:2.0.1")
    api ("androidx.localbroadcastmanager:localbroadcastmanager:1.0.0")
    api ("org.greenrobot:eventbus:3.1.1")
    api ("com.google.code.gson:gson:2.8.5")
    api ("com.github.bumptech.glide:glide:4.11.0")

    api("com.github.bumptech.glide:okhttp3-integration:4.11.0") {
        isTransitive = false
    }
    api("com.github.goweii:RxHttp:2.2.12") {
        exclude(group = "com.google.code.gson")
    }
    api ("com.github.goweii:AnyPermission:1.1.2")
    api ("com.github.goweii:SwipeBack:2.0.5")
    api ("com.github.goweii.LazyFragment:lazyfragment:1.1.2")
    kapt{
        api( "com.github.bumptech.glide:compiler:4.11.0")
    }
}
