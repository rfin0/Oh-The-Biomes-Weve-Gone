architectury {
    common("fabric", "neoforge")
    platformSetupLoomIde()
}

val minecraftVersion = project.properties["minecraft_version"] as String

loom.accessWidenerPath.set(file("src/main/resources/biomeswevegone.accesswidener"))

sourceSets.main.get().resources.srcDir("src/main/generated/resources")

dependencies {
    implementation("net.fabricmc:fabric-loader:${project.properties["fabric_loader_version"]}")

    implementation("com.github.glitchfiend:TerraBlender-common:$minecraftVersion-${project.properties["terrablender_version"]}")
    implementation("dev.corgitaco.ohthetreesyoullgrow:ohthetreesyoullgrow-common-$minecraftVersion:${project.properties["ohthetreesyoullgrow_version"]}")
//    implementation("dev.corgitaco:Oh-The-Trees-Youll-Grow-common:$minecraftVersion-${project.properties["ohthetreesyoullgrow_version"]}")
    implementation("com.geckolib:geckolib-common-$minecraftVersion:${project.properties["geckolib_version"]}")

    compileOnly("mcp.mobius.waila:wthit-api:fabric-${project.properties["WTHIT"]}")
}
