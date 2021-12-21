apply {
    from("$rootDir/library-build.gradle")
}

plugins {
    kotlin(KotlinPlugins.serialization) version Kotlin.version
    id(SqlDelight.plugin)
}
dependencies {
    "implementation"(project(Modules.heroDomain))

    "implementation"(Ktor.core)
    "implementation"(Ktor.clientSerialization)
    "implementation"(Ktor.android)

    "implementation"(SqlDelight.runtime)

}


sqldelight {
    database("HeroDatabase"){
//        this packagename under the sqldelight
        packageName = "com.pasukanlangit.id.hero_datasource.cache"
        sourceFolders = listOf("sqldelight")
    }
}