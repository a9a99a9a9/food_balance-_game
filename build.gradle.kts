// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

repositories {
    mavenCentral()
    google()
    maven {
        url = uri("https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}
