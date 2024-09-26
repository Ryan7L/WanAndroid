plugins {
    alias(libs.plugins.android.library)
}
apply("${rootDir.path}/basic.gradle")
android {
    namespace = "per.goweii.basic.utils"
}

dependencies {
    api("per.goweii.ponyo:ponyo-log:1.0.0")
    api("per.goweii.ponyo:ponyo-crash:1.0.0")
}
