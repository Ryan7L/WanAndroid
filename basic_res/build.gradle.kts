plugins {
    alias(libs.plugins.android.library)
}
apply("${rootDir.path}/basic.gradle")
android {
    resourcePrefix  = ""
    namespace = "per.goweii.basic.res"
}

dependencies {
    api ("androidx.appcompat:appcompat:1.2.0")
}
