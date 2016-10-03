package rtp;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class randomtp implements CommandExecutor{
	World w = Bukkit.getWorld("world");
	JavaPlugin Main;
	public HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	
	public randomtp(JavaPlugin Main){
		this.Main = Main;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command comm, String first, String[] params) {
		if (!(sender instanceof Entity)){
			return false;
		}
		int cooldownTime = Main.getConfig().getInt("cooldown");
		Player player = (Player) sender;
		if (!(player.getWorld().equals(w))){
			player.sendMessage(ChatColor.DARK_RED + "You can only use this in the Overworld!");
			return true;
		}
		if (cooldowns.containsKey(player.getName())){
			 long secondsLeft = ((cooldowns.get(sender.getName())/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
			 if (secondsLeft > 0){
				 int minutesLeft = (int)(secondsLeft/60);
				 secondsLeft = secondsLeft%60;
				 player.sendMessage(ChatColor.DARK_RED + "You can't use that for another " + minutesLeft + " minutes and " + secondsLeft + " seconds!");
				 return true;
			 }
		}
		double radius = 0;
		if (params.length > 1)
			return false;
		if (params.length == 1){
			if (params[0].equalsIgnoreCase("small") || params[0].equalsIgnoreCase("s")){
				radius = Main.getConfig().getDouble("radius.small")+ .5;
			}
			if (params[0].equalsIgnoreCase("medium") || params[0].equalsIgnoreCase("m") || params[0].equalsIgnoreCase("med")){
				radius = Main.getConfig().getDouble("radius.medium")+ .5;
			}
			if (params[0].equalsIgnoreCase("large") || params[0].equalsIgnoreCase("l")){
				radius = Main.getConfig().getDouble("radius.large")+ .5;
			}
		}
		if (radius == 0){
			radius = Main.getConfig().getDouble("radius.medium")+ .5;
		}
		double minRadius = Main.getConfig().getDouble("radius.minRad");
		double startingX = Main.getConfig().getDouble("center.x");
		double startingY;
		double startingZ = Main.getConfig().getDouble("center.z");
		Location newLoc = new Location(w, startingX, 0, startingZ);
		Location baseLoc = new Location(w, startingX, 0, startingZ);
		while (newLoc.distance(baseLoc) > radius || newLoc.distance(baseLoc) < minRadius){			
			int addX = (int) ((Math.random()-.5)*radius*2);
			int addZ = (int) ((Math.random()-.5)*radius*2);
			//Defining the variables to be added to the current coordinates
			startingX = ((int) player.getLocation().getBlockX());
			startingZ = ((int) player.getLocation().getBlockZ());
			startingY = player.getLocation().getBlockY();
			//current coords
			startingX += addX+.5;
			startingZ += addZ+.5;
			//adding the coords (plus .5 so they're in the middle of a block)
			newLoc = new Location(w, startingX, startingY, startingZ);
			if (surface(startingX, startingZ) == -1){
				newLoc = baseLoc;
			}
		}
			startingY = surface(startingX, startingZ) + .1;
			newLoc = new Location(w, startingX, startingY, startingZ);
			player.teleport(newLoc);
			cooldowns.put(player.getName(), System.currentTimeMillis());
			return true;
	}
	
	//Finds the highest non-air block
	private int surface(double startingX, double startingZ){
		for (int y = 255; y > 0; y--){
			Location possibleLoc = new Location(w, startingX, y, startingZ);
			if (possibleLoc.getBlock().getType() != Material.AIR){
				if (possibleLoc.getBlock().getType() == Material.LAVA ||
						possibleLoc.getBlock().getType() == Material.FIRE ||
						possibleLoc.getBlock().getType() == Material.STATIONARY_LAVA ||
						possibleLoc.getBlock().getType() == Material.MAGMA)
					return -1;
				if (Main.getConfig().getBoolean("tpintowater"))
					if (possibleLoc.getBlock().getType() == Material.WATER ||
							possibleLoc.getBlock().getType() == Material.STATIONARY_WATER)
						return -1;
				return y+1;
			}
		}
		return -1;
	}
}
