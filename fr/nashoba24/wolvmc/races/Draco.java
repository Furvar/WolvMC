package fr.nashoba24.wolvmc.races;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.events.WolvMCInitEffectsEvent;
import fr.nashoba24.wolvmc.utils.ArmorEquipEvent;
import fr.nashoba24.wolvmc.utils.TitleAPI;

public class Draco implements Listener {

	public static boolean enabled = true;
	static Integer resistance = 4;
	static Integer strength = 3;
	static HashMap<String, Long> healcoal = new HashMap<String, Long>();
	static HashMap<String, Long> healcoalbug = new HashMap<String, Long>();
	static HashMap<String, Long> wither = new HashMap<String, Long>();
	static HashMap<String, Long> witherbug = new HashMap<String, Long>();
	static ArrayList<String> glide = new ArrayList<String>();
	static String healMSG = ChatColor.GREEN + "Vous vous êtes soigné!";
	static String witherMSG = ChatColor.GREEN + "Vous avez donné l'effet Wither à %entity% pour 10 secondes";
	
	@EventHandler
	public void onInitEffects(WolvMCInitEffectsEvent e) {
		Player p = e.getPlayer();
		if(e.getRace().equals("draco")) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, resistance - 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, strength - 1));
		}
	}
	
	@EventHandler
	public void onEquip(ArmorEquipEvent e) {
		if(e.getNewArmorPiece()!=null) {
			if(e.getNewArmorPiece().getType()!=Material.SKULL && e.getNewArmorPiece().getType()!=Material.SKULL_ITEM && e.getNewArmorPiece().getType()!=Material.ELYTRA) {
				if(WolvMC.getRace(e.getPlayer().getName()).equals("draco")) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void damageDraco(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(WolvMC.getRace(p.getName()).equals("draco")) {
				if(e.getDamager() instanceof LivingEntity) {
					if(((LivingEntity) e.getDamager()).getEquipment().getItemInMainHand()==null) {
						((LivingEntity) e.getDamager()).damage(1);
					}
					else if(((LivingEntity) e.getDamager()).getEquipment().getItemInMainHand().getType()==Material.GOLD_SWORD) {
						e.setDamage(e.getFinalDamage() * 2);
					}
				}
				if(e.getDamager() instanceof Projectile) {
					Projectile ent = (Projectile) e.getDamager();
					if(ent.getType()==EntityType.ARROW) {
						e.setDamage(e.getFinalDamage() * 1.5);
					}
					else if(ent.getType()==EntityType.SNOWBALL) {
						e.setDamage(4);
					}
				}
				if(e.getCause()==DamageCause.POISON || e.getCause()==DamageCause.WITHER || e.getCause()==DamageCause.FIRE_TICK || e.getCause()==DamageCause.FIRE) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onHealCoal(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction()==Action.RIGHT_CLICK_AIR) {
			if(e.getItem()!=null && p.getHealth()<p.getMaxHealth()) {
				if(WolvMC.getRace(p.getName()).equals("draco") && e.getItem().getType()==Material.COAL) {
				       int cooldownTime = 600;
				        if(healcoal.containsKey(p.getName())) {
							Long bug = healcoalbug.get(p.getName());
							if(System.currentTimeMillis() - bug < 20) {
								return;
							}
				            long secondsLeft = ((healcoal.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
				            if(secondsLeft>0) {
				            	healcoalbug.put(p.getName(), System.currentTimeMillis());
				            	int seconds = (int) secondsLeft;
				            	p.sendMessage(WolvMC.msgCooldown(seconds));
				                return;
				            }
				        }
						if(e.getHand().equals(EquipmentSlot.HAND)) {
							if(e.getItem().getAmount()==1) {
								p.getInventory().setItemInMainHand(null);
							}
							else {
								p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
							}
						}
						else if(e.getHand().equals(EquipmentSlot.OFF_HAND)){
							if(e.getItem().getAmount()==1) {
								p.getInventory().setItemInOffHand(null);
							}
							else {
								p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount() - 1);
							}
						}
				        p.setHealth(p.getMaxHealth());
				        healcoal.put(p.getName(), System.currentTimeMillis());
				        healcoalbug.put(p.getName(), System.currentTimeMillis());
				        p.sendMessage(healMSG);
				        WolvMC.addNbToMission("draco.1", p.getName(), 1.00);
				        p.updateInventory();
				}
			}
		}
	}
	
	@EventHandler
	public void onFusRoDah(final AsyncPlayerChatEvent e) {
		if(e.getMessage().equalsIgnoreCase("fus ro dah") || e.getMessage().equalsIgnoreCase("fus ro dah!")) {
			if(WolvMC.getRace(e.getPlayer().getName()).equals("draco")) {
				if(WolvMC.hasFinishMission("draco.2", e.getPlayer().getName())) {
					e.setCancelled(true);
					if(WolvMC.canUsePowerSafe(e.getPlayer().getLocation(), e.getPlayer())) {
						Vector dir = e.getPlayer().getEyeLocation().getDirection();
						dir.setY(0);
						dir = dir.multiply(5);
						final Vector push = dir;
						List<Entity> list = e.getPlayer().getNearbyEntities(5, 5, 5);
						BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
				        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
				            @Override
				            public void run() {
				            	TitleAPI.sendTitle(e.getPlayer(), 1, 9, 0, ChatColor.DARK_BLUE + "FUS", "");
				            	e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
				            }
				        }, 10L);
				        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
				            @Override
				            public void run() {
				            	TitleAPI.sendTitle(e.getPlayer(), 0, 10, 0, ChatColor.DARK_BLUE + "RO", "");
				            	e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
				            }
				        }, 20L);
				        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
				            @Override
				            public void run() {
				            	TitleAPI.sendTitle(e.getPlayer(), 0, 20, 5, ChatColor.DARK_BLUE + "DAH!", "");
				            	e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
				            }
				        }, 30L);
						for(final Entity ent : list) {
							if(ent instanceof Player) {
						        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
						            @Override
						            public void run() {
						            	TitleAPI.sendTitle((Player) ent, 1, 9, 0, ChatColor.DARK_BLUE + "FUS", "");
						            	((Player) ent).playSound(ent.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
						            }
						        }, 10L);
						        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
						            @Override
						            public void run() {
						            	TitleAPI.sendTitle((Player) ent, 0, 10, 0, ChatColor.DARK_BLUE + "RO", "");
						            	((Player) ent).playSound(ent.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
						            }
						        }, 20L);
						        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
						            @Override
						            public void run() {
						            	TitleAPI.sendTitle((Player) ent, 0, 20, 5, ChatColor.DARK_BLUE + "DAH!", "");
						            	ent.setVelocity(push);
						            	((Player) ent).playSound(ent.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
						            }
						        }, 30L);
							}
							else {
						        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
						            @Override
						            public void run() {
						            	ent.setVelocity(push);
						            }
						        }, 30L);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onToolChange(PlayerItemHeldEvent e) {
		final Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("draco")) {
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
	            @Override
	            public void run() {
	            	if(p.getInventory().getItemInMainHand()!=null) {
	            		if(p.getInventory().getItemInMainHand().getType()==Material.WOOD_SWORD || p.getInventory().getItemInMainHand().getType()==Material.WOOD_HOE || p.getInventory().getItemInMainHand().getType()==Material.WOOD_AXE || p.getInventory().getItemInMainHand().getType()==Material.WOOD_PICKAXE || p.getInventory().getItemInMainHand().getType()==Material.WOOD_SPADE) {
	            			p.getInventory().setItemInMainHand(null);
	            			p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE , 1.0F, 1.0F);
	            			p.updateInventory();
	            		}
	            	}
	            }
	        }, 1L);
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("draco")) {
			if(e.getItem()!=null) {
				if(e.getItem().getType()==Material.WOOD_SWORD || e.getItem().getType()==Material.WOOD_HOE || e.getItem().getType()==Material.WOOD_AXE || e.getItem().getType()==Material.WOOD_PICKAXE || e.getItem().getType()==Material.WOOD_SPADE) {
					if(e.getHand().equals(EquipmentSlot.HAND)) {
						p.getInventory().setItemInMainHand(null);
					}
					else if(e.getHand().equals(EquipmentSlot.OFF_HAND)) {
						p.getInventory().setItemInOffHand(null);
					}
					p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE , 1.0F, 1.0F);
					p.updateInventory();
				}
			}
		}
	}
	
	@EventHandler
	public void onDeathRegen(EntityDeathEvent e) {
		if(e.getEntity().getKiller() != null) {
			Player p = e.getEntity().getKiller();
			if(WolvMC.getRace(p.getName()).equals("draco")) {
				if(WolvMC.hasFinishMission("draco.1", p.getName())) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 1));
				}
			}
		}
	}
	
	@EventHandler
	public void onWitherGiveEffect(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if(e.getRightClicked() instanceof LivingEntity) {
			if(p.isSneaking() && WolvMC.getRace(p.getName()).equals("draco")) {
			       int cooldownTime = 30;
			        if(wither.containsKey(p.getName())) {
			        	if(witherbug.containsKey(p.getName())) {
							Long bug = witherbug.get(p.getName());
							if(System.currentTimeMillis() - bug < 20) {
								return;
							}
			        	}
			            long secondsLeft = ((wither.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
			            if(secondsLeft>0) {
			            	witherbug.put(p.getName(), System.currentTimeMillis());
			            	int seconds = (int) secondsLeft;
			            	p.sendMessage(WolvMC.msgCooldown(seconds));
			                return;
			            }
			        }
			        if(!WolvMC.canUsePowerSafe(e.getRightClicked().getLocation(), p)) {
			        	witherbug.put(p.getName(), System.currentTimeMillis());
			        	wither.put(p.getName(), System.currentTimeMillis() - cooldownTime * 1001);
			        	return;
			        }
			        wither.put(p.getName(), System.currentTimeMillis());
			        witherbug.put(p.getName(), System.currentTimeMillis());
			        LivingEntity ent = (LivingEntity) e.getRightClicked();
			        ent.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 1));
			        if(e.getRightClicked().getType()==EntityType.PLAYER) {
			        	p.sendMessage(witherMSG.replaceAll("%entity%", ((Player) e.getRightClicked()).getName()));
			        }
			        else {
			        	p.sendMessage(witherMSG.replaceAll("%entity%", e.getRightClicked().getType().toString()));
			        }
			}
		}
	}
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if(e.getItem().getType()==Material.SPIDER_EYE) {
			if(WolvMC.getRace(e.getPlayer().getName()).equals("draco")) {
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1200, 0));
			}
		}
	}
	
	@EventHandler
	public void onEatObsidian(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction()==Action.RIGHT_CLICK_AIR) {
			if(e.getItem()!=null) {
				if(e.getItem().getType()==Material.OBSIDIAN) {
					if(WolvMC.getRace(p.getName()).equals("draco")) {
						if(p.getFoodLevel()<20) {
							p.setFoodLevel(20);
							p.setSaturation(1.0F);
							WolvMC.addNbToMission("draco.2", p.getName(), 1.00);
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0F, 1.0F);
							if(e.getHand().equals(EquipmentSlot.HAND)) {
								if(e.getItem().getAmount()==1) {
									p.getInventory().setItemInMainHand(null);
								}
								else {
									p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
								}
							}
							else if(e.getHand().equals(EquipmentSlot.OFF_HAND)){
								if(e.getItem().getAmount()==1) {
									p.getInventory().setItemInOffHand(null);
								}
								else {
									p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount() - 1);
								}
							}
							p.updateInventory();
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlane(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		if(p.isSneaking() && p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
			if(WolvMC.getRace(p.getName()).equals("draco")) {
				if(!glide.contains(p.getName())) {
					p.setGliding(true);
					glide.add(p.getName());
					p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.0F, 1.0F);
				}
				else {
					p.setGliding(false);
					glide.remove(p.getName());
					p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.0F, 1.0F);
				}
			}
		}
	}
	
	@EventHandler
	public void onToggleGlide(EntityToggleGlideEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(p.isGliding() && glide.contains(p.getName())) {
				if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
					if(WolvMC.getRace(p.getName()).equals("draco")) {
						e.setCancelled(true);
					}
				}
				else {
					glide.remove(p.getName());
				}
			}
		}
	}
	
	public static void initDraco() {
		//WolvMC.addMission("draco.1", (double) 10, "draco", "Get healed %goal% times with coal", "3 seconds of regeneration when you kill an entity");
	 	//WolvMC.addMission("draco.2", (double) 100, "draco", "Eat %goal% obsidians", "FUS RO DAH! (write it in the chat)");
		WolvMC.addRace("draco", ChatColor.BLACK + "Draconiforme", new ItemStack(Material.COAL_BLOCK, 1));
		WolvMC.addMission("draco.1", (double) 10, "draco", "Se soigner %goal% fois avec du charbon", "3 secondes de régénération par mob tué");
	 	WolvMC.addMission("draco.2", (double) 100, "draco", "Manger %goal% obsidiennes", "FUS ROH DAH! (à écrire dans le tchat)");
		WolvMC.getPlugin(WolvMC.class).getLogger().fine("Draco Class loaded!");
	       BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncRepeatingTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
	            @Override
	            public void run() {
	    			for(Player p : WolvMC.getPlugin(WolvMC.class).getServer().getOnlinePlayers()) {
	    				if(WolvMC.getRace(p.getName()).equals("draco")) {
	      					if(p.getLocation().getBlock().getType()==Material.WATER || p.getLocation().getBlock().getType()==Material.STATIONARY_WATER) {
	      						if(p.getVehicle()==null) {
	      							p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
	      						}
	      						else if(p.getVehicle().getType()!=EntityType.BOAT) {
	      							p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
	      						}
	      						else {
	      							p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, resistance - 1));
	      						}
	      					}
	      					else {
	      						p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, resistance - 1));
	      					}
	    				}
	    			}
	            }
	        }, 0L, 20L);
	}
}
