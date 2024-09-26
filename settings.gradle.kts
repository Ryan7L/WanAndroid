pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://gitee.com/goweii/maven-repository/raw/master/releases/")
        maven("https://maven.aliyun.com/repository/jcenter")
    }
}
rootProject.name = "WanAndroid"
include(":app")
include(":basic_core")
include(":basic_utils")
include(":basic_ui")
include(":basic_res")
