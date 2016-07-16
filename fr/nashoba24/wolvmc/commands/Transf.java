package fr.nashoba24.wolvmc.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.races.Werewolf;

public class Transf implements CommandExecutor {

	public static String tryTransf = ChatColor.RED + "You tried to become a werewolf but you failed!";
	
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
					double tot = Werewolf.getKills(p.getName());
					if(tot==0) {
						tot = 1;
					}
					tot = (WolvMC.getTime(p.getName()) / tot) / WerewolfStats.diviser;
					World w = WolvMC.getPlugin(WolvMC.class).getServer().getWorld("world");
					int time = (int) w.getTime();
					if(tot>140 || (tot>130 && time>1000) || (tot>120 && time>2000) || (tot>110 && time>3000) || (tot>100 && time>4000) || (tot>90 && time>5000)|| (tot>80 && time>6000)|| (tot>70 && time>7000)|| (tot>60 && time>8000)|| (tot>50 && time>9000) || (tot>40 && time>10000)|| (tot>30 && time>11000) || (tot>20 && time>12000)|| (tot>10 && time>13000) || time>14000) {
						//DISGUISE
						Werewolf.setTransf(p.getName(), true);
						Werewolf.applySpeedAndStrength(p);
						p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2147483647, 1));
						//ALPHA MEUTE HEALTH BOOST
						if(WolvMC.hasFinishMission("werewolf.1", p.getName())) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2147483647, 1));
						}
						if(WolvMC.hasFinishMission("werewolf.2", p.getName())) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 1));
						}
						if(args.length==0) {
							p.sendMessage(Werewolf.transfMSG);
						}
						return true;
					}
					if(args.length==0) {
						p.sendMessage(tryTransf);
					}
				}
				return true;
		}
		return false;
	}
}
