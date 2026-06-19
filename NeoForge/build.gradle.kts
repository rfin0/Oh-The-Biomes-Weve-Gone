import com.hypherionmc.modpublisher.properties.ModLoader

plugins {
    id("com.gradleup.shadow")
}

architectury {
    platformSetupLoomIde()
    neoForge()
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
    getByName("developmentNeoForge").extendsFrom(configurations["common"])
    "shadowBundle" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

loom {
    accessWidenerPath.set(project(":Common").loom.accessWidenerPath)

    runs.create("datagen") {
        clientData()
        programArguments.addAll(
            "--all", "--mod", "biomeswevegone",
            "--output", project(":Common").file("src/main/generated/resources").absolutePath,
            "--existing", project(":Common").file("src/main/resources").absolutePath
        )
    }
}

dependencies {
    neoForge("net.neoforged:neoforge:${project.properties["neoforge_version"]}")

    "common"(project(":Common")) { isTransitive = false }
    "shadowBundle"(project(":Common", "transformProductionNeoForge"))

    localRuntime("me.djtheredstoner:DevAuth-neoforge:${project.properties["devauth_version"]}")

    api("com.github.glitchfiend:TerraBlender-neoforge:$minecraftVersion-${project.properties["terrablender_version"]}")
//    api("dev.corgitaco:Oh-The-Trees-Youll-Grow-neoforge:$minecraftVersion-${project.properties["ohthetreesyoullgrow_version"]}")
    api("dev.corgitaco.ohthetreesyoullgrow:ohthetreesyoullgrow-common-$minecraftVersion:${project.properties["ohthetreesyoullgrow_version"]}")
    api("com.geckolib:geckolib-neoforge-$minecraftVersion:${project.properties["geckolib_version"]}")
    compileOnly("net.luckperms:api:5.5")

    compileOnly("mcp.mobius.waila:wthit-api:neo-${project.properties["WTHIT"]}")
    localRuntime("mcp.mobius.waila:wthit:neo-${project.properties["WTHIT"]}")
    localRuntime("lol.bai:badpackets:neo-${project.properties["badPackets"]}")

    api("com.github.glitchfiend:SereneSeasons-neoforge:$minecraftVersion-26.1.2.0.3")
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/neoforge.mods.toml") {
            expand(mapOf("version" to project.version))
        }
    }

    shadowJar {
        exclude("net/potionstudios/biomeswevegone/neoforge/datagen/**",
            "architectury.common.json", ".cache/**")
        configurations = listOf(project.configurations.getByName("shadowBundle"))
        archiveClassifier.set(null)
    }
}

publisher {
    setLoaders(ModLoader.NEOFORGE)
    curseDepends.required.set(mutableListOf("terrablender-neoforge", "geckolib", "oh-the-trees-youll-grow"))
    modrinthDepends.required.set(mutableListOf("terrablender", "geckolib", "oh-the-trees-youll-grow"))
    curseDepends.optional.set(mutableListOf("wthit-forge"))
}