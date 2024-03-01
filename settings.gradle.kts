pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://storage.zego.im/maven") } // Corrected syntax
        maven { url = uri("https://jitpack.io") } // Corrected syntax
    }
}


rootProject.name = "M.J.Messenger"
include(":app")

