package fr.nashoba24.wolvmc.races;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.events.WolvMCInitEffectsEvent;

public class Zorlim implements Listener {
	
	public HashMap<String, Long> blazeball = new HashMap<String, Long>();
	static HashMap<String, Integer> sneak = new HashMap<String, Integer>();
	public HashMap<String, Long> placefire = new HashMap<String, Long>();
	public HashMap<String, Boolean> confu = new HashMap<String, Boolean>();
	public HashMap<String, Long> firebug = new HashMap<String, Long>();
	public HashMap<String, Long> ignitebug = new HashMap<String, Long>();
	public HashMap<String, Long> ignite = new HashMap<String, Long>();
	public static boolean enabled = true;
	static String igniteMSG = ChatColor.GREEN + "Vous avez brûlé %entity% pour 5 secondes!";

	@EventHandler
	public void onInitEffects(WolvMCInitEffectsEvent e) {
		if(e.getRace().equals("zorlim")) {
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2147483647, 1));
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if(WolvMC.getRace(p.getName()).equals("zorlim")) {
			p.getWorld().createExplosion(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), 4.0F, false, false);
		}
	}
	
	@EventHandler
	public void onRightClickFireball(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction()==Action.RIGHT_CLICK_AIR && e.getItem()!=null) {
			if(e.getItem().getType()==Material.FIREBALL) {
				if(WolvMC.getRace(p.getName()).equals("zorlim")) {
					if(p.getInventory().getItemInMainHand()==e.getItem()) {
						if(e.getItem().getAmount()==1) {
							p.getInventory().setItemInMainHand(null);
						}
						else {
							p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
						}
					}
					else {
						if(e.getItem().getAmount()==1) {
							p.getInventory().setItemInOffHand(null);
						}
						else {
							p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount() - 1);
						}
					}
					WolvMC.addNbToMission("zorlim.2", p.getName(), 1.00);
			        Fireball fireball = p.launchProjectile(Fireball.class);
			        fireball.setVelocity(fireball.getVelocity().multiply(4));
				}
			}
		}
	}
	
	@EventHandler
	public void onLeftBlazeRod(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction()==Action.LEFT_CLICK_AIR && p.getInventory().getItemInMainHand().getType()==Material.BLAZE_ROD && WolvMC.getRace(p.getName()).equals("zorlim") && !WolvMC.canUsePowerSafe(p.getLocation(), p)) {
			if(WolvMC.canUsePowerBlock(p.getLocation())) {
		       int cooldownTime = 20;
		        if(blazeball.containsKey(p.getName())) {
		            long secondsLeft = ((blazeball.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
		            if(secondsLeft>0) {
		            	int seconds = (int) secondsLeft;
		            	p.sendMessage(WolvMC.msgCooldown(seconds));
		            	p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
		                return;
		            }
		        }
		        blazeball.put(p.getName(), System.currentTimeMillis());
		        Fireball fireball = p.launchProjectile(Fireball.class);
		        fireball.setVelocity(fireball.getVelocity().multiply(4));
			}
		}
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			if(WolvMC.getRace(((Player) e.getEntity()).getName()).equals("zorlim")) {
				if(e.getDamager() instanceof Blaze || e.getDamager() instanceof Ghast || e.getDamager() instanceof MagmaCube) {
					e.setCancelled(true);
				}
				if(e.getDamager() instanceof Player) { //== Non testé ==
					if(((Player) e.getDamager()).getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.FIRE_ASPECT)) {
						e.setCancelled(true);
						((Player) e.getDamager()).playSound(e.getDamager().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
						((Player) e.getEntity()).playSound(e.getEntity().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
					}
				}
				Random rand = new Random();
				int number = rand.nextInt(100) + 1;
				if(number <= 20)
				{
				   if(e.getDamager() instanceof LivingEntity) {
					   ((LivingEntity) e.getDamager()).setFireTicks(60);
				   }
				}
			}
		}
	}
	
	/*@EventHandler
	public void onFireTrail(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Block b = e.getTo().add(0, -1, 0).getBlock();
		if(WolvMC.getRace(p.getName()).equals("zorlim") && (b.getType()==Material.DIRT || b.getType()==Material.GRASS || b.getType()==Material.LOG || b.getType()==Material.LOG_2 || b.getType()==Material.WOOD || b.getType()==Material.NETHERRACK) && sneak.containsKey(p.getName())) {
			if(!WolvMC.canUsePowerBlock(e.getTo())) {
				if(p.isSprinting() && sneak.get(p.getName())==4) {
					Random rand = new Random();
					int number = rand.nextInt(100) + 1;
					if(number <= 33)
					{
						e.getTo().add(0, 1, 0).getBlock().setType(Material.FIRE);
					}
				}
			}
		}
	}*/
	
	/*@EventHandler
	public void onActivateFire(PlayerToggleSneakEvent e) {
		final Player p = e.getPlayer();
		if(p.isSneaking()) {
			if(WolvMC.getRace(p.getName()).equals("zorlim")) {
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
					p.sendMessage(ChatColor.GREEN + "Vous venez d'activer la trainée de feu quand vous courrez pour 10 secondes!");
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
					sneak.put(p.getName(), 4);
			        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
			        	@Override
				            public void run() {
				            	if(sneak.get(p.getName())!=null) {
				            		if(sneak.get(p.getName())==4) {
				            			sneak.remove(p.getName());
				            		}
				            	}
				            }
				        }, 3600L);
			        }
			}
		}
	}*///TODO Fire Trail
	
	@EventHandler
	public void onIgniteEntity(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("zorlim") && p.isSneaking() && p.getInventory().getItemInOffHand()==null && p.getInventory().getItemInMainHand()==null) {
			if(p.isSneaking()) {
				int cooldownTime = 30;
		        if(ignitebug.containsKey(p.getName())) {
					Long bug = ignitebug.get(p.getName());
					if(System.currentTimeMillis() - bug < 20) {
						return;
					}
				}
		        if(ignite.containsKey(p.getName())) {
		            long secondsLeft = ((ignite.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
		            if(secondsLeft>0) {
		            	int seconds = (int) secondsLeft;
		            	p.sendMessage(WolvMC.msgCooldown(seconds));
		                return;
		            }
		        }
		        if(!WolvMC.canUsePowerSafe(e.getRightClicked().getLocation(), p)) {
		        	return;
		        }
		        e.getRightClicked().setFireTicks(100);
		        ignite.put(p.getName(), System.currentTimeMillis());
		        p.sendMessage(igniteMSG.replaceAll("%entity%", e.getRightClicked().toString()));
		        WolvMC.addNbToMission("zorlim.1", p.getName(), 1.00);
			}
		}
	}
	
	@EventHandler
	public void onPlaceFire(PlayerInteractEvent e) { //== Non testé ==
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("zorlim") && p.isSneaking() && e.getAction()==Action.RIGHT_CLICK_BLOCK) {
		       int cooldownTime = 30;
		        if(placefire.containsKey(p.getName())) {
					Long bug = firebug.get(p.getName());
					if(System.currentTimeMillis() - bug < 20) {
						return;
					}
				}
		        if(placefire.containsKey(p.getName())) {
		            long secondsLeft = ((placefire.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
		            if(secondsLeft>0) {
		            	int seconds = (int) secondsLeft;
		            	p.sendMessage(WolvMC.msgCooldown(seconds));
		                return;
		            }
		        }
		        if(!WolvMC.canUsePowerBlock(e.getClickedBlock().getLocation(), p)) {
		        	firebug.put(p.getName(), System.currentTimeMillis());
		        	placefire.put(p.getName(), System.currentTimeMillis() - cooldownTime * 1001);
		        	return;
		        }
		        if(e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType()==Material.AIR) {
		        	if(WolvMC.canUsePowerBlock(e.getClickedBlock().getLocation(), p)) {
			        	placefire.put(p.getName(), System.currentTimeMillis());
			        	firebug.put(p.getName(), System.currentTimeMillis());
			        	e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().setType(Material.FIRE);
			        	if(WolvMC.hasFinishMission("zorlim.2", p.getName())) {
			        		int radius = 3;
			        		Location loc = e.getClickedBlock().getLocation();
			        		World world = loc.getWorld();
			        		for (int x = -radius; x < radius; x++) {
			        		    for (int y = -radius; y < radius; y++) {
			        		        for (int z = -radius; z < radius; z++) {
			        		            Block block = world.getBlockAt(loc.getBlockX()+x, loc.getBlockY()+y, loc.getBlockZ()+z);
			        		            if (block.getLocation().add(0, 1, 0).getBlock().getType()==Material.AIR && block.getType()!=Material.AIR && block.getType()!=Material.FIRE) {
			        		            	block.getLocation().add(0, 1, 0).getBlock().setType(Material.AIR);
			        		            }
			        		        }
			        		    }
			        		}
			        	}
		        	}
		        }
		        else {
		        	p.sendMessage(ChatColor.RED + "Vous ne pouvez poser du feu que si le bloc au dessus est vide!");
		        }
		}
	}
	
	public void onEatLavaOrCoal(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction()==Action.RIGHT_CLICK_AIR && WolvMC.getRace(p.getName()).equals("zorlim")) {
			if(p.getFoodLevel()<20) {
				if(p.getInventory().getItemInMainHand().getType()==Material.LAVA_BUCKET) {
					ItemStack is = e.getItem();
	        		if(is.getAmount()==1) {
	        			if(e.getHand()==EquipmentSlot.HAND) {
	        				p.getInventory().setItemInMainHand(null);
	        			}
	        			else if(e.getHand()==EquipmentSlot.OFF_HAND) {
	        				p.getInventory().setItemInOffHand(null);
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
					if(p.getFoodLevel()>4) {
						Integer food = 20 - p.getFoodLevel();
						food = 16 - food;
						p.setSaturation(food);
					}
	        		p.setFoodLevel(p.getFoodLevel() + 16);
	        		p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
	        		p.sendMessage(ChatColor.GREEN + "Vous avez mangé un seau de lave!");
				}
				else if(p.getInventory().getItemInMainHand().getType()==Material.COAL) {
					ItemStack is = e.getItem();
	        		if(is.getAmount()==1) {
	        			if(e.getHand()==EquipmentSlot.HAND) {
	        				p.getInventory().setItemInMainHand(null);
	        			}
	        			else if(e.getHand()==EquipmentSlot.OFF_HAND) {
	        				p.getInventory().setItemInOffHand(null);
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
					if(p.getFoodLevel()>16) {
						Integer food = 20 - p.getFoodLevel();
						food = 4 - food;
						p.setSaturation(food);
					}
	        		p.setFoodLevel(p.getFoodLevel() + 4);
	        		p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
	        		p.sendMessage(ChatColor.GREEN + "Vous avez mangé un charbon!");
				}
			}
		}
	}
	
	public static void initZorlim() {
		//WolvMC.addMission("zorlim.1", (double) 20, "zorlim", "Brûler %goal% joueurs", "Régéneration 2 dans la lave");
	 	//WolvMC.addMission("zorlim.2", (double) 10, "zorlim", "Shoot %goal% fireballs à la main", "Cercle de feu (sneak + clic droit sur un bloc)");
		WolvMC.addRace("zorlim", ChatColor.DARK_RED + "Zorlim", new ItemStack(Material.FIREBALL, 1));
		WolvMC.addMission("zorlim.1", (double) 20, "zorlim", "Brûler %goal% joueurs", "Régéneration 2 dans la lave");
	 	WolvMC.addMission("zorlim.2", (double) 10, "zorlim", "Shoot %goal% fireballs à la main", "Cercle de feu (sneak + clic droit sur un bloc)");
		WolvMC.getPlugin(WolvMC.class).getLogger().fine("Zorlim Class loaded!");
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
            @Override
            public void run() {
    			for(Player p : WolvMC.getPlugin(WolvMC.class).getServer().getOnlinePlayers()) {
    				if(WolvMC.getRace(p.getName()).equals("zorlim")) {
      					if(p.getLocation().getBlock().getLightFromSky()>14) {
    						if(!p.getWorld().hasStorm()) {
    							if(p.getWorld().getTime()>0 && p.getWorld().getTime()<12000) {
    								if(p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
    									p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
    								}
    								if(!p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
    									p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, 2));
    								}
    							}
    							else {
    								if(p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
    									p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    								}
    								if(!p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
    									p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 2));
    								}
    							}
    						}
							else {
								if(p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
									p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
								}
								if(!p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
									p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 2));
								}
							}
      					}
						else {
							if(p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
								p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
							}
							if(!p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
								p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 2));
							}
						}
      					if(p.getLocation().getBlock().getType()==Material.LAVA) {
      						if(p.getHealth()<p.getMaxHealth()) {
      							p.setHealth(p.getHealth() + 0.5);
      						}
      					}
      					else if(p.getLocation().getBlock().getType()==Material.WATER || p.getLocation().getBlock().getType()==Material.STATIONARY_WATER) {
      						if(p.getVehicle()==null) {
      							p.damage(1);
      						}
      						else if(p.getVehicle().getType()!=EntityType.BOAT) {
      							p.damage(1);
      						}
      					}
    				}
    			}
            }
        }, 0L, 20L);
	}
	
}
