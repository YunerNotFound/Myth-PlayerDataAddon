package com.playerdataaddon;

import com.playerdataaddon.Listener.SyncCMIPlayerNameplate;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerDataAddon extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SyncCMIPlayerNameplate(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
