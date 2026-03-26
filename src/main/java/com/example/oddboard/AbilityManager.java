package com.example.oddboard;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityManager {

    private final Map<UUID, BukkitTask> activeAbilities = new HashMap<>();

    public void activateAbility(Player player, Ability ability) {
        // Deactivate any currently active ability for this player
        deactivateAbility(player);

        // Start the ability
        BukkitTask task = ability.start(player);
        activeAbilities.put(player.getUniqueId(), task);
    }

    public void deactivateAbility(Player player) {
        BukkitTask task = activeAbilities.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }

    public void shutdown() {
        for (BukkitTask task : activeAbilities.values()) {
            task.cancel();
        }
        activeAbilities.clear();
    }
}
