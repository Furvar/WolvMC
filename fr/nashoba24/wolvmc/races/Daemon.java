package fr.nashoba24.wolvmc.races;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.commands.Nether;
import fr.nashoba24.wolvmc.events.WolvMCInitEffectsEvent;
import fr.nashoba24.wolvmc.events.WolvMCReloadEvent;

public class Daemon implements Listener {
	
	public HashMap<String, Long> lava = new HashMap<String, Long>();
	public HashMap<String, Long> lavabug = new HashMap<String, Long>();
	static Integer strength = 3;
	static Integer speed = 2;
	static double goldMultiplier = 2.0;
	static double netherMultiplier = 2.0;
	static Integer lavaCooldown = 30;
	static Integer villagerRadius = 10;
	public static boolean enabled = true;
	static boolean explosion = true;
	static boolean invinciblePigmen = true;
	static boolean glowstone = true;
	static boolean transfCreepPig = true;
	static boolean lavaPower = true;
	static boolean killVillagers = true;
	public static boolean pigTransf = true;
	public static boolean tpNether = true;

	@EventHandler
	public void onInitEffects(WolvMCInitEffectsEvent e) {
		Player p = e.getPlayer();
		if(e.getRace().equals("daemon")) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, strength - 1));
			if(WolvMC.hasFinishMission("daemon.1", p.getName())) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2147483647, 1));
			}
			if(WolvMC.hasFinishMission("daemon.2", p.getName())) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, speed - 1));
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(WolvMC.getRace(p.getName()).equals("daemon")) {
				if((e.getCause()==DamageCause.BLOCK_EXPLOSION || e.getCause()==DamageCause.ENTITY_EXPLOSION) && explosion) {
					e.setCancelled(true);
				}
				else if(e.getCause()==DamageCause.FIRE || e.getCause()==DamageCause.FIRE_TICK) {
					WolvMC.addNbToMission("daemon.1", p.getName(), e.getDamage());
				}
			}
		}
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(WolvMC.getRace(p.getName()).equals("daemon")) {
				Player a = (Player) e.getDamager();
				Material t = a.getInventory().getItemInMainHand().getType();
				if(t==Material.GOLD_SPADE || t==Material.GOLD_PICKAXE || t==Material.GOLD_SWORD || t==Material.GOLD_AXE || t==Material.GOLD_HOE) {
					e.setDamage(e.getDamage() * goldMultiplier);
				}
			}
		}
		else if(e.getEntity() instanceof Player && e.getDamager() instanceof PigZombie && invinciblePigmen) {
			if(WolvMC.getRace(((Player) e.getEntity()).getName()).equals("daemon")) {
				e.setCancelled(true);
			}
		}
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if(WolvMC.getRace(p.getName()).equals("daemon")) {
				if(p.getWorld().getEnvironment()==Environment.NETHER) {
					e.setDamage(e.getDamage() * netherMultiplier);
				}
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(!glowstone) {
			return;
		}
		if(WolvMC.getRace(e.getPlayer().getName()).equals("daemon") && e.getBlock().getType()==Material.GLOWSTONE) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onRightclickCreep(PlayerInteractEntityEvent e) {
		if(!transfCreepPig) {
			return;
		}
		if(WolvMC.getRace(e.getPlayer().getName()).equals("daemon")) {
			if(e.getRightClicked() instanceof Pig && e.getPlayer().isSneaking() && e.getPlayer().getInventory().getItemInMainHand().getType()==Material.AIR && e.getPlayer().getInventory().getItemInOffHand().getType()==Material.AIR) {
				e.getRightClicked().getLocation(e.getRightClicked().getLocation().add(0, -300, 0));
				e.getRightClicked().getWorld().spawnEntity(e.getRightClicked().getLocation(), EntityType.PIG_ZOMBIE);
			}
			else if(e.getRightClicked() instanceof Creeper && e.getPlayer().isSneaking() && e.getPlayer().getInventory().getItemInMainHand().getType()==Material.AIR && e.getPlayer().getInventory().getItemInOffHand().getType()==Material.AIR) {
				e.getRightClicked().getLocation(e.getRightClicked().getLocation().add(0, -300, 0));
				Creeper ent = (Creeper) e.getRightClicked().getWorld().spawnEntity(e.getRightClicked().getLocation(), EntityType.CREEPER);
				ent.setPowered(true);
			}
		}
	}
	
	@EventHandler
	public void onPlaceLava(PlayerInteractEvent e) {
		if(!lavaPower) {
			return;
		}
		Player p = e.getPlayer();
		if(e.getAction()==Action.RIGHT_CLICK_BLOCK && e.getItem()==null && WolvMC.getRace(p.getName()).equals("daemon")) {
			if(p.isSneaking()) {
				if(e.getClickedBlock().getLocation().add(0, 2, 0).getBlock().getType()!=Material.AIR && e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType()!=Material.AIR) {
					return;
				}
			       int cooldownTime = lavaCooldown;
			        if(lava.containsKey(p.getName())) {
						Long bug = lavabug.get(p.getName());
						if(System.currentTimeMillis() - bug < 20) {
							return;
						}
			            long secondsLeft = ((lava.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
			            if(secondsLeft>0) {
			            	lavabug.put(p.getName(), System.currentTimeMillis());
			            	int seconds = (int) secondsLeft;
			            	p.sendMessage(WolvMC.msgCooldown(seconds));
			                return;
			            }
			        }
			        if(!WolvMC.canUsePowerBlock(e.getClickedBlock().getLocation(), p)) {
			        	lavabug.put(p.getName(), System.currentTimeMillis());
			        	lava.put(p.getName(), System.currentTimeMillis() - cooldownTime * 1001);
			        	return;
			        }
			        lava.put(p.getName(), System.currentTimeMillis());
			        lavabug.put(p.getName(), System.currentTimeMillis());
			        if(e.getClickedBlock().getLocation().add(0, 2, 0).getBlock().getType()==Material.AIR) {
			        	e.getClickedBlock().getLocation().add(0, 2, 0).getBlock().setType(Material.LAVA);
			        }
			        if(e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType()==Material.AIR) {
			        	e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().setType(Material.LAVA);
			        }
			        final Block b = e.getClickedBlock();
					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
			            @Override
			            public void run() {
					        if(b.getLocation().add(0, 2, 0).getBlock().getType()==Material.LAVA) {
					        	b.getLocation().add(0, 2, 0).getBlock().setType(Material.AIR);
					        }
					        if(b.getLocation().add(0, 1, 0).getBlock().getType()==Material.LAVA) {
					        	b.getLocation().add(0, 1, 0).getBlock().setType(Material.AIR);
					        }
			            }
			        }, 20L);
			}
		}
	}
	
	@EventHandler
	public void onDeathInNeher(PlayerDeathEvent e) {
		if(WolvMC.getRace(e.getEntity().getName()).equals("daemon")) {
			if(e.getEntity().getWorld().getEnvironment()==Environment.NETHER) {
				WolvMC.addNbToMission("daemon.2", e.getEntity().getName(), (double) 1);
			}
		}
	}
	
	public static void initDaemon() {
		//WolvMC.addMission("daemon.1", (double) 50, "daemon", "Take %goal% units of damage by fire or ignition", "Fire resistance");
	 	//WolvMC.addMission("daemon.2", (double) 20, "daemon", "Die %goal% times in the Nether", "Speed II");
		WolvMC.addRace("daemon", ChatColor.RED + "Daemon", new ItemStack(Material.MAGMA, 1));
		WolvMC.addMission("daemon.1", (double) 50, "daemon", "Recevoir un équivalent à %goal% unités de dégâts par le feu ou par brûlure", "Résistance au feu");
	 	WolvMC.addMission("daemon.2", (double) 20, "daemon", "Mourir %goal% fois dans le nether", "Vitesse II");
		WolvMC.getPlugin(WolvMC.class).getLogger().fine("Daemon Class loaded!");
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
            @Override
            public void run() {
        		if(!killVillagers) {
        			return;
        		}
    			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
    				if(WolvMC.getRace(p.getName()).equals("daemon")) {
    					List<Entity> list = p.getNearbyEntities(villagerRadius, villagerRadius, villagerRadius);
    					for(Entity ent : list) {
    						if(ent.getType()==EntityType.VILLAGER) {
    							((LivingEntity) ent).setHealth(0);
    						}
    					}
    				}
    			}
            }
        }, 0L, 20L);
	}
	
	@EventHandler
	public void reloadDaemon(WolvMCReloadEvent e) {
		File file = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/daemon.yml");
		if(!file.exists()) {
			  InputStream stream = WolvMC.class.getClassLoader().getResourceAsStream("fr/nashoba24/wolvmc/config/daemon.yml");
			  FileOutputStream fos = null;
			  try {
			      fos = new FileOutputStream(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/daemon.yml");
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
		if(config.isSet("strength")) {
			Daemon.strength = config.getInt("strength");
		}
		if(config.isSet("speed")) {
			Daemon.speed = config.getInt("speed");
		}
		if(config.isSet("gold-damages-multiplier")) {
			Daemon.goldMultiplier = config.getDouble("gold-damages-multiplier");
		}
		if(config.isSet("nether-damages-multiplier")) {
			Daemon.netherMultiplier = config.getDouble("nether-damages-multiplier");
		}
		if(config.isSet("lava-cooldown")) {
			Daemon.lavaCooldown = config.getInt("lava-cooldown");
		}
		if(config.isSet("kill-villager-radius")) {
			Daemon.villagerRadius = config.getInt("kill-villager-radius");
		}
		if(config.isSet("enabled")) {
			Daemon.enabled = config.getBoolean("enabled");
		}
		if(config.isSet("world-nether")) {
			Nether.worldN = config.getString("world-nether");
		}
		if(config.isSet("invincible-explosion")) {
			Daemon.explosion = config.getBoolean("invincible-explosion");
		}
		if(config.isSet("invincible-zombie-pigmen")) {
			Daemon.invinciblePigmen = config.getBoolean("invincible-zombie-pigmen");
		}
		if(config.isSet("cant-break-glowstone")) {
			Daemon.glowstone = config.getBoolean("cant-break-glowstone");
		}
		if(config.isSet("transf-pigs-and-creepers")) {
			Daemon.transfCreepPig = config.getBoolean("transf-pigs-and-creepers");
		}
		if(config.isSet("place-lava")) {
			Daemon.lavaPower = config.getBoolean("place-lava");
		}
		if(config.isSet("kill-villagers")) {
			Daemon.killVillagers = config.getBoolean("kill-villagers");
		}
		if(config.isSet("pig-command")) {
			Daemon.pigTransf = config.getBoolean("pig-command");
		}
		if(config.isSet("tp-nether")) {
			Daemon.tpNether = config.getBoolean("tp-nether");
		}
	}
}
