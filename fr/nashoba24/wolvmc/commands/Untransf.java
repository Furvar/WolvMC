package fr.nashoba24.wolvmc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.races.Werewolf;

public class Untransf implements CommandExecutor {

	public static String notWW = ChatColor.RED + "You are not transformed!";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
				if(args.length==1) {
					if(!args[0].equalsIgnoreCase("silent")) {
						return false;
					}
				}
				if(WolvMC.getRace(p.getName()).equalsIgnoreCase("werewolf")) {
					if(Werewolf.getTransf(p.getName())) {
						//DisguiseAPI.undisguiseToAll(p);
						Werewolf.setTransf(p.getName(), false);
						p.removePotionEffect(PotionEffectType.SPEED);
						p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
						p.removePotionEffect(PotionEffectType.HEALTH_BOOST);
						p.removePotionEffect(PotionEffectType.JUMP);
						p.removePotionEffect(PotionEffectType.FAST_DIGGING);
						p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
						if(args.length==0) {
							p.sendMessage(Werewolf.untransfMSG);
						}
					}
					else {
						if(args.length==0) {
							p.sendMessage(notWW);
						}
					}
				}
		}
		return true;
	}
}
