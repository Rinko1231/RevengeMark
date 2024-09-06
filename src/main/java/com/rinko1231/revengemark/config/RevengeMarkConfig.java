package com.rinko1231.revengemark.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class RevengeMarkConfig
{
    private static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final RevengeMarkConfig INSTANCE = new RevengeMarkConfig();

    public static ForgeConfigSpec.IntValue RenameDuration;

    public static ForgeConfigSpec.ConfigValue<String> KillerCustomNameL;
    public static ForgeConfigSpec.ConfigValue<String> KillerCustomNameR;

    public static ForgeConfigSpec.BooleanValue isGlowing;

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> mobBlacklist;


    static
    {
        BUILDER.push("Murderer Mob Rename");

        mobBlacklist = BUILDER
                .comment("A list of mobs that will not get renamed")
                .defineList("Mob Blacklist", List.of("minecraft:wither","minecraft:ender_dragon","minecraft:player","cataclysm:ender_guardian","cataclysm:ignis","cataclysm:netherite_monstrosity","cataclysm:the_harbinger","cataclysm:the_leviathan","cataclysm:ancient_remnant","twilightforest:naga","twilightforest:snow_queen","twilightforest:yeti_alpha","twilightforest:ur_ghast","twilightforest:minoshroom"),
                        element -> element instanceof String);

        KillerCustomNameL = BUILDER
                .comment("The custom left part of name to give to entities that kill a player")
                .define("killerCustomNameL", ""); // 设置默认值

        KillerCustomNameR = BUILDER
                .comment("The custom right part of name to give to entities that kill a player")
                .define("killerCustomNameR", "Killer"); // 设置默认值

        RenameDuration = BUILDER
                .comment("Duration in ticks to keep unnamed Murderer Mob renamed")
                .defineInRange("RenameDuration", 3600, 0, 86400);

        isGlowing = BUILDER
                .comment("Whether the killer entity should glow after killing a player")
                .define("isGlowing", true); // 设置默认值


        SPEC = BUILDER.build();
    }

    // Getters

    public static int getConfigRenameDuration() {
        return RenameDuration.get();
    }
    public static String getKillerCustomNameL() {
        return KillerCustomNameL.get();
    }
    public static String getKillerCustomNameR() {
        return KillerCustomNameR.get();
    }

    public static boolean getIsGlowing() {
        return isGlowing.get();
    }
    // Getters
    public List<? extends String> getMobBlacklist()
    {   return mobBlacklist.get();
    }

    public static void setup()
    {
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path csConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "revengemark");

        // Create the config folder
        try
        {
            Files.createDirectory(csConfigPath);
        }
        catch (Exception e)
        {
            // Do nothing
        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "revengemark/main.toml");
    }

    public static RevengeMarkConfig getInstance()
    {
        return INSTANCE;
    }

    public void save() {
        SPEC.save();
    }
}
