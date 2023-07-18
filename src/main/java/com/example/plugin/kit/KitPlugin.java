package com.example.plugin.kit;

import com.example.plugin.kit.command.KitCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class KitPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Registra il comando /kit
        KitCommand kitCommand = new KitCommand(this, getConfig());
        getCommand("kit").setExecutor(kitCommand);

        // Registra il listener per l'evento di clic sull'inventario
        getServer().getPluginManager().registerEvents(kitCommand, this);

        // Salva il file di configurazione predefinito se non esiste
        saveDefaultConfig();

        // Messaggio di avvio del plugin
        getLogger().info("Il plugin Kit è stato abilitato.");
    }

    @Override
    public void onDisable() {
        // Messaggio di disabilitazione del plugin
        getLogger().info("Il plugin Kit è stato disabilitato.");
    }
}
