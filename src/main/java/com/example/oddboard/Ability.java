package com.example.oddboard;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public interface Ability {
    BukkitTask start(Player player);
}
