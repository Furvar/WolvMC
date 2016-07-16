package fr.nashoba24.wolvmc.races;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.events.WolvMCInitEffectsEvent;
import fr.nashoba24.wolvmc.events.WolvMCReloadEvent;
import fr.nashoba24.wolvmc.utils.EnumDisguises;

public class Angel implements Listener {
	
	public HashMap<String, Long> water = new HashMap<String, Long>();
	public HashMap<String, Long> waterbug = new HashMap<String, Long>();
	public HashMap<String, Long> transf = new HashMap<String, Long>();
	public HashMap<String, Long> bug = new HashMap<String, Long>();
	public HashMap<String, Long> bug2 = new HashMap<String, Long>();
	public static String healAngel = ChatColor.GREEN + "You have been healed by the Angel %player%!";
	public static String healedSO = ChatColor.GREEN + "You have healed %player%!";
	public static String netherMSG = ChatColor.RED + "Go back where you come creature of God!";
	static Integer waterCooldown = 30;
	static Integer protectionChance = 20;
	static double friendly = 2.0;
	static double hostile = 3.0;
	static double players = 1.0;
	static Integer morphTime = 20;
	static Integer morphTimeMission = 40;
	static Integer morphCooldown = 120;
	public static boolean enabled = true;
	static boolean ld = WolvMC.canDis();
	static boolean waterPower = true;
	static boolean cantDamageVillagers = true;
	static boolean healPower = true;
	static boolean fly = true;
	static boolean morph = true;
	static boolean stopRain = true;
	static boolean killOnNether = true;
	
	@EventHandler
	public void onInitEffects(WolvMCInitEffectsEvent e) {
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("angel")) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 2147483647, 1));
			p.setAllowFlight(true);
		}
	}
	
	@EventHandler
	public void onPlaceWater(PlayerInteractEvent e) {
		if(!waterPower) {
			return;
		}
		Player p = e.getPlayer();
		if(e.getAction()==Action.RIGHT_CLICK_BLOCK && e.getItem()==null && WolvMC.getRace(p.getName()).equals("angel")) {
			if(p.isSneaking()) {
				if(e.getClickedBlock().getLocation().add(0, 2, 0).getBlock().getType()!=Material.AIR && e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType()!=Material.AIR) {
					return;
				}
			       int cooldownTime = waterCooldown;
			        if(water.containsKey(p.getName())) {
						Long bug = waterbug.get(p.getName());
						if(System.currentTimeMillis() - bug < 20) {
							return;
						}
			            long secondsLeft = ((water.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
			            if(secondsLeft>0) {
			            	waterbug.put(p.getName(), System.currentTimeMillis());
			            	int seconds = (int) secondsLeft;
			            	p.sendMessage(WolvMC.msgCooldown(seconds));
			                return;
			            }
			        }
			        if(!WolvMC.canUsePowerBlock(e.getClickedBlock().getLocation(), p)) {
			        	waterbug.put(p.getName(), System.currentTimeMillis());
			        	water.put(p.getName(), System.currentTimeMillis() - cooldownTime * 1001);
			        	return;
			        }
			        waterbug.put(p.getName(), System.currentTimeMillis());
			        water.put(p.getName(), System.currentTimeMillis());
			        if(e.getClickedBlock().getLocation().add(0, 2, 0).getBlock().getType()==Material.AIR) {
			        	e.getClickedBlock().getLocation().add(0, 2, 0).getBlock().setType(Material.WATER);
			        }
			        if(e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType()==Material.AIR) {
			        	e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().setType(Material.WATER);
			        }
			        final Block b = e.getClickedBlock();
					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
			            @Override
			            public void run() {
					        if(b.getLocation().add(0, 2, 0).getBlock().getType()==Material.WATER) {
					        	b.getLocation().add(0, 2, 0).getBlock().setType(Material.AIR);
					        }
					        if(b.getLocation().add(0, 1, 0).getBlock().getType()==Material.WATER) {
					        	b.getLocation().add(0, 1, 0).getBlock().setType(Material.AIR);
					        }
			            }
			        }, 5L);
			}
		}
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(WolvMC.getRace(p.getName()).equals("angel")) {
				Random rand = new Random();
				int number = rand.nextInt(100) + 1;
				if(number <= protectionChance)
				{
					e.setCancelled(true);
					p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 1);
				}
			}
		}
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if(WolvMC.getRace(p.getName()).equals("angel")) {
				if(e.getEntity().getType() == EntityType.COW || e.getEntity().getType() == EntityType.PIG || e.getEntity().getType() == EntityType.CHICKEN || e.getEntity().getType() == EntityType.SHEEP || e.getEntity().getType() == EntityType.SQUID || e.getEntity().getType() == EntityType.OCELOT || e.getEntity().getType() == EntityType.HORSE || e.getEntity().getType() == EntityType.BAT || e.getEntity().getType() == EntityType.IRON_GOLEM || e.getEntity().getType() == EntityType.MUSHROOM_COW || e.getEntity().getType() == EntityType.RABBIT || e.getEntity().getType() == EntityType.WOLF) {
					e.setDamage(e.getDamage() / friendly);
				}
				else if(e.getEntity().getType() == EntityType.VILLAGER && cantDamageVillagers) {
					e.setCancelled(true);
				}
				else if(e.getEntity().getType() != EntityType.PLAYER) {
					e.setDamage(e.getDamage() * hostile);
				}
				else if(e.getEntity().getType() == EntityType.PLAYER) {
					e.setDamage(e.getDamage() / players);
				}
			}
		}
	}
	
	@EventHandler
	public void onHealPlayer(PlayerInteractAtEntityEvent e) {
		if(!healPower) {
			return;
		}
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("angel")) {
		        if(bug.containsKey(p.getName())) {
					Long ms = bug.get(p.getName());
					if(System.currentTimeMillis() - ms < 20) {
						return;
					}
		            bug.put(p.getName(), System.currentTimeMillis());
		        }
		        bug.put(p.getName(), System.currentTimeMillis());
			if(p.isSneaking() && e.getRightClicked() instanceof Player && p.getFoodLevel()>=2) {
				Player p2 = (Player) e.getRightClicked();
				if(WolvMC.hasFinishMission("angel.1", p.getName())) {
					Random rand = new Random();
					int number = rand.nextInt(100) + 1;
					if(number <= 50)
					{
						p.setFoodLevel(p.getFoodLevel() - 2);
					}
				}
				else {
					p.setFoodLevel(p.getFoodLevel() - 2);
				}
				if(p2.getHealth()>p.getMaxHealth() - 2) {
					p2.setHealth(p.getMaxHealth());
				}
				else {
					p2.setHealth(p.getHealth() + 2);
				}
				p.sendMessage(healAngel.replaceAll("%player%", p2.getName()));
				p2.sendMessage(healedSO.replaceAll("%player%", p.getName()));
				WolvMC.addNbToMission("angel.1", p.getName(), (double) 1);
			}
		}
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("angel")) {
			p.setAllowFlight(true);
		}
	}
	
	@EventHandler
	public void onTransf(PlayerInteractAtEntityEvent e) {
		if(!ld || !morph) {
			return;
		}
		if(EnumDisguises.getDisguiseType(e.getRightClicked().getType())==null) {
			return;
		}
		final Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("angel")) {
			if(e.getRightClicked().getType()!=EntityType.PLAYER && p.isSneaking()) {
			       int cooldownTime = morphCooldown;
			       if(transf.containsKey(p.getName())) {
			    	   Long bug = bug2.get(p.getName());
			    	   if(System.currentTimeMillis() - bug < 20) {
			    		   return;
			    	   }
			    	   long secondsLeft = ((transf.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
			    	   if(secondsLeft>0) {
			    		   bug2.put(p.getName(), System.currentTimeMillis());
			    		   int seconds = (int) secondsLeft;
			    		   p.sendMessage(WolvMC.msgCooldown(seconds));
			    		   return;
			    	   }
			       }
					MobDisguise md = new MobDisguise(EnumDisguises.getDisguiseType(e.getRightClicked().getType()));
			        DisguiseAPI.disguiseEntity(p, md);
			        transf.put(p.getName(), System.currentTimeMillis());
			        bug2.put(p.getName(), System.currentTimeMillis());
			        if(e.getRightClicked().getType()==EntityType.SQUID) {
			        	WolvMC.addNbToMission("angel.2", p.getName(), (double) 1);
			        }
			        if(WolvMC.hasFinishMission("angel.2", p.getName())) {
				        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
				        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
				            @Override
				            public void run() {
				            	DisguiseAPI.undisguiseToAll(p);
				            }
				        }, morphTimeMission * 20);
			        }
			        else {
				        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
				        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
				            @Override
				            public void run() {
				            	DisguiseAPI.undisguiseToAll(p);
				            }
				        }, morphTime * 20);
			        }
			}
		}
	}
	
	@EventHandler
	public void onStopRain(PlayerInteractEvent e) {
		if(!stopRain) {
			return;
		}
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("angel") && e.getAction()==Action.RIGHT_CLICK_AIR && e.getItem()!=null) {
			if(e.getItem().getType()==Material.GLOWSTONE_DUST && p.getWorld().hasStorm()) {
				p.getWorld().setStorm(false);
				if(e.getHand().equals(EquipmentSlot.OFF_HAND)) {
					if(p.getInventory().getItemInOffHand().getAmount()<=1) {
						p.getInventory().setItemInOffHand(null);
					}
					else {
						p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount() - 1);
					}
				}
				else if (e.getHand().equals(EquipmentSlot.HAND)){
					if(p.getInventory().getItemInMainHand().getAmount()<=1) {
						p.getInventory().setItemInMainHand(null);
					}
					else {
						p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
					}
				}
			}
		}
	}
	
	public static void initAngel() {
		//WolvMC.addMission("angel.1", (double) 70, "angel", "Heal %goal% players", "1 in 2 chance to not lose hunger when heal a player");
	 	//WolvMC.addMission("angel.2", (double) 15, "angel", "Metamorphose %goal% times in a squid", "Metamorphosis 2x longer (40 seconds)");
		WolvMC.addRace("angel", ChatColor.WHITE + "Angel", new ItemStack(Material.GHAST_TEAR, 1));
		WolvMC.addMission("angel.1", (double) 70, "angel", "Soigner %goal% joueurs", "1 chance sur 2 de ne pas perdre de faim lors d'un soin");
	 	WolvMC.addMission("angel.2", (double) 15, "angel", "Se transformer %goal% fois en poulpe", "Métamorphose 2x plus longue (40 secondes)");
		WolvMC.getPlugin(WolvMC.class).getLogger().fine("Angel Class loaded!");
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
            @Override
            public void run() {
        		if(!killOnNether) {
        			return;
        		}
    			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
    				if(WolvMC.getRace(p.getName()).equals("angel")) {
    					if(p.getWorld().getEnvironment()==Environment.NETHER && !p.isDead() && !p.hasPermission("angel.nether.nokill")) {
    						p.sendMessage(netherMSG);
    						p.setHealth(0);
    					}
    				}
    			}
            }
        }, 0L, 20L);
	}
	
	@EventHandler
	public void reloadAngel(WolvMCReloadEvent e) {
		File file = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/angel.yml");
		if(!file.exists()) {
			  InputStream stream = WolvMC.class.getClassLoader().getResourceAsStream("fr/nashoba24/wolvmc/config/angel.yml");
			  FileOutputStream fos = null;
			  try {
			      fos = new FileOutputStream(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/angel.yml");
			      byte[] buf = new byte[2048];
			      int r = stream.read(buf);
			      while(r != -1) {
			          fos.write(buf, 0, r);
			          r = stream.read(buf);
			      }
			      fos.close();
			  } catch (IOException e1) {
				e1.printStackTrace();
			  }
		}
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if(config.isSet("enabled")) {
			Angel.enabled = config.getBoolean("enabled");
		}
		if(config.isSet("water-cooldown")) {
			Angel.waterCooldown = config.getInt("water-cooldown");
		}
		if(config.isSet("protection-chance")) {
			Angel.protectionChance = config.getInt("protection-chance");
		}
		if(config.isSet("friendly-mobs-damages-diviser")) {
			Angel.friendly = config.getDouble("friendly-mobs-damages-diviser");
		}
		if(config.isSet("hostile-mobs-damages-multiplier")) {
			Angel.hostile = config.getDouble("hostile-mobs-damages-multiplier");
		}
		if(config.isSet("player-damages-diviser")) {
			Angel.players = config.getDouble("player-damages-diviser");
		}
		if(config.isSet("morph-time")) {
			Angel.morphTime = config.getInt("morph-time");
		}
		if(config.isSet("morph-time-mission-finished")) {
			Angel.morphTimeMission = config.getInt("morph-time-mission-finished");
		}
		if(config.isSet("morph-cooldown")) {
			Angel.morphCooldown = config.getInt("morph-cooldown");
		}
		if(config.isSet("place-water")) {
			Angel.waterPower = config.getBoolean("place-water");
		}
		if(config.isSet("cant-damage-villager")) {
			Angel.cantDamageVillagers = config.getBoolean("cant-damage-villager");
		}
		if(config.isSet("heal-player")) {
			Angel.healPower = config.getBoolean("heal-player");
		}
		if(config.isSet("fly")) {
			Angel.fly = config.getBoolean("fly");
		}
		if(config.isSet("morph")) {
			Angel.morph = config.getBoolean("morph");
		}
		if(config.isSet("stop-rain")) {
			Angel.stopRain = config.getBoolean("stop-rain");
		}
		if(config.isSet("kill-on-nether")) {
			Angel.killOnNether = config.getBoolean("kill-on-nether");
		}
	}
}
