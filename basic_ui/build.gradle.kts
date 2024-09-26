plugins {
    alias(libs.plugins.android.library)
}
apply("${rootDir.path}/basic.gradle")
android {
    namespace = "per.goweii.basic.ui"
}
dependencies {
    api(project(":basic_res"))
    api(project(":basic_utils"))
    api ("com.google.android.material:material:1.0.0")
    api ("com.scwang.smart:refresh-layout-kernel:2.0.3")
    api ("com.scwang.smart:refresh-header-classics:2.0.3")
    api ("com.scwang.smart:refresh-header-two-level:2.0.3")
    api ("com.scwang.smart:refresh-footer-classics:2.0.3")
    api ("com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.50")
    api ("androidx.recyclerview:recyclerview:1.1.0")
    api ("com.github.goweii:AnyLayer:5.0.0-alpha02")
    api ("com.github.goweii:ActionBarEx:3.3.0")
    api ("com.github.goweii:PercentImageView:1.0.2")
    api ("com.makeramen:roundedimageview:2.3.0")
    api ("de.hdodenhof:circleimageview:2.2.0")
    api ("com.github.hackware1993:MagicIndicator:1.6.0")
    api ("me.zhanghai.android.materialprogressbar:library:1.4.2")
    api ("com.github.lihangleo2:ShadowLayout:3.1.6")
    api ("com.github.goweii:blurred:1.3.0")
    api ("per.goweii.visualeffect:visualeffect-core:1.0.0")
    api ("per.goweii.visualeffect:visualeffect-view:1.0.0")
    api ("per.goweii.visualeffect:visualeffect-blur:1.0.0")

    implementation ("com.github.woxingxiao:BounceScrollView:1.5-androidx")
}