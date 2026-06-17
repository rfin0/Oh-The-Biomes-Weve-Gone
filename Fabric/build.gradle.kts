import com.hypherionmc.modpublisher.properties.ModLoader

plugins {
    id("com.gradleup.shadow")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

val minecraftVersion = project.properties["minecraft_version"] as String

configurations {
    create("common")
    "common" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
    create("shadowBundle")
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentFabric").extendsFrom(configurations["common"])
    "shadowBundle" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

loom.accessWidenerPath.set(project(":Common").loom.accessWidenerPath)

dependencies {
    implementation("net.fabricmc:fabric-loader:${project.properties["fabric_loader_version"]}")
    api("net.fabricmc.fabric-api:fabric-api:${project.properties["fabric_api_version"]}+$minecraftVersion")

    "common"(project(":Common")) { isTransitive = false }
    "shadowBundle"(project(":Common", "transformProductionFabric"))

    localRuntime("me.djtheredstoner:DevAuth-fabric:${project.properties["devauth_version"]}")

    api("com.github.glitchfiend:TerraBlender-fabric:$minecraftVersion-${project.properties["terrablender_version"]}")
//    api("dev.corgitaco:Oh-The-Trees-Youll-Grow-fabric:$minecraftVersion-${project.properties["ohthetreesyoullgrow_version"]}")
    api("dev.corgitaco.ohthetreesyoullgrow:ohthetreesyoullgrow-common-26.1:${project.properties["ohthetreesyoullgrow_version"]}")
    api("com.geckolib:geckolib-fabric-$minecraftVersion:${project.properties["geckolib_version"]}")
    api("me.lucko:fabric-permissions-api:0.7.0")

    compileOnly("mcp.mobius.waila:wthit-api:fabric-${project.properties["WTHIT"]}")
    localRuntime("mcp.mobius.waila:wthit:fabric-${project.properties["WTHIT"]}")
    localRuntime("lol.bai:badpackets:fabric-${project.properties["badPackets"]}")
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to project.version))
        }
    }

    shadowJar {
        exclude("net/potionstudios/biomeswevegone/fabric/datagen/**",
            "architectury.common.json", ".cache/**", "data/biomeswevegone/neoforge/**",
            "data/neoforge/**")
        configurations = listOf(project.configurations.getByName("shadowBundle"))
        archiveClassifier.set(null)
    }
}

publisher {
    setLoaders(ModLoader.FABRIC, ModLoader.QUILT)
    curseDepends.required.set(mutableListOf("fabric-api", "terrablender-fabric", "geckolib", "corgilib", "oh-the-trees-youll-grow"))
    modrinthDepends.required.set(mutableListOf("fabric-api", "terrablender", "geckolib", "corgilib", "oh-the-trees-youll-grow"))
    curseDepends.optional.set(mutableListOf("wthit"))
}
