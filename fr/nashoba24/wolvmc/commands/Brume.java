package fr.nashoba24.wolvmc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.races.Vampire;

public class Brume implements CommandExecutor {

@Override
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	   if(!Vampire.brumePower) {
		   sender.sendMessage(ChatColor.RED + "That power has been disabled");
		   return true;
	   }
	if(sender instanceof Player) {
		Player p = (Player) sender;
			if(WolvMC.getRace(p.getName()).equals("vampire")) {
				if(!Vampire.getVampMode(p.getName())) {
					if(Vampire.getBrumeMode(p.getName())) {
						Vampire.setBrumeMode(p.getName(), false);
						p.removePotionEffect(PotionEffectType.INVISIBILITY);
						p.sendMessage(ChatColor.GREEN + "Vous avez quitté le mode brume!");
					}
					else {
						Vampire.setBrumeMode(p.getName(), true);
						p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2147483647, 1));
						p.sendMessage(ChatColor.GREEN + "Vous vous êtes transformé en brume!");
					}
				}
				else {
					p.sendMessage(ChatColor.RED + "Vous ne pouvez pas vous transformer en brume si vous êtes en mode vampire!");
				}
				return true;
			}
			else {
				p.sendMessage(ChatColor.RED + "Vous n'êtes pas un vampire!");
				return true;
			}
	}
	return false;
}
}
