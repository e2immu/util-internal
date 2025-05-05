tasks.register("test") {
    dependsOn(gradle.includedBuilds.map { it.task(":test") })
}
tasks.register("clean") {
    dependsOn(gradle.includedBuilds.map { it.task(":clean") })
}
tasks.register("publish") {
    dependsOn(gradle.includedBuilds.map { it.task(":publish") })
}
tasks.register("publishToMavenLocal") {
    dependsOn(gradle.includedBuilds.map { it.task(":publishToMavenLocal") })
}
