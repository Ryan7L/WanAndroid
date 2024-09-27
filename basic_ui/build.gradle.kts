plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

}
apply("${rootDir.path}/basic.gradle")
android {
    namespace = "per.goweii.basic.ui"
}
dependencies {
    api(project(":basic_res"))
    api(project(":basic_utils"))
    api (libs.material)
    api (libs.smart.refresh.kernel)
    api (libs.smart.refresh.header.classics)
    api (libs.smart.refresh.header.two.level)
    api (libs.smart.refresh.footer.classics)
    api (libs.brvah)
    api (libs.recyclerview)
    api (libs.anylayer)
    api (libs.action.bar.ex)
    api (libs.percentimg)
    api (libs.roundimg)
    api (libs.circleimg)
    api (libs.magicindicator)
    api (libs.progressbar.library)
    api (libs.shadowlayout)
    api (libs.blurred)
    api (libs.visualeffect.core)
    api (libs.visualeffect.view)
    api (libs.visualeffect.blur)
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.reflect)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation (libs.bouncescrollview)
}