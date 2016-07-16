package fr.nashoba24.wolvmc.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.nashoba24.wolvmc.WolvMC;

public class Missions implements CommandExecutor {
	
	public static String header = ChatColor.GOLD + "*************** MISSIONS ******************";
	public static String footer = ChatColor.GOLD + "*******************************************";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			ArrayList<String> arr = WolvMC.getMissionsForRace(WolvMC.getRace(p.getName()));
			p.sendMessage(header);
			for(String s : arr) {
				if(WolvMC.hasFinishMission(s, p.getName())) {
					String fin = WolvMC.getMissionFinishMsg(s);
					p.sendMessage(ChatColor.GOLD + "[" + ChatColor.GREEN + "✓" + ChatColor.GOLD + "] " + ChatColor.YELLOW + fin);
				}
				else {
					String descr = WolvMC.getMissionDescr(s);
					int goal = WolvMC.getMissionGoal(s).intValue();
					int curr = WolvMC.getPlayerMission(s, p.getName()).intValue();
					p.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "✗" + ChatColor.GOLD + "] " + ChatColor.GOLD + descr + ": " + curr + "/" + goal);
				}
			}
			p.sendMessage(footer);
		}
		return true;
	}
}