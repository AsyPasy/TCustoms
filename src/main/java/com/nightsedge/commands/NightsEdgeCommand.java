package com.nightsedge.commands;

import com.nightsedge.NightsEdgePlugin;
import com.nightsedge.items.NightsEdgeSword;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

/**
 * Handles the {@code /nightsedge [player]} command.
 *
 * <pre>
 *   /nightsedge            – gives the sword to the command sender (must be a player)
 *   /nightsedge <player>   – gives the sword to the named player  (requires nightsedge.give)
 * </pre>
 *
 * Requires the permission node {@code nightsedge.give} (default: op).
 */
public final class NightsEdgeCommand implements CommandExecutor, TabCompleter {

    @SuppressWarnings("unused")
    private final NightsEdgePlugin plugin;

    public NightsEdgeCommand(NightsEdgePlugin plugin) {
        this.plugin = plugin;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Command execution
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {

        // ── Permission check ──────────────────────────────────────────────
        if (!sender.hasPermission("nightsedge.give")) {
            sender.sendMessage(Component.text("✖ You lack permission to wield the Night's Edge.")
                    .color(NamedTextColor.RED));
            return true;
        }

        // ── Resolve target ────────────────────────────────────────────────
        Player target;

        if (args.length >= 1) {
            // Admin giving to another player: /nightsedge <name>
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(Component.text("✖ Player not found: " + args[0])
                        .color(NamedTextColor.RED));
                return true;
            }
        } else {
            // Self-give: sender must be a player
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Usage: /nightsedge <player>");
                return true;
            }
            target = player;
        }

        // ── Give the sword ────────────────────────────────────────────────
        ItemStack sword = NightsEdgeSword.create();
        target.getInventory().addItem(sword);

        // Notify receiver
        target.sendMessage(
                Component.text("✦ The Night's Edge materialises in your hands.")
                        .color(NamedTextColor.DARK_PURPLE)
        );
        target.sendMessage(
                Component.text("  Forged from four elemental blades, the void obeys.")
                        .color(NamedTextColor.GRAY)
        );

        // Notify admin (if different from receiver)
        if (!sender.equals(target)) {
            sender.sendMessage(
                    Component.text("✦ Gave Night's Edge to " + target.getName() + ".")
                            .color(NamedTextColor.LIGHT_PURPLE)
            );
        }

        return true;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Tab completion
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command,
                                      String alias, String[] args) {
        if (!sender.hasPermission("nightsedge.give")) return Collections.emptyList();

        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(prefix))
                    .toList();
        }

        return Collections.emptyList();
    }
}
