package fr.nashoba24.wolvmc.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.races.Daemon;

public class Nether implements CommandExecutor {
	
	public static String already = ChatColor.RED + "You are already in the Nether!";
	public static String worldN = "world_nether";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		   if(!Daemon.tpNether) {
			   sender.sendMessage(ChatColor.RED + "That power has been disabled");
			   return true;
		   }
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(WolvMC.getRace(p.getName()).equals("daemon")) {
				if(!p.getWorld().getName().equals(worldN)) {
					Location loc = p.getLocation();
					loc.setWorld(WolvMC.getPlugin(WolvMC.class).getServer().getWorld(worldN));
					loc.setX(WolvMC.getPlugin(WolvMC.class).getServer().getWorld(worldN).getSpawnLocation().getX());
					loc.setY(WolvMC.getPlugin(WolvMC.class).getServer().getWorld(worldN).getSpawnLocation().getY());
					loc.setZ(WolvMC.getPlugin(WolvMC.class).getServer().getWorld(worldN).getSpawnLocation().getZ());
					p.teleport(loc);
				}
				else {
					p.sendMessage(already);
				}
			}
		}
		return true;
	}

}
