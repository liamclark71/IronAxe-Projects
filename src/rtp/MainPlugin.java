package rtp;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class MainPlugin extends JavaPlugin {
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	this.saveDefaultConfig();
    	getServer().getPluginManager().registerEvents(new MyListener(), this);
    	this.getCommand("randomtp").setExecutor(new randomtp(this));
    	this.getCommand("rtp").setExecutor(new randomtp(this));
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
    	
    }
    
}