pluginManagement {
    repositories {
        maven("https://jitpack.io")
        maven("https://api.xposed.info")
        maven { url = uri("https://mirrors.tuna.tsinghua.edu.cn/maven/") }
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://jitpack.io")
        maven("https://api.xposed.info")
        maven { url = uri("https://mirrors.tuna.tsinghua.edu.cn/maven/") }
        google()
        mavenCentral()
    }
}

rootProject.name = "XposedUtils"
include(":app")
 