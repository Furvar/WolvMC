package fr.nashoba24.wolvmc.races;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.events.WolvMCInitEffectsEvent;
import fr.nashoba24.wolvmc.events.WolvMCReloadEvent;

public class Vampire implements Listener {
	
	static HashMap<String, Boolean> vampMode = new HashMap<String, Boolean>();
	static HashMap<String, Integer> sneak = new HashMap<String, Integer>();
	static HashMap<String, Boolean> brume = new HashMap<String, Boolean>();
	public HashMap<String, Long> fiole = new HashMap<String, Long>();
	public HashMap<String, Long> blood = new HashMap<String, Long>();
	public HashMap<String, Long> quick = new HashMap<String, Long>();
	public HashMap<String, Long> quickbug = new HashMap<String, Long>();
	public HashMap<String, Long> lookbug = new HashMap<String, Long>();
	public HashMap<String, Long> bloodbug = new HashMap<String, Long>();
	public static String batTransf = ChatColor.GREEN + "You became a bat!";
	public static String thirtySecsLeft = ChatColor.RED + "You will become a human again in 30 seconds!";
	public static String noLongerBat = ChatColor.RED + "You are no longer a bat!";
	public static String canTransfAgain = ChatColor.GREEN + "You can become a bat again!";
	public static String emptySlot = ChatColor.RED + "You don't have empty slot in your inventory!";
	public static String tooManyBottle = ChatColor.RED + "You have too many blood bottles!";
	public static String notEnoughBlood = ChatColor.RED + "Your victim has not enough blood!";
	public static String bloodBottle = ChatColor.RED + "Blood " + ChatColor.WHITE + "bottle";
	public static String notHungry = ChatColor.RED + "You are not hungry!";
	public static String bloodVessel = ChatColor.GREEN + "Blood in %player%'s vessels";
	public static String bloodSucked = ChatColor.GREEN + "You have sucked the blood of %player%!";
	public static String cantSleep = ChatColor.RED + "You can't sleep because you are a vampire!";
	static Integer batTime = 120;
	static Integer cooldownBat = 30;
	static Double fallModifier = 2.5;
	static Double woodSwordModifier = 4.0;
	static Integer cooldownFiole = 60;
	static Integer fioleHunger = 6;
	static Integer fioleLimit = 3;
	static Integer cooldownSuck = 20;
	static Integer hungerSuckPlayer = 6;
	static Integer hungerSuckMobs = 2;
	public static Integer effSpeed = 2;
	public static Integer effStrength = 2;
	public static Integer effDiging = 1;
	static Integer effWeakness = 2;
	public static boolean enabled = true;
	static boolean libs = WolvMC.canDis();
	static boolean invincibleMobs = true;
	static boolean batPower = true;
	static boolean invokeRain = true;
	static boolean invinciblePoisonWither = true;
	static boolean fiolePower = true;
	static boolean suckBlood = true;
	static boolean lookBlood = true;
	static boolean eatNoBlood = true;
	static boolean sleepPower = true;
	public static boolean vampireCMD = true;
	public static boolean brumePower = true;
	
	@EventHandler
	public void onInitEffects(WolvMCInitEffectsEvent e) {
		Player p = e.getPlayer();
		if(e.getRace().equals("vampire")) {
			p.removePotionEffect(PotionEffectType.JUMP);
			p.removePotionEffect(PotionEffectType.HUNGER);
			p.setAllowFlight(false);
			Vampire.setVampMode(p.getName(), false);
			sneak.remove(p.getName());
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, effSpeed - 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, effStrength - 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2147483647, effDiging - 1));
			p.setHealthScale(20);
			if(WolvMC.hasFinishMission("vampire.1", p.getName())) { p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 2147483647, 1)); }
			p.removePotionEffect(PotionEffectType.INVISIBILITY);
			Vampire.setBrumeMode(p.getName(), false);
			}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(!invincibleMobs) {
			return;
		}
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(WolvMC.getRace(p.getName()).equals("vampire")) {
				if((e.getDamager() instanceof Zombie || e.getDamager() instanceof Spider) && WolvMC.getRace(((Player) e.getEntity()).getName()).equals("vampire")) {
					e.setCancelled(true);
				}
				else if(e.getDamager() instanceof Projectile) {
					Projectile proj = (Projectile) e.getDamager();
					if(proj.getShooter() instanceof LivingEntity) {
						EntityType t = ((LivingEntity) proj.getShooter()).getType();
						if(t==EntityType.ZOMBIE || t==EntityType.SKELETON || t==EntityType.SPIDER) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBat(PlayerToggleSneakEvent e) {
		if(!libs || !batPower) {
			return;
		}
		final Player p = e.getPlayer();
		if(p.isSneaking()) {
			if(!Vampire.getVampMode(p.getName()) && WolvMC.getRace(p.getName()).equals("vampire")) {
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
					p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 0);
					MobDisguise mobDisguise = new MobDisguise(DisguiseType.BAT);
					DisguiseAPI.disguiseToAll(p, mobDisguise);
					p.sendMessage(batTransf);
					sneak.put(p.getName(), 4);
					WolvMC.addNbToMission("vampire.3", p.getName(), (double) 1);
					p.setAllowFlight(true);
					p.setHealthScale(8);
			        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			        if(!WolvMC.hasFinishMission("vampire.3", p.getName())) {
				        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
				            @Override
				            public void run() {
				            	p.sendMessage(thirtySecsLeft);
				            	
				            }
				        }, (batTime - 30) * 20);
				        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
				            @Override
				            public void run() {
				            	if(sneak.get(p.getName())!=null) {
				            		if(sneak.get(p.getName())==4) {
					            		DisguiseAPI.undisguiseToAll(p);
					            		p.setAllowFlight(false);
					            		p.sendMessage(noLongerBat);
					            		sneak.put(p.getName(), 6);
					            		p.setHealthScale(20);
				            		}
				            	}
				            }
				        }, batTime * 20);
				        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
				            @Override
				            public void run() {
				            	if(sneak.get(p.getName())!=null) {
				            		if(sneak.get(p.getName())==6) {
				            			sneak.remove(p.getName());
				            			p.sendMessage(canTransfAgain);
				            		}
				            	}
				            }
				        }, (batTime + cooldownBat) * 20);
			        }
			        else {
				        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
				            @Override
				            public void run() {
				            	p.sendMessage(thirtySecsLeft);
				            	
				            }
				        }, batTime * 20);
				        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
				            @Override
				            public void run() {
				            	if(sneak.get(p.getName())!=null) {
				            		if(sneak.get(p.getName())==4) {
					            		DisguiseAPI.undisguiseToAll(p);
					            		p.setAllowFlight(false);
					            		p.sendMessage(noLongerBat);
					            		sneak.put(p.getName(), 6);
					            		p.setHealthScale(20);
				            		}
				            	}
				            }
				        }, (batTime + 30) * 20);
				        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
				            @Override
				            public void run() {
				            	if(sneak.get(p.getName())!=null) {
				            		if(sneak.get(p.getName())==6) {
				            			sneak.remove(p.getName());
				            			p.sendMessage(canTransfAgain);
				            		}
				            	}
				            }
				        }, (batTime + cooldownBat) * 20);
			        }
				}
			}
		}
	}
	
	@EventHandler
	public void onInvokeRain(PlayerInteractEvent e) {
		if(!invokeRain) {
			return;
		}
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("vampire") && e.getAction()==Action.RIGHT_CLICK_AIR && e.getItem()!=null) {
			if(e.getItem().getType()==Material.WATER_BUCKET) {
				p.getWorld().setStorm(true);
				if(e.getHand().equals(EquipmentSlot.OFF_HAND)) {
					p.getInventory().setItemInOffHand(null);
				}
				else if(e.getHand().equals(EquipmentSlot.HAND)) {
					p.getInventory().setItemInMainHand(null);
				}
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(WolvMC.getRace(p.getName()).equals("vampire")) {
				if(sneak.get(p.getName())!=null) {
					if(sneak.get(p.getName())==6) {
						e.setDamage(e.getDamage() * 2.5);
					}
				}
				if((e.getCause() == DamageCause.POISON || e.getCause() == DamageCause.WITHER) && invinciblePoisonWither) {
					e.setCancelled(true);
				}
				if(e.getCause() == DamageCause.FALL) {
					e.setDamage(e.getDamage() / fallModifier);
				}
			}
		}
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(WolvMC.getRace(p.getName()).equals("vampire")) {
				if(e.getCause() == DamageCause.ENTITY_ATTACK && e.getDamager() instanceof Player) {
					Player damager = (Player) e.getDamager();
					if(damager.getInventory().getItemInMainHand().getType()==Material.WOOD_SWORD) {
						if(WolvMC.hasFinishMission("vampire.2", p.getName())) {
							e.setDamage(e.getDamage() * (woodSwordModifier / 2));
						}
						else {
							e.setDamage(e.getDamage() * woodSwordModifier);
						}
					}
				}
			}
		}
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if(WolvMC.getRace(p.getName()).equals("vampire") && Vampire.getBrumeMode(p.getName())==true) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBrumeBreak(BlockBreakEvent e) {
		if(WolvMC.getRace(e.getPlayer().getName()).equals("vampire") && Vampire.getBrumeMode(e.getPlayer().getName())==true) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFioleFill(PlayerInteractEntityEvent e) { //== NON TESTÉ ==
		if(!fiolePower) {
			return;
		}
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("vampire") && e.getRightClicked() instanceof Player && WolvMC.canUsePowerSafe(p.getLocation(), p)) {
			Player rc = (Player) e.getRightClicked();
			ItemStack is = null;
			if(e.getHand()==EquipmentSlot.HAND) {
				is = p.getInventory().getItemInMainHand();
			}
			else if(e.getHand()==EquipmentSlot.OFF_HAND) {
				is = p.getInventory().getItemInOffHand();
			}
			if(is.getType()==Material.GLASS_BOTTLE) {
			       int cooldownTime = cooldownFiole;
			        if(fiole.containsKey(p.getName())) {
			            long secondsLeft = ((fiole.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
			            if(secondsLeft>0) {
			            	int seconds = (int) secondsLeft;
			            	p.sendMessage(WolvMC.msgCooldown(seconds));
			                return;
			            }
			        }
			        if(rc.getFoodLevel()>fioleHunger) {
			        	Integer blood = 0;
			        	boolean air = false;
			        	for(int i = 0 ; i < p.getInventory().getSize() ; i++) {
			        		if(!(i>=36 && i<=40)) {
				        	    ItemStack item = p.getInventory().getItem(i);
				        	    if(item==null) {
				        	    	air = true;
				        	    }
				        	    else {
					        	    if(item.getItemMeta().getDisplayName() == null) {
					        	    	
					        	    }
					        	    else {
					        	    	if(item.getItemMeta().getDisplayName().equals(bloodBottle)) {
					        	    		++blood;
					        	    	}
					        	    }
					        	    if(item.getType()==Material.AIR) {
					        	    	air = true;
					        	    }
				        	    }
			        		}
			        	}
			        	if(blood<=fioleLimit) {
			        		if(air) {
				        		if(is.getAmount()==1) {
				        			if(e.getHand()==EquipmentSlot.HAND) {
				        				p.getInventory().getItemInMainHand().setType(Material.AIR);
				        			}
				        			else if(e.getHand()==EquipmentSlot.OFF_HAND) {
				        				p.getInventory().getItemInOffHand().setType(Material.AIR);
				        			}
				        		}
				        		else {
				        			if(e.getHand()==EquipmentSlot.HAND) {
				        				p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
				        			}
				        			else if(e.getHand()==EquipmentSlot.OFF_HAND) {
				        				p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount() - 1);
				        			}
				        		}
				        		rc.setFoodLevel(rc.getFoodLevel() - fioleHunger);
				        		ItemStack itemFiole = new ItemStack(Material.POTION, 1, (short) 8201);
				        		ItemMeta meta = itemFiole.getItemMeta();
				        		meta.setDisplayName(bloodBottle);
				        		itemFiole.setItemMeta(meta);
				        		itemFiole.setDurability((short) 8201);
				        		p.getInventory().addItem(itemFiole);
			        		}
				        	else {
				        		p.sendMessage(emptySlot);
				        		return;
				        	}
			        	}
			        	else {
			        		p.sendMessage(tooManyBottle);
			        		return;
			        	}
			        }
			        else {
			        	p.sendMessage(notEnoughBlood);
			        	return;
			        }
			        fiole.put(p.getName(), System.currentTimeMillis());
			        
			}
		}
	}
	
	@EventHandler
	public void onFioleConsume(PlayerItemConsumeEvent e) {
		if(!fiolePower) {
			return;
		}
		Player p = e.getPlayer();
		if(e.getItem().hasItemMeta()) {
			if(e.getItem().getItemMeta().getDisplayName()!=null) {
				if(WolvMC.getRace(p.getName()).equals("vampire") && e.getItem().getItemMeta().getDisplayName().equals(bloodBottle)) {
					if(p.getFoodLevel()<20) {
						e.setCancelled(true);
						if(p.getFoodLevel()> (20 - fioleHunger)) {
							Integer food = 20 - p.getFoodLevel();
							food = fioleHunger - food;
							p.setSaturation(food);
						}
						p.setFoodLevel(p.getFoodLevel() + fioleHunger);
						if(p.getInventory().getItemInMainHand()!=null) {
							if(p.getInventory().getItemInMainHand().hasItemMeta()) {
								if(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()!=null) {
									if(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(bloodBottle)) {
										p.getInventory().setItemInMainHand(new ItemStack(Material.GLASS_BOTTLE, 1));
									}
								}
							}
						}
						else if(p.getInventory().getItemInOffHand()!=null) {
							if(p.getInventory().getItemInOffHand().hasItemMeta()) {
								if(p.getInventory().getItemInOffHand().getItemMeta().getDisplayName()!=null) {
									if(p.getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals(bloodBottle)) {
										p.getInventory().setItemInOffHand(new ItemStack(Material.GLASS_BOTTLE, 1));
									}
								}
							}
						}
					}
					else {
						p.sendMessage(notHungry);
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onSuckBlood(PlayerInteractEntityEvent e) {
		if(!suckBlood) {
			return;
		}
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("vampire") && p.isSneaking() && p.getFoodLevel()<20) {
			if(e.getHand().equals(EquipmentSlot.HAND)) {
				if(p.getInventory().getItemInMainHand()==null) {
					return;
				}
			}
			else {
				if(p.getInventory().getItemInOffHand()==null) {
					return;
				}
			}
			if(e.getRightClicked() instanceof Player) {
			       int cooldownTime = cooldownSuck;
			        if(blood.containsKey(p.getName())) {
						Long bug = bloodbug.get(p.getName());
						if(System.currentTimeMillis() - bug < 20) {
							return;
						}
			            long secondsLeft = ((blood.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
			            if(secondsLeft>0) {
			            	bloodbug.put(p.getName(), System.currentTimeMillis());
			            	int seconds = (int) secondsLeft;
			            	p.sendMessage(WolvMC.msgCooldown(seconds));
			                return;
			            }
			        }
			        if(!WolvMC.canUsePowerSafe(e.getRightClicked().getLocation(), p)) {
			        	bloodbug.put(p.getName(), System.currentTimeMillis());
			        	blood.put(p.getName(), System.currentTimeMillis() - cooldownTime * 1001);
			        	return;
			        }
			        blood.put(p.getName(), System.currentTimeMillis());
			        bloodbug.put(p.getName(), System.currentTimeMillis());
					if(p.getFoodLevel()> (20 - hungerSuckPlayer)) {
						Integer food = 20 - p.getFoodLevel();
						food = hungerSuckPlayer - food;
						p.setSaturation(food);
						p.setFoodLevel(20);
					}
					else {
						p.setFoodLevel(p.getFoodLevel() + hungerSuckPlayer);
					}
					if((((Player) e.getRightClicked()).getFoodLevel() - hungerSuckPlayer) < 0) {
						((Player) e.getRightClicked()).setFoodLevel(0);
					}
					else {
						((Player) e.getRightClicked()).setFoodLevel(((Player) e.getRightClicked()).getFoodLevel() - hungerSuckPlayer);
					}
					WolvMC.addNbToMission("vampire.1", p.getName(), (double) 1);
					blood.put(p.getName(), System.currentTimeMillis());
					bloodbug.put(p.getName(), System.currentTimeMillis());
					p.sendMessage(bloodSucked.replaceAll("%player%", ((Player) e.getRightClicked()).getName()));
					((Player) e.getRightClicked()).getWorld().playEffect(((Player) e.getRightClicked()).getLocation().add(0, 1, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
			}
			else {
				if(e.getRightClicked() instanceof LivingEntity){
					if(!WolvMC.canUsePowerSafe(p.getLocation())) {
						return;
					}
					LivingEntity rc = (LivingEntity) e.getRightClicked();
					if(p.getFoodLevel()> (20 - hungerSuckMobs)) {
						Integer food = 20 - p.getFoodLevel();
						food = hungerSuckMobs - food;
						p.setSaturation(food);
					}
					p.setFoodLevel(p.getFoodLevel() + hungerSuckMobs);
					if(rc.getHealth() > hungerSuckMobs) {
						rc.damage(hungerSuckMobs);
					}
					else {
						rc.remove();
					}
					rc.getWorld().playEffect(rc.getLocation().add(0, 1, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
				}
			}
		}
	}
	
	@EventHandler
	public void onLookBlood(PlayerInteractEntityEvent e) {
		if(!lookBlood) {
			return;
		}
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("vampire") && e.getRightClicked() instanceof Player && p.isSneaking()) {
			if(e.getHand().equals(EquipmentSlot.HAND)) {
				if(p.getInventory().getItemInMainHand()==null) {
					return;
				}
			}
			else {
				if(p.getInventory().getItemInOffHand()==null) {
					return;
				}
			}
	        if(lookbug.containsKey(p.getName())) {
				Long bug = lookbug.get(p.getName());
				if(System.currentTimeMillis() - bug < 20) {
					return;
				}
	        }
	        lookbug.put(p.getName(), System.currentTimeMillis());
			p.sendMessage(bloodVessel.replaceAll("%player%", ((Player) e.getRightClicked()).getName()) + ": " + ((Player) e.getRightClicked()).getFoodLevel() + "/20");
		}
	}
	
	@EventHandler
	public void onEatNoBlood(PlayerItemConsumeEvent e) {
		if(!eatNoBlood) {
			return;
		}
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("vampire")) {
			Material is = e.getItem().getType();
			if(is!=Material.MILK_BUCKET && is!=Material.POTION && is!=Material.GOLDEN_APPLE) {
				p.damage(p.getHealth() - 1);
				p.setFoodLevel(0);
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(WolvMC.getRace(p.getName()).equals("vampire") && Vampire.getVampMode(p.getName())==true && p.getKiller() instanceof Player) {
				WolvMC.addNbToMission("vampire.4", p.getName(), (double) 1);
			}
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if(e.getEntity().getKiller() instanceof Player) {
			Player p = (Player) e.getEntity().getKiller();
			if(WolvMC.getRace(p.getName()).equals("vampire") && e.getEntity() instanceof Player && p.getInventory().getItemInMainHand().getType()==Material.WOOD_SWORD) {
				WolvMC.addNbToMission("vampire.2", p.getName(), (double) 1);
			}
		}
	}
	
	@EventHandler
	public void onQuickTransf(PlayerInteractEvent e) {
		if(!vampireCMD) {
			return;
		}
		Player p = e.getPlayer();
		if((e.getAction()==Action.RIGHT_CLICK_BLOCK || e.getAction()==Action.RIGHT_CLICK_AIR) && (p.getInventory().getItemInOffHand().getType()==Material.AIR || p.getInventory().getItemInOffHand()==null) 
				&& (p.getInventory().getItemInMainHand().getType()==Material.AIR || p.getInventory().getItemInMainHand()==null)) {
			if(p.getPlayer().getInventory().getHeldItemSlot()==8) {
				if(WolvMC.getRace(p.getName()).equals("vampire")) {
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
					        WolvMC.getPlugin(WolvMC.class).getServer().dispatchCommand(p, "vampire");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBedEnter(PlayerBedEnterEvent e) {
		if(!sleepPower) {
			return;
		}
		if(WolvMC.getRace(e.getPlayer().getName()).equals("vampire")) {
			e.getPlayer().sendMessage(cantSleep);
			e.setCancelled(true);
		}
	}
	
	public static boolean getVampMode(String name) {
		if(vampMode.get(name)!=null) {
			if(vampMode.get(name)) {
				return true;
			}
			return false;
		}
		else {
			return false;
		}
	}
	
	public static void setVampMode(String name, Boolean bool) {
		vampMode.remove(name);
		if(bool) {
			vampMode.put(name, bool);
		}
	}
	
	public static boolean getBrumeMode(String name) {
		if(brume.get(name)!=null) {
			if(brume.get(name)) {
				return true;
			}
			return false;
		}
		else {
			return false;
		}
	}
	
	public static void setBrumeMode(String name, Boolean bool) {
		brume.remove(name);
		if(bool) {
			brume.put(name, bool);
		}
	}
	
	public static void initVampire() {
		//WolvMC.addMission("vampire.1", (double) 10, "vampire", "Suck player's blood %goal% times", "Night Vision");
	 	//WolvMC.addMission("vampire.2", (double) 20, "vampire", "Kill %goal% players with a wooden sword", "Damage by wooden swords divided by 2");
		//WolvMC.addMission("vampire.3", (double) 50, "vampire", "Become %goal% times a bat", "30 seconds added to bat mode");
		//WolvMC.addMission("vampire.4", (double) 25, "vampire", "Get killed %goal% times in vampire mode", "No hunger in vampire mode");
		WolvMC.addRace("vampire", ChatColor.RED + "Vampire", new ItemStack(Material.REDSTONE, 1));
		WolvMC.addMission("vampire.1", (double) 10, "vampire", "Sucer le sang de %goal% joueurs", "Vision nocturne");
	 	WolvMC.addMission("vampire.2", (double) 20, "vampire", "Tuer %goal% joueurs avec une épée en bois", "Dégâts par les épées en bois réduits par 2");
		WolvMC.addMission("vampire.3", (double) 50, "vampire", "Se transformer %goal% fois en chauve-souris", "30 secondes de plus en chauve-souris");
		WolvMC.addMission("vampire.4", (double) 25, "vampire", "Se faire tuer %goal% fois en mode vampire", "Pas de faim en mode vampire");
		WolvMC.getPlugin(WolvMC.class).getLogger().fine("Vampire Class loaded!");
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
            @Override
            public void run() {
    			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
    				if(WolvMC.getRace(p.getName()).equals("vampire") && p.getLocation().getY()>0) {
    					if(p.getLocation().getBlock().getLightFromSky()>14) {
    						if(!p.getWorld().hasStorm()) {
    							if(p.getWorld().getTime()>0 && p.getWorld().getTime()<12000) {
    								if(!p.hasPotionEffect(PotionEffectType.WEAKNESS)) {
    									p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 2147483647, effWeakness - 1));
    								}
    							}
    							else if (p.hasPotionEffect(PotionEffectType.WEAKNESS)) {
    								p.removePotionEffect(PotionEffectType.WEAKNESS);
    							}
    						}
							else if (p.hasPotionEffect(PotionEffectType.WEAKNESS)) {
								p.removePotionEffect(PotionEffectType.WEAKNESS);
							}
    					}
						else if (p.hasPotionEffect(PotionEffectType.WEAKNESS)) {
							p.removePotionEffect(PotionEffectType.WEAKNESS);
						}
    				}
    			}
            }
        }, 0L, 20L);
	}
	
	@EventHandler
	public void reloadVampire(WolvMCReloadEvent e) {
		File file = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/vampire.yml");
		if(!file.exists()) {
			  InputStream stream = WolvMC.class.getClassLoader().getResourceAsStream("fr/nashoba24/wolvmc/config/vampire.yml");
			  FileOutputStream fos = null;
			  try {
			      fos = new FileOutputStream(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/vampire.yml");
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
		if(config.isSet("bat-time")) {
			Vampire.batTime = config.getInt("bat-time");
		}
		if(config.isSet("bat-cooldown")) {
			Vampire.cooldownBat = config.getInt("bat-cooldown");
		}
		if(config.isSet("fall-modifier")) {
			Vampire.fallModifier = config.getDouble("fall-modifier");
		}
		if(config.isSet("wooden-sword-damages-modifier")) {
			Vampire.woodSwordModifier = config.getDouble("wooden-sword-damages-modifier");
		}
		if(config.isSet("blood-bottle-cooldown")) {
			Vampire.cooldownFiole = config.getInt("blood-bottle-cooldown");
		}
		if(config.isSet("blood-bottle-hunger")) {
			Vampire.fioleHunger = config.getInt("blood-bottle-hunger");
		}
		if(config.isSet("blood-bottle-limit")) {
			Vampire.fioleLimit = config.getInt("blood-bottle-limit");
		}
		if(config.isSet("suck-cooldown")) {
			Vampire.cooldownSuck = config.getInt("suck-cooldown");
		}
		if(config.isSet("suck-player-hunger")) {
			Vampire.hungerSuckPlayer = config.getInt("suck-player-hunger");
		}
		if(config.isSet("suck-mobs-hunger")) {
			Vampire.hungerSuckMobs = config.getInt("suck-mobs-hunger");
		}
		if(config.isSet("effect-speed")) {
			Vampire.effSpeed = config.getInt("effect-speed");
		}
		if(config.isSet("effect-strength")) {
			Vampire.effStrength = config.getInt("effect-strength");
		}
		if(config.isSet("effect-fast-diging")) {
			Vampire.effDiging = config.getInt("effect-fast-diging");
		}
		if(config.isSet("effect-weakness")) {
			Vampire.effWeakness = config.getInt("effect-weakness");
		}
		if(config.isSet("enabled")) {
			Vampire.enabled = config.getBoolean("enabled");
		}
		if(config.isSet("invincible-to-spider-zombie")) {
			Vampire.invincibleMobs = config.getBoolean("invincible-to-spider-zombie");
		}
		if(config.isSet("bat")) {
			Vampire.batPower = config.getBoolean("bat");
		}
		if(config.isSet("invoke-rain")) {
			Vampire.invokeRain = config.getBoolean("invoke-rain");
		}
		if(config.isSet("invincible-to-poison-wither")) {
			Vampire.invinciblePoisonWither = config.getBoolean("invincible-to-poison-wither");
		}
		if(config.isSet("fiole-fill-and-eat")) {
			Vampire.fiolePower = config.getBoolean("fiole-fill-and-eat");
		}
		if(config.isSet("suck-blood")) {
			Vampire.suckBlood = config.getBoolean("suck-blood");
		}
		if(config.isSet("look-blood")) {
			Vampire.lookBlood = config.getBoolean("look-blood");
		}
		if(config.isSet("damage-on-eat-no-blood")) {
			Vampire.eatNoBlood = config.getBoolean("damage-on-eat-no-blood");
		}
		if(config.isSet("cant-sleep")) {
			Vampire.sleepPower = config.getBoolean("cant-sleep");
		}
		if(config.isSet("vampire-command")) {
			Vampire.vampireCMD = config.getBoolean("vampire-command");
		}
		if(config.isSet("brume")) {
			Vampire.brumePower = config.getBoolean("brume");
		}
	}
}
