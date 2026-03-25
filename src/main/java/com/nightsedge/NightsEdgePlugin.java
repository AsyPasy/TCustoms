package com.nightsedge;

import com.nightsedge.commands.NightsEdgeCommand;
import com.nightsedge.listeners.CombatListener;
import com.nightsedge.listeners.GlowListener;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * NightsEdgePlugin – entry point.
 *
 * Responsibilities:
 *   • Register event listeners
 *   • Register the /nightsedge command
 *   • Create the purple scoreboard team used for the held-glow effect
 */
public final class NightsEdgePlugin extends JavaPlugin {

    /** Singleton reference used by helper classes that have no direct plugin handle. */
    private static NightsEdgePlugin instance;

    /** Name of the scoreboard team that makes the player glow purple while holding the sword. */
    public static final String GLOW_TEAM = "ne_glow";

    @Override
    public void onEnable() {
        instance = this;

        // Persist default config values if config.yml is absent
        saveDefaultConfig();

        // ── Scoreboard glow team ──────────────────────────────────────────────
        setupGlowTeam();

        // ── Listeners ────────────────────────────────────────────────────────
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new GlowListener(this),   this);

        // ── Commands ─────────────────────────────────────────────────────────
        NightsEdgeCommand cmd = new NightsEdgeCommand(this);
        getCommand("nightsedge").setExecutor(cmd);
        getCommand("nightsedge").setTabCompleter(cmd);

        getLogger().info("Night's Edge is active – the void awakens.");
    }

    @Override
    public void onDisable() {
        // Remove the temporary scoreboard team on shutdown
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = sb.getTeam(GLOW_TEAM);
        if (team != null) team.unregister();

        getLogger().info("Night's Edge disabled.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private void setupGlowTeam() {
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();

        Team existing = sb.getTeam(GLOW_TEAM);
        if (existing != null) existing.unregister();          // clean slate on reload

        Team team = sb.registerNewTeam(GLOW_TEAM);
        team.color(NamedTextColor.DARK_PURPLE);               // purple outline
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
    }

    public static NightsEdgePlugin getInstance() {
        return instance;
    }
}
