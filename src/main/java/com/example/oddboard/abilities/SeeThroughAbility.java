package com.example.oddboard.abilities;

import com.example.oddboard.Ability;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;

public class SeeThroughAbility implements Ability {

    private static final int RADIUS = 30;
    private static final int DURATION_SECONDS = 30;
    private static final int EFFECT_DURATION_TICKS = DURATION_SECONDS * 20; // 600 ticks

    @Override
    public BukkitTask start(Player player) {
        // Run a repeating task that applies the glow effect to nearby enemies every second.
        // This ensures the effect stays applied for the entire duration even if mobs move in/out.
        return new BukkitRunnable() {
            int elapsed = 0;

            @Override
            public void run() {
                if (elapsed >= EFFECT_DURATION_TICKS) {
                    // Ability finished
                    player.sendMessage(org.bukkit.ChatColor.RED + "See-through ability has worn off.");
                    this.cancel();
                    return;
                }

                Location playerLoc = player.getLocation();
                Collection<Entity> nearby = player.getWorld().getNearbyEntities(playerLoc, RADIUS, RADIUS, RADIUS);
                for (Entity entity : nearby) {
                    if (entity instanceof LivingEntity && isEnemy((LivingEntity) entity, player)) {
                        // Apply glowing for 2 seconds, refreshing every second
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 40, 0, false, false, true));
                    }
                }

                elapsed += 20; // This runs every second (20 ticks)
            }
        }.runTaskTimer(OddBoardPlugin.getInstance(), 0L, 20L);
    }

    private boolean isEnemy(LivingEntity entity, Player player) {
        // Consider monsters as enemies
        if (entity instanceof Monster) {
            return true;
        }
        // Optionally, also consider other players if they are in a different team or if PvP is enabled
        // For simplicity, we only target monsters.
        return false;
    }
}
