package com.example.oddboard;

import com.example.oddboard.abilities.SeeThroughAbility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GUIListener implements Listener {

    private static final String GUI_TITLE = ChatColor.DARK_PURPLE + "Odd Board Abilities";

    public static void openAbilityGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, GUI_TITLE);

        // See-through ability item
        ItemStack seeThroughItem = new ItemStack(Material.SPYGLASS);
        ItemMeta seeThroughMeta = seeThroughItem.getItemMeta();
        seeThroughMeta.setDisplayName(ChatColor.GREEN + "See-through");
        seeThroughMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to make all enemies within 30 blocks glow for 30 seconds"));
        seeThroughItem.setItemMeta(seeThroughMeta);
        gui.setItem(0, seeThroughItem);

        // Fill the rest with black glass panes (optional)
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = 1; i < 9; i++) {
            gui.setItem(i, filler);
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        if (!event.getView().getTitle().equals(GUI_TITLE)) return;

        event.setCancelled(true); // Prevent taking items

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        ItemStack clicked = event.getCurrentItem();
        if (!clicked.hasItemMeta()) return;

        String displayName = clicked.getItemMeta().getDisplayName();

        if (displayName.equals(ChatColor.GREEN + "See-through")) {
            // Close the GUI
            player.closeInventory();

            // Activate the ability
            SeeThroughAbility ability = new SeeThroughAbility();
            OddBoardPlugin.getInstance().getAbilityManager().activateAbility(player, ability);
            player.sendMessage(ChatColor.GREEN + "See-through activated! Enemies within 30 blocks will glow for 30 seconds.");
        }
    }
}
