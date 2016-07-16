package fr.nashoba24.wolvmc.commands;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.utils.TitleAPI;

public class Dracorage implements CommandExecutor {
	
	static HashMap<String, Long> rage = new HashMap<String, Long>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(WolvMC.getRace(p.getName()).equals("draco")) {
			       int cooldownTime = 300;
			        if(rage.containsKey(p.getName())) {
			            long secondsLeft = ((rage.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
			            if(secondsLeft>0) {
			            	int seconds = (int) secondsLeft;
			            	p.sendMessage(WolvMC.msgCooldown(seconds));
			                return true;
			            }
			        }
			        List<Entity> list = p.getNearbyEntities(10, 10, 10);
			        for(Entity ent : list) {
			        	ent.setFireTicks(200);
			        	if(ent instanceof Player) {
			        		TitleAPI.sendTitle((Player) ent, 10, 40, 10, "&4DRACORAGE!", "");
			        	}
			        }
			        rage.put(p.getName(), System.currentTimeMillis());
			}
		}
		return true;
	}
}
