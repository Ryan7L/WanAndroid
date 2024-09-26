import com.android.build.api.dsl.ApplicationExtension
fun Project.configureAndroidCommon(
    namespace: String,
    versionName: String = "2.2.8",
    versionCode: Int = 81,
) {
    plugins.apply("org.jetbrains.kotlin.android")
    plugins.apply("org.jetbrains.kotlin.kapt")
    extensions.getByType<ApplicationExtension>().apply {
        compileSdk = 33
        buildToolsVersion = "30.0.3"
        this.namespace = namespace
        defaultConfig {
            this.minSdk = 21
            this.targetSdk = 33
            versionName.let {
                this.versionName = it
            }
            versionCode.let {
                this.versionCode = it
            }
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            multiDexEnabled = true
        }

    }

}