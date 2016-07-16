package fr.nashoba24.wolvmc.races;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import me.kvq.plugin.trails.API.SuperTrailsAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.events.WolvMCInitEffectsEvent;
import fr.nashoba24.wolvmc.events.WolvMCReloadEvent;

public class Fairy implements Listener {
	
	static HashMap<String, Integer> sneak = new HashMap<String, Integer>();
	public HashMap<String, Long> blind = new HashMap<String, Long>();
	public HashMap<String, Long> blindbug = new HashMap<String, Long>();
	public HashMap<String, Long> cake = new HashMap<String, Long>();
	public HashMap<String, Long> cakebug = new HashMap<String, Long>();
	public HashMap<String, Boolean> last = new HashMap<String, Boolean>();
	public HashMap<String, Boolean> fly = new HashMap<String, Boolean>();
	public static String inviMSG = ChatColor.GREEN + "You become invisible for %secs% seconds!";
	public static String inviAgain = ChatColor.GREEN + "You can become invisible again!";
	public static String blindMSG = ChatColor.GREEN + "You blind %player% for 15 seconds!";
	public static String instinct = ChatColor.GREEN + "You healed yourself thanks to your survival instinct!";
	public static boolean wings = true;
	static boolean superTrails = WolvMC.trailPlug();
	static Integer speed = 3;
	static Integer inviTime = 30;
	static Integer inviCooldown = 180;
	static Integer blindTime = 15;
	static Integer blindCooldown = 60;
	static Integer cakeCooldown = 90;
	static Integer flyHunger = 90;
	public static boolean enabled = true;
	static boolean invisibility = true;
	static boolean blindPower = true;
	static boolean placeCake = true;
	static boolean flyPower = true;
	static boolean arrowFly = true;
	static boolean instinctPower = true;
	
	@EventHandler
	public void onInitEffects(WolvMCInitEffectsEvent e) {
		Player p = e.getPlayer();
		if(e.getRace().equals("fairy")) {
			if(WolvMC.hasFinishMission("fairy.1", p.getName())) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, speed - 1));
			}
			if(!fly.containsKey(p.getName())) {
				if(wings && superTrails) {
					SuperTrailsAPI.SetWings(p, "Purple", "Purple");
				}
				if(flyPower) {
					p.setAllowFlight(true);
				}
			}
			else if(fly.get(p.getName())==true) {
				if(wings && superTrails) {
					SuperTrailsAPI.SetWings(p, "Purple", "Purple");
				}
				if(flyPower) {
					p.setAllowFlight(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onInvisiblity(PlayerToggleSneakEvent e) {
		if(!invisibility) {
			return;
		}
		final Player p = e.getPlayer();
		if(p.isSneaking()) {
			if(WolvMC.getRace(p.getName()).equals("fairy")) {
				if(sneak.get(p.getName()) == null) {
					sneak.put(p.getName(), 1);
			        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
			            @Override
			            public void run() {
			                if(sneak.get(p.getName())!= null) {
			                	if(sneak.get(p.getName())==1) {
				                	sneak.remove(p.getName());
			                	}
			                }
			            }
			        }, 20L);
				}
				else if(sneak.get(p.getName()) == 1) {
					sneak.put(p.getName(), 2);
			        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
			            @Override
			            public void run() {
			                if(sneak.get(p.getName())!= null) {
			                	if(sneak.get(p.getName())==2) {
				                	sneak.remove(p.getName());
			                	}
			                }
			            }
			        }, 20L);
				}
				else if(sneak.get(p.getName()) == 2) {
					sneak.put(p.getName(), 3);
			        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
			            @Override
			            public void run() {
			                if(sneak.get(p.getName())!= null) {
			                	if(sneak.get(p.getName())==3) {
				                	sneak.remove(p.getName());
			                	}
			                }
			            }
			        }, 20L);
				}
				else if(sneak.get(p.getName()) == 3) {
					p.sendMessage(inviMSG.replaceAll("%secs%", Fairy.inviTime.toString()));
            		if(wings && superTrails) {
            			SuperTrailsAPI.setTrail(null, p);
            		}
					sneak.put(p.getName(), 4);
					p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, inviTime * 20, 1));
			        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
			        	@Override
				            public void run() {
		            		if(wings && superTrails) {
		            			SuperTrailsAPI.SetWings(p, "Purple", "Purple");
		            		}
				            }
				        }, inviTime * 20);
			        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
			        	@Override
				            public void run() {
				            	if(sneak.get(p.getName())!=null) {
				            		if(sneak.get(p.getName())==4) {
				            			sneak.remove(p.getName());
				            			p.sendMessage(inviAgain);
				            		}
				            	}
				            }
				        }, inviCooldown * 20);
			        }
			}
		}
	}
	
	@EventHandler
	public void onBlindPlayer(PlayerInteractEntityEvent e) {
		if(!blindPower) {
			return;
		}
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("fairy") && p.isSneaking() && p.getInventory().getItemInOffHand().getType()==Material.AIR && p.getInventory().getItemInMainHand().getType()==Material.AIR && WolvMC.canUsePowerSafe(p.getLocation(), p)) {
			if(e.getRightClicked() instanceof Player) {
			       int cooldownTime = blindCooldown;
			        if(blind.containsKey(p.getName())) {
						Long bug = blindbug.get(p.getName());
						if(System.currentTimeMillis() - bug < 20) {
							return;
						}
			            long secondsLeft = ((blind.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
			            if(secondsLeft>0) {
			            	blindbug.put(p.getName(), System.currentTimeMillis());
			            	int seconds = (int) secondsLeft;
			            	p.sendMessage(WolvMC.msgCooldown(seconds));
			                return;
			            }
			        }
					WolvMC.addNbToMission("fairy.2", p.getName(), (double) 1);
					((Player) e.getRightClicked()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, blindTime * 20, 1));
					blind.put(p.getName(), System.currentTimeMillis());
					blindbug.put(p.getName(), System.currentTimeMillis());
					p.sendMessage(blindMSG.replaceAll("%player%", ((Player) e.getRightClicked()).getName()).replaceAll("%secs%", Fairy.blindTime.toString()));
			}
		}
	}
	
	@EventHandler
	public void onPoseCake(PlayerInteractEvent e) {
		if(!placeCake) {
			return;
		}
		Player p = e.getPlayer();
		if(e.getAction()==Action.RIGHT_CLICK_BLOCK && (p.getInventory().getItemInOffHand().getType()==Material.AIR || p.getInventory().getItemInOffHand()==null) 
				&& (p.getInventory().getItemInMainHand().getType()==Material.AIR || p.getInventory().getItemInMainHand()==null)) {
				if(WolvMC.getRace(p.getName()).equals("fairy")) {
					if(p.isSneaking() && WolvMC.hasFinishMission("fairy.2", p.getName())) {
						if(e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType()==Material.AIR) {
					       int cooldownTime = cakeCooldown;
					        if(cake.containsKey(p.getName())) {
								Long bug = cakebug.get(p.getName());
								if(System.currentTimeMillis() - bug < 20) {
									return;
								}
					            long secondsLeft = ((cake.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
					            if(secondsLeft>0) {
					            	cakebug.put(p.getName(), System.currentTimeMillis());
					            	int seconds = (int) secondsLeft;
					            	p.sendMessage(WolvMC.msgCooldown(seconds));
					                return;
					            }
					        }
					        if(!WolvMC.canUsePowerBlock(e.getClickedBlock().getLocation(), p)) {
					        	cakebug.put(p.getName(), System.currentTimeMillis());
					        	cake.put(p.getName(), System.currentTimeMillis() - cooldownTime * 1001);
					        	return;
					        }
					        cake.put(p.getName(), System.currentTimeMillis());
					        cakebug.put(p.getName(), System.currentTimeMillis());
					        e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().setType(Material.CAKE_BLOCK);
					        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
						}
					}
				}
		}
	}
	
	@EventHandler
	public void onDamageByAnArrow(EntityDamageByEntityEvent e) {
		if(!arrowFly) {
			return;
		}
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
			Player p = (Player) e.getEntity();
			if(WolvMC.getRace(p.getName()).equals("fairy")) {
        		if(flyPower) {
            		p.setAllowFlight(false);
        		}
				fly.put(p.getName(), false);
        		if(wings && superTrails) {
        			SuperTrailsAPI.setTrail(null, p);
        		}
			}
		}
	}
	
	@EventHandler
	public void onDeathByArrow(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if(last.containsKey(p.getName())) {
			WolvMC.addNbToMission("fairy.1", p.getName(), (double) 1);
			last.remove(p.getName());
		}
	}
	
	@EventHandler
	public void onLastDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
			Player p = (Player) e.getEntity();
			Projectile proj = (Projectile) e.getDamager();
			if(WolvMC.getRace(p.getName()).equals("fairy") && proj.getShooter() instanceof Player) {
				Player shooter = (Player) proj.getShooter();
				if(p.getName().equals(shooter.getName()) && e.getCause()==DamageCause.PROJECTILE && shooter.getName().equals(p.getName())) {
					last.put(p.getName(), true);
					return;
				}
			}
		}
		if(e.getEntity() instanceof Player && !(last.containsKey(((Player) e.getEntity()).getName()) && e.getCause()==DamageCause.FALL)) {
			last.remove(((Player) e.getEntity()).getName());
		}
	}
	
	@EventHandler
	public void onInstinct(EntityDamageEvent e) {
		if(!instinctPower) {
			return;
		}
		if(e.getEntity() instanceof Player) {
			final Player p = (Player) e.getEntity();
			if(WolvMC.getRace(p.getName()).equals("fairy") && p.getHealth()<10 && p.getFoodLevel()>10) {
				p.setFoodLevel(p.getFoodLevel() - 10);
				p.setHealth(p.getHealth() + 10);
				p.sendMessage(instinct);
        		if(wings && superTrails) {
        			SuperTrailsAPI.SetWings(p, "Red", "Red");
        		}
		        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
		        	@Override
			            public void run() {
	            		if(wings && superTrails) {
	            			SuperTrailsAPI.SetWings(p, "Purple", "Purple");
	            		}
			            }
			        }, 40L);
			}
		}
	}
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if(WolvMC.getRace(e.getPlayer().getName()).equals("fairy")) {
    		if(flyPower) {
        		e.getPlayer().setAllowFlight(true);
    		}
			fly.put(e.getPlayer().getName(), true);
    		if(wings && superTrails) {
    			SuperTrailsAPI.SetWings(e.getPlayer(), "Purple", "Purple");
    		}
		}
	}
	
	@EventHandler
	public void onWorldChange(PlayerTeleportEvent e) {
		final Player p = e.getPlayer();
		if(!e.getFrom().getWorld().getName().equals(e.getTo().getWorld().getName()) && WolvMC.getRace(p.getName()).equals("fairy")) {
			if(p.getAllowFlight()) {
				BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
		            @Override
		            public void run() {
	            		if(flyPower) {
		            		p.setAllowFlight(true);
	            		}
		            	fly.put(p.getName(), true);
	            		if(wings && superTrails) {
	            			SuperTrailsAPI.SetWings(p, "Purple", "Purple");
	            		}
		            }
		        }, 1L);
			}
		}
	}
	
	public static void initFairy() {
		//WolvMC.addMission("fairy.1", (double) 5, "fairy", "Shoot yourself %goal% times with an arrow and die then by falling", "Speed III");
	 	//WolvMC.addMission("fairy.2", (double) 30, "fairy", "Blind %goal% players", "Can place cakes (sneak + rightclick on a block)");
		WolvMC.addRace("fairy", ChatColor.DARK_PURPLE + "Fairy", new ItemStack(Material.GLOWSTONE_DUST, 1));
		WolvMC.addMission("fairy.1", (double) 5, "fairy", "Se tirer %goal% fois dessus avec une flèche et mourir en tombant", "Vitesse III");
	 	WolvMC.addMission("fairy.2", (double) 30, "fairy", "Aveugler %goal% joueurs", "Pouvoir poser des gateaux (sneak + clic droit sur un bloc)");
		WolvMC.getPlugin(WolvMC.class).getLogger().fine("Fairy Class loaded!");
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
            @SuppressWarnings("deprecation")
			@Override
            public void run() {
    			for(Player p : WolvMC.getPlugin(WolvMC.class).getServer().getOnlinePlayers()) {
    				if(WolvMC.getRace(p.getName()).equals("fairy")) {
    					if(p.getFoodLevel()==0 && p.getGameMode()!=GameMode.CREATIVE && p.getGameMode()!=GameMode.SPECTATOR && p.getAllowFlight()==true) {
    	            		if(flyPower) {
    		            		p.setAllowFlight(false);
    	            		}
    					}
    					if(p.isFlying()) {
    						p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 2147483647, flyHunger));
    					}
    					else {
    						Collection<PotionEffect> effects = p.getActivePotionEffects();
    			            for (PotionEffect e : effects) {
    			                if(e.getType().getId()==17 && e.getDuration()>100000) {
    			                	p.removePotionEffect(PotionEffectType.HUNGER);
    			                }
    			            }
    					}
    				}
    			}
            }
        }, 0L, 20L);
	}
	
	@EventHandler
	public void reloadFairy(WolvMCReloadEvent e) {
		File file = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/fairy.yml");
		if(!file.exists()) {
			  InputStream stream = WolvMC.class.getClassLoader().getResourceAsStream("fr/nashoba24/wolvmc/config/fairy.yml");
			  FileOutputStream fos = null;
			  try {
			      fos = new FileOutputStream(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/fairy.yml");
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
		if(config.isSet("wings")) {
			Fairy.wings = config.getBoolean("wings");
		}
		if(config.isSet("speed-effect")) {
			Fairy.speed = config.getInt("speed-effect");
		}
		if(config.isSet("invisibility-time")) {
			Fairy.inviTime = config.getInt("invisibility-time");
		}
		if(config.isSet("invisibility-cooldown")) {
			Fairy.inviCooldown = config.getInt("invisibility-cooldown");
		}
		if(config.isSet("blind-time")) {
			Fairy.blindTime = config.getInt("blind-time");
		}
		if(config.isSet("blind-cooldown")) {
			Fairy.blindCooldown = config.getInt("blind-cooldown");
		}
		if(config.isSet("cake-cooldown")) {
			Fairy.cakeCooldown = config.getInt("cake-cooldown");
		}
		if(config.isSet("fly-hunger")) {
			Fairy.flyHunger = config.getInt("fly-hunger");
		}
		if(config.isSet("enabled")) {
			Fairy.enabled = config.getBoolean("enabled");
		}
		if(config.isSet("invisibility")) {
			Fairy.invisibility = config.getBoolean("invisibility");
		}
		if(config.isSet("blind-player")) {
			Fairy.blindPower = config.getBoolean("blind-player");
		}
		if(config.isSet("place-cake")) {
			Fairy.placeCake = config.getBoolean("place-cake");
		}
		if(config.isSet("fly")) {
			Fairy.flyPower = config.getBoolean("fly");
		}
		if(config.isSet("arrow-disallow-fly")) {
			Fairy.arrowFly = config.getBoolean("arrow-disallow-fly");
		}
		if(config.isSet("heal-thanks-to-instinct")) {
			Fairy.instinctPower = config.getBoolean("heal-thanks-to-instinct");
		}
	}

}
