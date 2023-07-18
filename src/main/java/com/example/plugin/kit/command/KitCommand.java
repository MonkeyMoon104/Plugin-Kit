package com.example.plugin.kit.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.InventoryHolder;


public class KitCommand implements CommandExecutor, Listener {

    private final JavaPlugin plugin;
    private final FileConfiguration config;

    public KitCommand(JavaPlugin plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Questo comando può essere eseguito solo da un giocatore.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            openKitMenu(player);
            return true;
        }

        String kitName = args[0];
        String permission = "kit." + kitName;

        if (!player.hasPermission(permission)) {
            player.sendMessage("Non hai il permesso per accedere a questo kit.");
            return true;
        }

        ItemStack helmet = getItemStackFromConfig("kit-" + kitName + ".helmet");
        ItemStack chestplate = getItemStackFromConfig("kit-" + kitName + ".chestplate");
        ItemStack leggings = getItemStackFromConfig("kit-" + kitName + ".leggings");
        ItemStack boots = getItemStackFromConfig("kit-" + kitName + ".boots");
        ItemStack sword = getItemStackFromConfig("kit-" + kitName + ".sword");

        // Assegna gli oggetti ai giocatori
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
        player.getInventory().setItemInMainHand(sword);

        player.sendMessage("Hai ricevuto il kit " + kitName + ".");
        return true;
    }

    private void openKitMenu(Player player) {
        Inventory gui = Bukkit.createInventory(player, InventoryType.HOPPER, "Kit Selection");

        ItemStack baseKitIcon = getKitIconFromConfig("kit-base");
        gui.setItem(2, baseKitIcon);

        ItemStack vipKitIcon = getKitIconFromConfig("kit-vip");
        gui.setItem(6, vipKitIcon);

        player.openInventory(gui);
    }


    private ItemStack getKitIconFromConfig(String kitName) {
        if (config.contains(kitName)) {
            String materialName = config.getString(kitName + ".icon.material");
            if (materialName != null) {
                Material material = Material.matchMaterial(materialName);
                if (material != null) {
                    ItemStack itemStack = new ItemStack(material);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    String displayName = config.getString(kitName + ".icon.display-name");
                    if (displayName != null) {
                        itemMeta.setDisplayName(displayName);
                    }
                    itemStack.setItemMeta(itemMeta);
                    return itemStack;
                }
            }
        }
        return new ItemStack(Material.AIR);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        InventoryView inventoryView = event.getView();
        Inventory inventory = inventoryView.getTopInventory();

        if (inventoryView.getTitle().equals("Kit Selection")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                if (event.getRawSlot() == 2) {
                    // Clic sull'icona del kit base
                    player.sendMessage("Hai cliccato sul kit base");
                    player.closeInventory();
                    assignKit(player, "kit-base");
                } else if (event.getRawSlot() == 6) {
                    // Clic sull'icona del kit VIP
                    player.sendMessage("Hai cliccato sul kit VIP");
                    player.closeInventory();
                    assignKit(player, "kit-vip");
                }
            }
        }
    }

    private void assignKit(Player player, String kitName) {
        String permission = "kit." + kitName;

        if (!player.hasPermission(permission)) {
            player.sendMessage("Non hai il permesso per accedere a questo kit.");
            return;
        }

        ItemStack helmet = getItemStackFromConfig("kit-" + kitName + ".helmet");
        ItemStack chestplate = getItemStackFromConfig("kit-" + kitName + ".chestplate");
        ItemStack leggings = getItemStackFromConfig("kit-" + kitName + ".leggings");
        ItemStack boots = getItemStackFromConfig("kit-" + kitName + ".boots");
        ItemStack sword = getItemStackFromConfig("kit-" + kitName + ".sword");

        // Assegna gli oggetti ai giocatori
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
        player.getInventory().setItemInMainHand(sword);

        player.sendMessage("Hai ricevuto il kit " + kitName + ".");
    }

    private ItemStack getItemStackFromConfig(String path) {
        if (config.contains(path)) {
            String materialName = config.getString(path + ".material");
            if (materialName != null) {
                Material material = Material.matchMaterial(materialName);
                if (material != null) {
                    ItemStack itemStack = new ItemStack(material);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    String displayName = config.getString(path + ".display-name");
                    if (displayName != null) {
                        itemMeta.setDisplayName(displayName);
                    }
                    itemStack.setItemMeta(itemMeta);
                    return itemStack;
                }
            }
        }
        return new ItemStack(Material.AIR);
    }
}
