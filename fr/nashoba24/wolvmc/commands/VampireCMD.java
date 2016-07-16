package fr.nashoba24.wolvmc.commands;

import me.libraryaddict.disguise.DisguiseAPI;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.races.Vampire;

public class VampireCMD implements CommandExecutor {

	public static String becomeHuman = ChatColor.GREEN + "You are became a human again!";
	public static String becomeVampire = ChatColor.GREEN + "You switched to vampire mode!";
	public static String noVampire = ChatColor.RED + "Your race is not vampire!";
	
	   @Override
	    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		   if(!Vampire.vampireCMD) {
			   sender.sendMessage(ChatColor.RED + "That power has been disabled");
			   return true;
		   }
		if(sender instanceof Player) {
			Player p = (Player) sender;
				if(WolvMC.getRace(p.getName()).equals("vampire")) {
					if(Vampire.getVampMode(p.getName())) {
						p.removePotionEffect(PotionEffectType.JUMP);
						p.removePotionEffect(PotionEffectType.HUNGER);
						p.removePotionEffect(PotionEffectType.SPEED);
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, Vampire.effSpeed - 1));
						p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, Vampire.effStrength - 1));
						p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2147483647, Vampire.effDiging - 1));
						Vampire.setVampMode(p.getName(), false);
						p.sendMessage(becomeHuman);
						p.setHealthScale(20);
					}
					else {
						DisguiseAPI.undisguiseToAll(p);
						p.setAllowFlight(false);
						p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2147483647, 2));
						p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, Vampire.effStrength - 1));
						p.removePotionEffect(PotionEffectType.SPEED);
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, Vampire.effSpeed + 2));
						if(!WolvMC.hasFinishMission("vampire.4", p.getName())) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 2147483647, 1));
						}
						p.sendMessage(becomeVampire);
						Vampire.setVampMode(p.getName(), true);
						p.setHealthScale(20);
						if(Vampire.getBrumeMode(p.getName())==true) {
							p.removePotionEffect(PotionEffectType.INVISIBILITY);
							Vampire.setBrumeMode(p.getName(), false);
						}
					}
					return true;
				}
				else {
					p.sendMessage(noVampire);
					return true;
				}
		}
		return false;
	   } 
}
