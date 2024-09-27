plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

}
apply("${rootDir.path}/basic.gradle")
android {
    resourcePrefix  = ""
    namespace = "per.goweii.basic.res"
}

dependencies {
    api (libs.appcompat)

}
