package fr.nashoba24.wolvmc.races;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.events.WolvMCHumanChangeEvent;

public class Human implements Listener {
	
	public static boolean enabled = true;
	
	@EventHandler
	public void onKillMob(EntityDeathEvent e) {
		if(e.getEntity().getKiller()!=null) {
			if(e.getEntity().getKiller() instanceof Player) {
				Player p = e.getEntity().getKiller();
				if(WolvMC.getRace(p.getName()).equals("human")) {
					if(WolvMC.getPlayerMission("human.strength1", p.getName()) + 1.00>=WolvMC.getMissionGoal("human.strength1") && !WolvMC.hasFinishMission("human.strength1", p.getName())) {
						WolvMC.getPlugin(WolvMC.class).getServer().getPluginManager().callEvent(new WolvMCHumanChangeEvent(p));
					}
					if(WolvMC.getPlayerMission("human.strength2", p.getName()) + 1.00>=WolvMC.getMissionGoal("human.strength2") && !WolvMC.hasFinishMission("human.strength2", p.getName())) {
						WolvMC.getPlugin(WolvMC.class).getServer().getPluginManager().callEvent(new WolvMCHumanChangeEvent(p));
					}
					if(WolvMC.getPlayerMission("human.strength3", p.getName()) + 1.00>=WolvMC.getMissionGoal("human.strength3") && !WolvMC.hasFinishMission("human.strength3", p.getName())) {
						WolvMC.getPlugin(WolvMC.class).getServer().getPluginManager().callEvent(new WolvMCHumanChangeEvent(p));
					}
					WolvMC.addNbToMission("human.strength1", p.getName(), 1.00);
					WolvMC.addNbToMission("human.strength2", p.getName(), 1.00);
					WolvMC.addNbToMission("human.strength3", p.getName(), 1.00);
				}
			}
		}
	}
	
	@EventHandler
	public void onChange(WolvMCHumanChangeEvent e) {
		speed(e.getPlayer());
		strength(e.getPlayer());
		jump(e.getPlayer());
		mining(e.getPlayer());
		resistance(e.getPlayer());
	}
	
	public static void speed(Player p) {
		p.removePotionEffect(PotionEffectType.SPEED);
		if(WolvMC.hasFinishMission("human.speed3", p.getName())) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 2), true);
		}
		else if(WolvMC.hasFinishMission("human.speed2", p.getName())) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 1), true);
		}
		else if(WolvMC.hasFinishMission("human.speed1", p.getName())) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 0), true);
		}
	}
	
	public static void strength(Player p) {
		p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		if(WolvMC.hasFinishMission("human.strength3", p.getName())) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, 2), true);
		}
		else if(WolvMC.hasFinishMission("human.strength2", p.getName())) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, 1), true);
		}
		else if(WolvMC.hasFinishMission("human.strength1", p.getName())) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, 0), true);
		}
	}
	
	public static void jump(Player p) {
		p.removePotionEffect(PotionEffectType.JUMP);
		if(WolvMC.hasFinishMission("human.jump3", p.getName())) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2147483647, 2), true);
		}
		else if(WolvMC.hasFinishMission("human.jump2", p.getName())) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2147483647, 1), true);
		}
		else if(WolvMC.hasFinishMission("human.jump1", p.getName())) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2147483647, 0), true);
		}
	}
	
	public static void mining(Player p) {
		p.removePotionEffect(PotionEffectType.FAST_DIGGING);
		if(WolvMC.hasFinishMission("human.mining2", p.getName())) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2147483647, 1), true);
		}
		else if(WolvMC.hasFinishMission("human.mining1", p.getName())) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2147483647, 0), true);
		}
	}
	
	public static void resistance(Player p) {
		p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		if(WolvMC.hasFinishMission("human.speed3", p.getName()) && WolvMC.hasFinishMission("human.strength3", p.getName()) && WolvMC.hasFinishMission("human.jump3", p.getName()) && WolvMC.hasFinishMission("human.mining2", p.getName())) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 1), true);
		}
	}

	public static void initHuman() {
		WolvMC.addRace("human", ChatColor.GOLD + "Human", new ItemStack(Material.DIAMOND_PICKAXE, 1));
		WolvMC.addMission("human.speed1", (double) 1000, "human", "Courir sur un total de %goal% blocs", "Vitesse I");
		WolvMC.addMission("human.speed2", (double) 2500, "human", "Courir sur un total de %goal% blocs", "Vitesse II");
		WolvMC.addMission("human.speed3", (double) 5000, "human", "Courir sur un total de %goal% blocs", "Vitesse III");
		WolvMC.addMission("human.strength1", (double) 50, "human", "Tuer %goal% mobs", "Force I");
		WolvMC.addMission("human.strength2", (double) 100, "human", "Tuer %goal% mobs", "Force II");
		WolvMC.addMission("human.strength3", (double) 200, "human", "Tuer %goal% mobs", "Force III");
		WolvMC.addMission("human.jump1", (double) 50, "human", "Faire %goal% sauts", "Saut I");
		WolvMC.addMission("human.jump2", (double) 100, "human", "Faire %goal% sauts", "Saut II");
		WolvMC.addMission("human.jump3", (double) 200, "human", "Faire %goal% sauts", "Saut III");
		WolvMC.addMission("human.mining1", (double) 100, "human", "Miner %goal% blocs", "Célérité I");
		WolvMC.addMission("human.mining2", (double) 1000, "human", "Miner %goal% blocs", "Célérité II");
		WolvMC.addMission("human.resistance1", (double) 11, "human", "Finir les %goal% autres missions", "Résistance II");
		WolvMC.getPlugin(WolvMC.class).getLogger().fine("Human Class loaded!");
	}
}
