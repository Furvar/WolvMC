package fr.nashoba24.wolvmc.commands;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;

import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.races.Daemon;

public class Pig implements CommandExecutor {
	
	public static String normal = ChatColor.GREEN + "You are now normal again!";
	public static String pig = ChatColor.GREEN + "You are now a pig!";
	public static String zpig = ChatColor.GREEN + "You are now a zombie pigman!";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		   if(!Daemon.pigTransf) {
			   sender.sendMessage(ChatColor.RED + "That power has been disabled");
			   return true;
		   }
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(WolvMC.getRace(p.getName()).equals("daemon")) {
				if(DisguiseAPI.isDisguised(p)) {
					if(DisguiseAPI.getDisguise(p).getType()==DisguiseType.PIG || DisguiseAPI.getDisguise(p).getType()==DisguiseType.PIG_ZOMBIE) {
						DisguiseAPI.undisguiseToAll(p);
						p.sendMessage(normal);
						return true;
					}
				}
				if(p.getWorld().getEnvironment()==Environment.NETHER) {
					MobDisguise mobDisguise = new MobDisguise(DisguiseType.PIG_ZOMBIE);
					DisguiseAPI.disguiseToAll(p, mobDisguise);
					p.sendMessage(zpig);
				}
				else {
					MobDisguise mobDisguise = new MobDisguise(DisguiseType.PIG);
					DisguiseAPI.disguiseToAll(p, mobDisguise);
					p.sendMessage(pig);
				}
			}
		}
		return true;
	}

}
