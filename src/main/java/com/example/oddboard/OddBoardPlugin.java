package com.example.oddboard;

import org.bukkit.plugin.java.JavaPlugin;

public class OddBoardPlugin extends JavaPlugin {

    private static OddBoardPlugin instance;
    private AbilityManager abilityManager;

    @Override
    public void onEnable() {
        instance = this;
        abilityManager = new AbilityManager();

        // Register event listeners
        getServer().getPluginManager().registerEvents(new OddBoardItem(), this);
        getServer().getPluginManager().registerEvents(new GUIListener(), this);

        getLogger().info("OddBoardPlugin enabled!");
    }

    @Override
    public void onDisable() {
        // Clean up any running ability tasks
        if (abilityManager != null) {
            abilityManager.shutdown();
        }
        getLogger().info("OddBoardPlugin disabled!");
    }

    public static OddBoardPlugin getInstance() {
        return instance;
    }

    public AbilityManager getAbilityManager() {
        return abilityManager;
    }
}
