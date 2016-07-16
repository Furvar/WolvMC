package fr.nashoba24.wolvmc.races;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.commands.WerewolfStats;
import fr.nashoba24.wolvmc.events.WolvMCInitEffectsEvent;
import fr.nashoba24.wolvmc.events.WolvMCReloadEvent;
import fr.nashoba24.wolvmc.events.WolvMCSaveEvent;

public class Werewolf implements Listener {
	
	static int lastTick = 0;
	static HashMap<String, Boolean> transf = new HashMap<String, Boolean>();
	static HashMap<String, Integer> kills = new HashMap<String, Integer>();
	public HashMap<String, Long> howl = new HashMap<String, Long>();
	public HashMap<String, Long> quick = new HashMap<String, Long>();
	public HashMap<String, Long> quickbug = new HashMap<String, Long>();
	public static String howlMSG = ChatColor.translateAlternateColorCodes('&', "&7&kaaa&r &7HAAOOOOUWW!&r &7&kaaa");
	public static String cantSleep = ChatColor.RED + "You can't sleep because you are a werewolf!";
	public static String transfMSG = ChatColor.GRAY + "You've turned into a Werewolf!";
	public static String untransfMSG = ChatColor.GRAY + "You are a human again!";
	static Integer cooldownHowl = 10;
	public static boolean enabled = true;
	static boolean walkOrchid = true;
	static boolean damageByOrchid = true;
	static boolean breakOrchid = true;
	static boolean invincibleMobs = true;
	static boolean shootArrow = true;
	static boolean eatBones = true;
	static boolean howlPower = true;
	static boolean cantSleepPower = true;
	
	@EventHandler
	public void onInitEffects(WolvMCInitEffectsEvent e) {
		final Player p = e.getPlayer();
		if(e.getRace().equals("werewolf")) {
			Object data = WolvMC.getSave(p.getUniqueId().toString(), "werewolf.kills");
			if(data==null) {
				kills.put(p.getName(), 0);
			}
			else {
				kills.put(p.getName(), (Integer) data);
			}
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
	            @Override
	            public void run() {
	            	WolvMC.getPlugin(WolvMC.class).getServer().dispatchCommand(p, "untransf silent");
	            }
	        }, 1L);
	        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
	            @Override
	            public void run() {
	            	WolvMC.getPlugin(WolvMC.class).getServer().dispatchCommand(p, "transf silent");
	            }
	        }, 2L);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onWalkBlueOrchid(PlayerMoveEvent e) {
		if(!walkOrchid) {
			return;
		}
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("werewolf")) {
			if(p.getLocation().getBlock().getType()==Material.RED_ROSE && p.getLocation().getBlock().getData()==(byte) 1) {
				p.damage(p.getHealth() / 2);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamageByBlueOrchid(EntityDamageByEntityEvent e) {
		if(!damageByOrchid) {
			return;
		}
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(WolvMC.getRace(p.getName()).equals("werewolf") && e.getDamager() instanceof Player) {
				Player a = (Player) e.getDamager();
				if(a.getInventory().getItemInMainHand().getType()==Material.RED_ROSE && a.getInventory().getItemInMainHand().getData().getData()==1) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 1));
					p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 2));
					Random rand = new Random();
					int number = rand.nextInt(100) + 1;
					if(number <= 33)
					{
						if(Werewolf.getTransf(p.getName())) {
							   WolvMC.getPlugin(WolvMC.class).getServer().dispatchCommand(p, "untransf");
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBreakBlueOrchid(BlockBreakEvent e) {
		if(!breakOrchid) {
			return;
		}
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("werewolf")) {
			if(e.getBlock().getType()==Material.RED_ROSE && e.getBlock().getData()==1) {
				e.setCancelled(true);
			}
			else if(e.getBlock().getLocation().add(0, 1, 0).getBlock().getType()==Material.RED_ROSE && e.getBlock().getLocation().add(0, 1, 0).getBlock().getData()==1) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if(WolvMC.getRace(p.getName()).equals("werewolf") && Werewolf.getTransf(p.getName())) {
				Integer kill = Werewolf.getKills(p.getName());
				Material m = p.getInventory().getItemInMainHand().getType();
				boolean b = false;
				String lvl = WerewolfStats.getLevelForKills(kill);
				Integer tools = WerewolfStats.tools.get(lvl);
				if(tools==1) {
					if(m==Material.DIAMOND_AXE || m==Material.DIAMOND_SWORD || m==Material.DIAMOND_SPADE || m==Material.DIAMOND_PICKAXE || m==Material.DIAMOND_HOE) {
						e.setCancelled(true);
						b = true;
					}
				}
				if(tools==2) {
					if(m==Material.DIAMOND_AXE || m==Material.DIAMOND_SWORD || m==Material.DIAMOND_SPADE || m==Material.DIAMOND_PICKAXE || m==Material.DIAMOND_HOE || m==Material.IRON_AXE || m==Material.IRON_SWORD || m==Material.IRON_SPADE || m==Material.IRON_PICKAXE || m==Material.IRON_HOE) {
						e.setCancelled(true);
						b = true;
					}
				}
				if(tools==3) {
					if(m==Material.DIAMOND_AXE || m==Material.DIAMOND_SWORD || m==Material.DIAMOND_SPADE || m==Material.DIAMOND_PICKAXE || m==Material.DIAMOND_HOE || m==Material.IRON_AXE || m==Material.IRON_SWORD || m==Material.IRON_SPADE || m==Material.IRON_PICKAXE || m==Material.IRON_HOE || m==Material.STONE_AXE || m==Material.STONE_SWORD || m==Material.STONE_SPADE || m==Material.STONE_PICKAXE || m==Material.STONE_HOE) {
						e.setCancelled(true);
						b = true;
					}
				}
				if(tools==4) {
					if(m==Material.DIAMOND_AXE || m==Material.DIAMOND_SWORD || m==Material.DIAMOND_SPADE || m==Material.DIAMOND_PICKAXE || m==Material.DIAMOND_HOE || m==Material.IRON_AXE || m==Material.IRON_SWORD || m==Material.IRON_SPADE || m==Material.IRON_PICKAXE || m==Material.IRON_HOE || m==Material.STONE_AXE || m==Material.STONE_SWORD || m==Material.STONE_SPADE || m==Material.STONE_PICKAXE || m==Material.STONE_HOE || m==Material.GOLD_AXE || m==Material.GOLD_SWORD || m==Material.GOLD_SPADE || m==Material.GOLD_PICKAXE || m==Material.GOLD_HOE || m==Material.WOOD_AXE || m==Material.WOOD_SWORD || m==Material.WOOD_SPADE || m==Material.WOOD_PICKAXE || m==Material.WOOD_HOE) {
						e.setCancelled(true);
						b = true;
					}
				}
				if(b) {
					p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
				}
			}
		}
	}
	
	@EventHandler
	public void onDamageByMob(EntityDamageByEntityEvent e) {
		if(!invincibleMobs) {
			return;
		}
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(WolvMC.getRace(p.getName()).equals("werewolf") && Werewolf.getTransf(p.getName())) {
				EntityType t = e.getDamager().getType();
				if(t==EntityType.ZOMBIE || t==EntityType.SKELETON || t==EntityType.SPIDER || t==EntityType.CAVE_SPIDER || t==EntityType.CREEPER || t==EntityType.GHAST || t==EntityType.BLAZE || t==EntityType.SLIME || t==EntityType.PIG_ZOMBIE || t==EntityType.ENDERMAN || t==EntityType.SILVERFISH || t==EntityType.MAGMA_CUBE || t==EntityType.WITCH) {
					e.setCancelled(true);
				}
				else if(e.getDamager() instanceof Projectile) {
					Projectile proj = (Projectile) e.getDamager();
					if(proj.getShooter() instanceof LivingEntity) {
						t = ((LivingEntity) proj.getShooter()).getType();
						if(t==EntityType.ZOMBIE || t==EntityType.SKELETON || t==EntityType.SPIDER || t==EntityType.CAVE_SPIDER || t==EntityType.CREEPER || t==EntityType.GHAST || t==EntityType.BLAZE || t==EntityType.SLIME || t==EntityType.PIG_ZOMBIE || t==EntityType.ENDERMAN || t==EntityType.SILVERFISH || t==EntityType.MAGMA_CUBE || t==EntityType.WITCH) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onKillMob(EntityDeathEvent e) {
		if(!(e.getEntity() instanceof Player) && e.getEntity().getKiller() instanceof Player) {
			Player p = (Player) e.getEntity().getKiller();
			if(WolvMC.getRace(p.getName()).equals("werewolf") && (p.getInventory().getItemInMainHand()==null || p.getInventory().getItemInMainHand().getType()==Material.AIR) && Werewolf.getTransf(p.getName())) {
				WolvMC.addNbToMission("werewolf.1", p.getName(), (double) 1);
			}
		}
	}
	
	@EventHandler
	public void onShootArrow(ProjectileLaunchEvent e) {
		if(!shootArrow) {
			return;
		}
		if(e.getEntity() instanceof Arrow && e.getEntity().getShooter() instanceof Player) {
			Player p = (Player) e.getEntity().getShooter();
			if(WolvMC.getRace(p.getName()).equals("werewolf") && Werewolf.getTransf(p.getName())) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		final Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("werewolf") && Werewolf.getTransf(p.getName())) {
			Material i = e.getItem().getType();
			if(i==Material.APPLE || i==Material.MUSHROOM_SOUP || i==Material.BREAD || i==Material.RAW_FISH || i==Material.COOKED_FISH || i==Material.MUSHROOM_SOUP || i==Material.CAKE || i==Material.COOKIE || i==Material.MELON || i==Material.COOKED_BEEF || i==Material.COOKED_CHICKEN || i==Material.CARROT || i==Material.POTATO_ITEM || i==Material.BAKED_POTATO || i==Material.GOLDEN_CARROT || i==Material.PUMPKIN_PIE || i==Material.COOKED_MUTTON || i==Material.COOKED_RABBIT || i==Material.SPECKLED_MELON || i==Material.GRILLED_PORK) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 400, 5));
			}
			else {
				if(p.getFoodLevel()>16) {
					p.setSaturation(4 - (20 - p.getFoodLevel()));
				}
			    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
				if(!p.hasPotionEffect(PotionEffectType.HUNGER)) {
				        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
				            @Override
				            public void run() {
				            	if(p.hasPotionEffect(PotionEffectType.HUNGER)) {
				            		p.removePotionEffect(PotionEffectType.HUNGER);
				            	}
				            }
				        }, 1L);
				}
		        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
		            @Override
		            public void run() {
		            	p.setFoodLevel(p.getFoodLevel() + 4);
		            }
		        }, 1L);
			}
		}
	}
	
	@EventHandler
	public void onRightClickBone(PlayerInteractEvent e) {
		if(!eatBones) {
			return;
		}
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("werewolf") && p.getFoodLevel()<20 && e.getItem()!=null && e.getAction()==Action.RIGHT_CLICK_AIR) {
			if(e.getItem().getType()==Material.BONE) {
				if(p.getInventory().getItemInMainHand()!=null) {
					if(p.getInventory().getItemInMainHand().getType()==Material.BONE) {
						if(p.getInventory().getItemInMainHand().getAmount()==1) {
							p.getInventory().setItemInMainHand(null);
							if(p.getFoodLevel()>16) {
								p.setSaturation(4 - (20 - p.getFoodLevel()));
							}
							p.setFoodLevel(p.getFoodLevel() + 4);
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
						}
						else {
							p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
							if(p.getFoodLevel()>16) {
								p.setSaturation(4 - (20 - p.getFoodLevel()));
							}
							p.setFoodLevel(p.getFoodLevel() + 4);
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onKillPlayer(PlayerDeathEvent e) {
		if(e.getEntity().getKiller() instanceof Player) {
			Player p = (Player) e.getEntity().getKiller();
			if(WolvMC.getRace(p.getName()).equals("werewolf") && Werewolf.getTransf(p.getName())) {
				kills.put(p.getName(), Werewolf.getKills(p.getName()) + 1);
			}
		}
	}
	
	@EventHandler
	public void onSave(WolvMCSaveEvent e) {
		e.save("werewolf.kills", Werewolf.getKills(e.getPlayer().getName()));
	}
	
	@EventHandler
	public void onHowl(PlayerToggleSneakEvent e) {
		if(!howlPower) {
			return;
		}
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("werewolf") && Werewolf.getTransf(p.getName())) {
			if(p.isSneaking() && p.getLocation().getPitch()==-90) {
			       int cooldownTime = Werewolf.cooldownHowl;
			        if(howl.containsKey(p.getName())) {
			            long secondsLeft = ((howl.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
			            if(secondsLeft>0) {
			            	int seconds = (int) secondsLeft;
			            	p.sendMessage(WolvMC.msgCooldown(seconds));
			                return;
			            }
			        }
			        p.sendMessage(howlMSG);
					for(Player lp : Bukkit.getServer().getOnlinePlayers()) {
						if(p.getWorld()==lp.getWorld()) {
							if(p.getLocation().distance(lp.getLocation())<50) {
								lp.playSound(lp.getLocation(), Sound.ENTITY_WOLF_HOWL, 1, 1);
							}
						}
					}
			        howl.put(p.getName(), System.currentTimeMillis());
			}
		}
	}
	
	@EventHandler
	public void onQuickTransf(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if((e.getAction()==Action.RIGHT_CLICK_BLOCK || e.getAction()==Action.RIGHT_CLICK_AIR) && (p.getInventory().getItemInOffHand().getType()==Material.AIR || p.getInventory().getItemInOffHand()==null) 
				&& (p.getInventory().getItemInMainHand().getType()==Material.AIR || p.getInventory().getItemInMainHand()==null)) {
			if(p.getPlayer().getInventory().getHeldItemSlot()==8) {
				if(WolvMC.getRace(p.getName()).equals("werewolf")) {
					if(p.isSneaking()) {
					       int cooldownTime = 5;
					        if(quick.containsKey(p.getName())) {
								Long bug = quickbug.get(p.getName());
								if(System.currentTimeMillis() - bug < 20) {
									return;
								}
					            long secondsLeft = ((quick.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
					            if(secondsLeft>0) {
					            	quickbug.put(p.getName(), System.currentTimeMillis());
					            	int seconds = (int) secondsLeft;
					            	p.sendMessage(WolvMC.msgCooldown(seconds));
					                return;
					            }
					        }
					        quick.put(p.getName(), System.currentTimeMillis());
					        quickbug.put(p.getName(), System.currentTimeMillis());
					        if(Werewolf.getTransf(p.getName())) {
					        	 WolvMC.getPlugin(WolvMC.class).getServer().dispatchCommand(p, "untransf");
					        }
					        else {
					        	 WolvMC.getPlugin(WolvMC.class).getServer().dispatchCommand(p, "transf");
					        }
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBedEnter(PlayerBedEnterEvent e) {
		if(!cantSleepPower) {
			return;
		}
		if(WolvMC.getRace(e.getPlayer().getName()).equals("werewolf")) {
			e.getPlayer().sendMessage(cantSleep);
			e.setCancelled(true);
		}
	}
	
	static void at6() {
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			if(WolvMC.getRace(p.getName()).equals("werewolf")) {
				//DisguiseAPI.undisguiseToAll(p);
				Werewolf.setTransf(p.getName(), false);
				p.sendMessage(untransfMSG);
				p.removePotionEffect(PotionEffectType.SPEED);
				p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				p.removePotionEffect(PotionEffectType.HEALTH_BOOST);
				p.removePotionEffect(PotionEffectType.JUMP);
				p.removePotionEffect(PotionEffectType.FAST_DIGGING);
				p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			}
		}
	}
	
	static void at20() {
		boolean wolf = false;
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			if(WolvMC.getRace(p.getName()).equals("werewolf")) {
				if(!Werewolf.getTransf(p.getName())) {
					wolf = true;
					Werewolf.setTransf(p.getName(), true);
					//PlayerDisguise playerDisguise = new PlayerDisguise();
					//DisguiseAPI.disguiseEntity(p, playerDisguise);
					p.sendMessage(transfMSG);
					WolvMC.addNbToMission("werewolf.2", p.getName(), (double) 1);
					p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2147483647, 1));
					Werewolf.applySpeedAndStrength(p);
					//ALPHA MEUTE HEALTH BOOST
					if(WolvMC.hasFinishMission("werewolf.1", p.getName())) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2147483647, 1));
					}
					if(WolvMC.hasFinishMission("werewolf.2", p.getName())) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 1));
					}
				}
			}
		}
		if(wolf) {
			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.ENTITY_WOLF_HOWL, 1, 1);
			}
		}
	}
	
	public static void applySpeedAndStrength(Player p) {
		Integer nb = kills.get(p.getName());
		if(nb==null) {
			nb = 0;
		}
		String lvl = WerewolfStats.getLevelForKills(nb);
		Integer speed = WerewolfStats.speed.get(lvl);
		Integer strength = WerewolfStats.strength.get(lvl);
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, speed - 1));
		p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, strength - 1));
	}
	
	public static boolean getTransf(String name) {
		if(transf.get(name)!=null) {
			if(transf.get(name)) {
				return true;
			}
			return false;
		}
		else {
			return false;
		}
	}
	
	public static void setTransf(String name, Boolean bool) {
		transf.remove(name);
		if(bool) {
			transf.put(name, bool);
		}
	}
	
	public static int getKills(String name) {
		if(kills.get(name)!=null) {
			return kills.get(name);
		}
		else {
			return 0;
		}
	}
	
	public static void setKills(String name, Integer i) {
		kills.put(name, i);
	}
	
	public static void initWerewolf() {
		//WolvMC.addMission("werewolf.1", (double) 40, "werewolf", "Kill %goal% mobs in werewolf mode", "Fast Mining II in werewolf mode");
	 	//WolvMC.addMission("werewolf.2", (double) 15, "werewolf", "Turn into werewolf %goal% times naturally", "Resistance II in werewolf mode");
	 	WolvMC.addRace("werewolf", ChatColor.GRAY + "Werewolf", new ItemStack(Material.WOOL, 1, (short) 7));
		WolvMC.addMission("werewolf.1", (double) 40, "werewolf", "Tuer %goal% mobs à main nue en étant transformé", "Minage rapide II en mode Loup-Garou");
	 	WolvMC.addMission("werewolf.2", (double) 15, "werewolf", "Se transformer %goal% fois naturellement", "Résistance II en mode Loup-Garou");
	 	WolvMC.getPlugin(WolvMC.class).getLogger().fine("Werewolf Class loaded!");
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    	final int CHECKPERIOD = 10;
        scheduler.scheduleSyncRepeatingTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
            @Override
            public void run() {
            	World w = WolvMC.getPlugin(WolvMC.class).getServer().getWorld("world");
				final int tick = (int) w.getTime();
				if (lastTick == tick) {
					if (lastTick + CHECKPERIOD * 2 < tick || lastTick > tick && lastTick - 24000 + CHECKPERIOD * 2 < tick) { // time changed, e.g. by a command or plugin
						lastTick = (tick - CHECKPERIOD) % 24000;
					}
					final boolean midnight = lastTick > tick;
					if (midnight) {
						lastTick -= 24000;
					}
				}
				else if(tick>14000 && lastTick<=14000) {
					Werewolf.at20();
				}
				else if(lastTick > tick) {
					Werewolf.at6();
				}
				lastTick = tick;
				return;
			}
        }, 0L, CHECKPERIOD);
	}
	
	@EventHandler
	public void reloadWerewolf(WolvMCReloadEvent e) {
		File file = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/werewolf.yml");
		if(!file.exists()) {
			  InputStream stream = WolvMC.class.getClassLoader().getResourceAsStream("fr/nashoba24/wolvmc/config/werewolf.yml");
			  FileOutputStream fos = null;
			  try {
			      fos = new FileOutputStream(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/werewolf.yml");
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
		WerewolfStats.speed.clear();
		WerewolfStats.strength.clear();
		WerewolfStats.tools.clear();
		WerewolfStats.levels.clear();
		WerewolfStats.levels.put("1", 0);
		WerewolfStats.speed.put("1", 1);
		WerewolfStats.strength.put("1", 1);
		WerewolfStats.tools.put("1", 0);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if(config.isSet("cooldown-howl")) {
			Werewolf.cooldownHowl = config.getInt("cooldown-howl");
		}
		if(config.isSet("enabled")) {
			Werewolf.enabled = config.getBoolean("enabled");
		}
		if(config.isSet("transf-time-diviser")) {
			WerewolfStats.diviser = config.getDouble("transf-time-diviser");
		}
		ConfigurationSection sect = config.getConfigurationSection("levels");
		Set<String> keys = sect.getKeys(false);
		for(String key : keys) {
			ConfigurationSection sect2 = config.getConfigurationSection("levels." + key);
			WerewolfStats.speed.put(key, sect2.getInt("speed"));
			WerewolfStats.strength.put(key, sect2.getInt("strength"));
			WerewolfStats.tools.put(key, sect2.getInt("tools"));
			WerewolfStats.levels.put(key, sect2.getInt("kills"));
		}
		if(config.isSet("damage-walk-orchid")) {
			Werewolf.walkOrchid = config.getBoolean("damage-walk-orchid");
		}
		if(config.isSet("cant-break-orchid")) {
			Werewolf.breakOrchid = config.getBoolean("cant-break-orchid");
		}
		if(config.isSet("damage-by-orchid-attack")) {
			Werewolf.damageByOrchid = config.getBoolean("damage-by-orchid-attack");
		}
		if(config.isSet("invincible-for-mobs")) {
			Werewolf.invincibleMobs = config.getBoolean("invincible-for-mobs");
		}
		if(config.isSet("cant-shoot-arrow")) {
			Werewolf.shootArrow = config.getBoolean("cant-shoot-arrow");
		}
		if(config.isSet("eat-bones")) {
			Werewolf.eatBones = config.getBoolean("eat-bones");
		}
		if(config.isSet("howl")) {
			Werewolf.howlPower = config.getBoolean("howl");
		}
		if(config.isSet("cant-sleep")) {
			Werewolf.cantSleepPower = config.getBoolean("cant-sleep");
		}
	}
}
