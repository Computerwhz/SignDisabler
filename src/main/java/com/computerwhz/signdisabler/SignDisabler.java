package com.computerwhz.signdisabler;

import com.computerwhz.signdisabler.commands.SignDisablerCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.HangingSign;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class SignDisabler extends JavaPlugin implements Listener {

    private static SignDisabler instance;

    private File configFile;
    private FileConfiguration config;
    private File dataFile;
    private FileConfiguration data;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        configFile = new File(getDataFolder(), "config.yml");
        dataFile = new File(getDataFolder(), "data.yml");
        data = YamlConfiguration.loadConfiguration(dataFile);
        getServer().getPluginManager().registerEvents(this, this);
        SignDisablerCommand command = new SignDisablerCommand();
        getCommand("signdisabler").setExecutor(command);
        getCommand("signdisabler").setTabCompleter(command);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void OnSignChange(SignChangeEvent event){
        Player player = event.getPlayer();
        if (isPlayerDisabled(player)){
            player.sendMessage(ChatColor.RED + "You are not allowed to complete this action");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void OnSignPlace(BlockPlaceEvent event) {
        BlockData data = event.getBlock().getBlockData();
        if (data instanceof Sign
                || data instanceof WallSign
                || data instanceof HangingSign
                || data instanceof WallHangingSign)
        {
            Player player = event.getPlayer();
            if (isPlayerDisabled(player)){
                player.sendMessage(ChatColor.RED + "You are not allowed to complete this action");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void OnSignBreak(BlockBreakEvent event){
        BlockData data = event.getBlock().getBlockData();
        if (data instanceof Sign
                || data instanceof WallSign
                || data instanceof HangingSign
                || data instanceof WallHangingSign)
        {
        Player player = event.getPlayer();
            if (isPlayerDisabled(player)){
                player.sendMessage(ChatColor.RED + "You are not allowed to complete this action");
                event.setCancelled(true);
            }
        }
    }

    public boolean isPlayerDisabled(Player player){
        if (!isPluginEnabled()) { return false; }

        if (player.hasPermission("signdisabler.bypass")) return false;
        
        if (isGloballyDisabled()) return true;

        List<String> disabledUUIDs = getData().getStringList("disabled-players");
        return disabledUUIDs.contains(player.getUniqueId().toString());
    }

    public void addDisabledPlayer(Player player){
        List<String> disabledUUIDs = data.getStringList("disabled-players");
        String UUID = player.getUniqueId().toString();

        if (disabledUUIDs.contains(UUID)) {
            return;
        }
        disabledUUIDs.add(UUID);
        data.set("disabled-players", disabledUUIDs);
        saveData();
    }

    public List<Player> getDisabledPlayers() {
        List<String> disabledUUIDs = data.getStringList("disabled-players");
        List<Player> players = new ArrayList<>();

        for (String s : disabledUUIDs) {
            Player player = Bukkit.getPlayer(UUID.fromString(s));

            if (player != null) {
                players.add(player);
            }
        }

        return players;
    }

    public void removeDisabledPlayer(Player player) {
        FileConfiguration data = getData();
        List<String> disabledUUIDs = data.getStringList("disabled-players");

        String uuid = player.getUniqueId().toString();

        if (disabledUUIDs.contains(uuid)) {
            disabledUUIDs.remove(uuid);
            data.set("disabled-players", disabledUUIDs);
            saveData();
        }
    }

    public boolean isPluginEnabled() {
        return getData().getBoolean("enabled");
    }

    public void setPluginEnabled(boolean value) {
        getData().set("enabled", value);
    }

    public boolean isGloballyDisabled(){
        return getData().getBoolean("globally-disabled");
    }

    public void setGloballyDisabled(boolean value){
        data.set("globally-disabled", value);
        saveData();
    }

    public FileConfiguration getConfig(){
        return this.config;
    }

    public FileConfiguration getData(){
        return this.data;
    }

    public void saveData (){
        try {
            this.data.save(dataFile);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Could not save data");
            Bukkit.getLogger().severe(e.getMessage());
            Bukkit.getLogger().severe(e.getStackTrace().toString());
        }
    }

    public void saveConfig(){
        try {
            this.config.save(configFile);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Could not save config");
            Bukkit.getLogger().severe(e.getMessage());
            Bukkit.getLogger().severe(e.getStackTrace().toString());
        }
    }

    public static SignDisabler getInstance(){
        return instance;
    }
}
