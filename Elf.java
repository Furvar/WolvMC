package fr.nashoba24.wolvmc.races;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.events.WolvMCInitEffectsEvent;

public class Elf implements Listener {
	
	public static boolean enabled = true;
	static Integer powerEnchant = 2;
	static Double damageMultiplierWood = 4.00;
	static Double goldDiviser = 1.25;
	static String notFound = ChatColor.RED + "Vous n'avez trouvé aucun fruit...";
	static Double chanceFoundFruit = 20.0;
	static Double chanceFoundWrongFruit = 20.0;
	static String wrongFruitMSG = ChatColor.RED + "Le fruit que vous avez trouvé n'est pas comestible!";
	static String goodFruitMSG = ChatColor.GREEN + "Vous avez trouvé un fruit dans les feuilles!";
	static Integer strength = 1;
	public HashMap<String, Long> grow = new HashMap<String, Long>();
	public HashMap<String, Long> growbug = new HashMap<String, Long>();
	public HashMap<String, Long> leavesbug = new HashMap<String, Long>();
	static boolean glowAPI = true;
	
	@EventHandler
	public void onInitEffects(WolvMCInitEffectsEvent e) {
		Player p = e.getPlayer();
		if(e.getRace().equals("elf")) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, strength - 1));
			if(WolvMC.hasFinishMission("elf.1", p.getName())) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 2147483647, 1));
			}
			if(WolvMC.hasFinishMission("elf.2", p.getName())) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 2));
			}
			else {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 0));
			}
		}
	}
	
	@EventHandler
	public void onRightClickBow(PlayerInteractEvent e) {
		if(e.getItem()!=null) {
			if(e.getItem().getType()==Material.BOW && WolvMC.getRace(e.getPlayer().getName()).equals("elf")) {
				Map<Enchantment, Integer> ench = e.getItem().getEnchantments();
				if(ench.containsKey(Enchantment.ARROW_DAMAGE)) {
					if(ench.get(Enchantment.ARROW_DAMAGE)<powerEnchant) {
						e.getItem().addEnchantment(Enchantment.ARROW_DAMAGE, powerEnchant);
					}
				}
				else {
					e.getItem().addEnchantment(Enchantment.ARROW_DAMAGE, powerEnchant);
				}
			}
		}
	}
	
	//CODE POUR M'ENTRAëNER
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPoison(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player){
			Player p = (Player) e.getDamager();
			LivingEntity c1 = (LivingEntity) e.getEntity();
			Player c2 = (Player) e.getEntity();
			if(WolvMC.getRace(p.getName()).equals("elf")){
			Material mM = p.getInventory().getItemInMainHand().getType();
			ItemStack rose = new ItemStack(Material.RED_ROSE, 1,(byte) 0);
			ItemStack pissenlit = new ItemStack(Material.YELLOW_FLOWER, 1, (byte) 0);
			ItemStack orchidŽe_bleue = new ItemStack(Material.RED_ROSE, 1, (byte)1);
			ItemStack allium = new ItemStack(Material.RED_ROSE, 1, (byte)2);
			ItemStack marguerite = new ItemStack(Material.RED_ROSE, 1, (byte)8);
			if(mM == Material.WOOD_SWORD || mM == Material.STONE_SWORD || mM == Material.IRON_SWORD || mM == Material.GOLD_SWORD || mM == Material.DIAMOND_SWORD){	
				if(p.getInventory().getItemInOffHand() == rose){
					c1.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 0, 60));
					c1.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 0, 60));
					c2.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 0, 60));
					c2.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 0, 60));
					p.getInventory().remove(rose);
					p.updateInventory();
				}else if(p.getInventory().getItemInOffHand() == pissenlit){
					c1.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1000, 5));
					c1.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000, 5));
					c1.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000, 5));
					c2.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1000, 5));
					c2.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000, 5));
					c2.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000, 5));
					p.getInventory().remove(pissenlit);
					p.updateInventory();
				}else if(p.getInventory().getItemInOffHand() == orchidŽe_bleue){
					c1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1, 60));
					c1.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1, 60));
					c2.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1, 60));
					c2.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1, 60));
					p.getInventory().remove(orchidŽe_bleue);
					p.updateInventory();
				}else if(p.getInventory().getItemInOffHand() == allium){
					c1.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1, 5));
					c1.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2, 5));
					c2.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1, 5));
					c2.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2, 5));
					p.getInventory().remove(allium);
					p.updateInventory();
				}else if(p.getInventory().getItemInOffHand() == marguerite){
					c1.damage(c1.getHealth() / 4 * 3);
					c2.damage(c2.getHealth() / 4 * 3);
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, (int) (c1.getHealth() / 4)));
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, (int) (c2.getHealth() / 4)));
					p.getInventory().remove(marguerite);
					p.updateInventory();
				}
			}
			}
		}
	}
	//C'EST RŽANOUVEAU TON CODE ˆ PARTIR DE MAINTENANT:
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof LivingEntity && e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			LivingEntity att = (LivingEntity) e.getDamager();
			if(WolvMC.getRace(p.getName()).equals("elf")) {
				if(att.getEquipment().getItemInMainHand()!=null) {
					if(att.getEquipment().getItemInMainHand().getType()==Material.DIAMOND_AXE || att.getEquipment().getItemInMainHand().getType()==Material.DIAMOND_HOE || att.getEquipment().getItemInMainHand().getType()==Material.DIAMOND_SPADE || att.getEquipment().getItemInMainHand().getType()==Material.DIAMOND_PICKAXE || att.getEquipment().getItemInMainHand().getType()==Material.DIAMOND_SWORD) {
						e.setCancelled(true);
						att.getWorld().playSound(att.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
					}
					else if(att.getEquipment().getItemInMainHand().getType()==Material.WOOD_SWORD) {
						e.setDamage(e.getDamage() * Elf.damageMultiplierWood);
					}
				}
				if(p.getInventory().getHelmet()!=null) {
					if(p.getInventory().getHelmet().getType()==Material.GOLD_HELMET) {
						e.setDamage(e.getDamage() / goldDiviser);
					}
				}
				if(p.getInventory().getChestplate()!=null) {
					if(p.getInventory().getChestplate().getType()==Material.GOLD_CHESTPLATE) {
						e.setDamage(e.getDamage() / goldDiviser);
					}
				}
				if(p.getInventory().getBoots()!=null) {
					if(p.getInventory().getBoots().getType()==Material.GOLD_BOOTS) {
						e.setDamage(e.getDamage() / goldDiviser);
					}
				}
				if(p.getInventory().getLeggings()!=null) {
					if(p.getInventory().getLeggings().getType()==Material.GOLD_LEGGINGS) {
						e.setDamage(e.getDamage() / goldDiviser);
					}
				}
			}
		}
		if(e.getEntity() instanceof LivingEntity && e.getDamager() instanceof Arrow) {
			Projectile proj = (Projectile) e.getDamager();
			if(proj.getShooter() instanceof Player) {
				Player p = (Player) proj.getShooter();
				if(WolvMC.getRace(p.getName()).equals("elf")) {
					PotionEffectType[] list = new PotionEffectType[]{ PotionEffectType.BLINDNESS, PotionEffectType.CONFUSION, PotionEffectType.GLOWING, PotionEffectType.HUNGER, PotionEffectType.LEVITATION, PotionEffectType.POISON, PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.UNLUCK, PotionEffectType.WEAKNESS, PotionEffectType.WITHER };
					PotionEffectType eff = list[new Random().nextInt(list.length - 1)];
					((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(eff, 100, 1));
				}
			}
		}
	}
	
	@EventHandler
	public void onPlant(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction()==Action.RIGHT_CLICK_BLOCK) {
			if(leavesbug.containsKey(p.getName())) {
			Long bug = leavesbug.get(p.getName());
				if(System.currentTimeMillis() - bug < 20) {
					return;
				}
			}
			leavesbug.put(p.getName(), System.currentTimeMillis());
			if(WolvMC.getRace(p.getName()).equals("elf") && WolvMC.canUsePowerBlock(e.getClickedBlock().getLocation(), p)) {
				if(e.getClickedBlock().getType()==Material.DIRT || e.getClickedBlock().getType()==Material.GRASS) {
					if(e.getItem()!=null) {
						if(e.getItem().getType()==Material.SEEDS) {
							e.getClickedBlock().setType(Material.SOIL);
							e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().setType(Material.CROPS);
							if(e.getHand().equals(EquipmentSlot.HAND)) {
								if(e.getItem().getAmount()==1) {
									p.getInventory().setItemInMainHand(null);
								}
								else {
									p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
								}
							}
							else if(e.getHand().equals(EquipmentSlot.OFF_HAND)) {
								if(e.getItem().getAmount()==1) {
									p.getInventory().setItemInOffHand(null);
								}
								else {
									p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount() - 1);
								}
							}
						}
						if(e.getItem().getType()==Material.CARROT_ITEM) {
							e.getClickedBlock().setType(Material.SOIL);
							e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().setType(Material.CARROT);
							if(e.getHand().equals(EquipmentSlot.HAND)) {
								if(e.getItem().getAmount()==1) {
									p.getInventory().setItemInMainHand(null);
								}
								else {
									p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
								}
							}
							else if(e.getHand().equals(EquipmentSlot.OFF_HAND)) {
								if(e.getItem().getAmount()==1) {
									p.getInventory().setItemInOffHand(null);
								}
								else {
									p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount() - 1);
								}
							}
						}
						if(e.getItem().getType()==Material.POTATO_ITEM) {
							e.getClickedBlock().setType(Material.SOIL);
							e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().setType(Material.POTATO);
							if(e.getHand().equals(EquipmentSlot.HAND)) {
								if(e.getItem().getAmount()==1) {
									p.getInventory().setItemInMainHand(null);
								}
								else {
									p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
								}
							}
							else if(e.getHand().equals(EquipmentSlot.OFF_HAND)) {
								if(e.getItem().getAmount()==1) {
									p.getInventory().setItemInOffHand(null);
								}
								else {
									p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount() - 1);
								}
							}
						}
					}
				}
				else if(e.getClickedBlock().getType()==Material.LEAVES && e.getItem()==null && p.getFoodLevel()<20) {
					Random rand = new Random();
					int number = rand.nextInt(100) + 1;
					if(number <= chanceFoundFruit)
					{
						e.getClickedBlock().setType(Material.AIR);
						number = rand.nextInt(100) + 1;
						if(number <= chanceFoundWrongFruit)
						{
							p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 0));
							p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20, 1));
							p.sendMessage(wrongFruitMSG);
							WolvMC.addNbToMission("elf.1", p.getName(), 1.00);
						}
						else {
							double randomValue = 1 + (2 * rand.nextDouble());
							if(p.getFoodLevel() + randomValue > 20) {
								p.setFoodLevel(20);
								Double sat = p.getFoodLevel() + randomValue - 20;
								p.setSaturation(sat.floatValue());
							}
							else {
								Double food = p.getFoodLevel() + randomValue;
								p.setFoodLevel(food.intValue());
							}
							p.sendMessage(goodFruitMSG);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onGrowTree(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction()==Action.RIGHT_CLICK_BLOCK && p.isSneaking() && e.getItem()==null && WolvMC.getRace(p.getName()).equals("elf")) {
			if(e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType()==Material.AIR && (e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.SAND || e.getClickedBlock().getType()==Material.DIRT || e.getClickedBlock().getType()==Material.STAINED_CLAY || e.getClickedBlock().getType()==Material.NETHERRACK || e.getClickedBlock().getType()==Material.ICE || e.getClickedBlock().getType()==Material.PACKED_ICE || e.getClickedBlock().getType()==Material.SNOW || e.getClickedBlock().getType()==Material.ENDER_STONE)) {
			       int cooldownTime = 60;
			        if(grow.containsKey(p.getName())) {
			        	if(growbug.containsKey(p.getName())) {
							Long bug = growbug.get(p.getName());
							if(System.currentTimeMillis() - bug < 20) {
								return;
							}
			        	}
			            long secondsLeft = ((grow.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
			            if(secondsLeft>0) {
			            	growbug.put(p.getName(), System.currentTimeMillis());
			            	int seconds = (int) secondsLeft;
			            	p.sendMessage(WolvMC.msgCooldown(seconds));
			                return;
			            }
			        }
			        if(!WolvMC.canUsePowerBlock(e.getClickedBlock().getLocation(), p)) {
			        	growbug.put(p.getName(), System.currentTimeMillis());
			        	grow.put(p.getName(), System.currentTimeMillis() - cooldownTime * 1001);
			        	return;
			        }
		        grow.put(p.getName(), System.currentTimeMillis());
		        growbug.put(p.getName(), System.currentTimeMillis());
		        Location loc = e.getClickedBlock().getLocation().add(0, 1, 0);
		        WolvMC.addNbToMission("elf.2", p.getName(), 1.00);
		        TreeType t = null;
		        Random rand = new Random();
		        int number;
		        switch(e.getClickedBlock().getBiome()) {
		        	case OCEAN:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TREE;
		        		break;
		        	case PLAINS:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TREE;
		        		break;
		        	case DESERT:
		        		if(e.getClickedBlock().getType()==Material.SAND) {
		        			loc.getBlock().setType(Material.CACTUS);
		        			loc.add(0, 1, 0).getBlock().setType(Material.CACTUS);
		        			loc.add(0, 1, 0).getBlock().setType(Material.CACTUS);
		        			return;
		        		}
		        	case EXTREME_HILLS:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 40)
							{
								t = TreeType.BIG_TREE;
							}
							else if(number <= 75){
								t = TreeType.REDWOOD;
							}
							else {
								t = TreeType.TALL_REDWOOD;
							}
		        		}
		        		break;
		        	case FOREST:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TREE;
		        		break;
		        	case TAIGA:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 20)
							{
								t = TreeType.TALL_REDWOOD;
							}
							else {
								t = TreeType.REDWOOD;
							}
		        		}
		        		break;
		        	case SWAMPLAND:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.SWAMP;
		        		break;
		        	case ICE_FLATS:
		        		if(e.getClickedBlock().getType()==Material.SNOW)
		        		t = TreeType.REDWOOD;
		        		break;
		        	case MUSHROOM_ISLAND:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 50)
							{
								t = TreeType.BROWN_MUSHROOM;
							}
							else {
								t = TreeType.RED_MUSHROOM;
							}
		        		}
		        		break;
		        	case JUNGLE:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 50)
							{
								t = TreeType.JUNGLE;
							}
							else if(number <= 90){
								t = TreeType.SMALL_JUNGLE;
							}
							else {
								t = TreeType.JUNGLE_BUSH;
							}
		        		}
		        		break;
		        	case HELL:
		        		if(e.getClickedBlock().getType()==Material.NETHERRACK) {
		        			generateCustomTree(loc, rand.nextInt(3) + 4, Material.NETHER_BRICK, Material.MAGMA);
		        			return;
		        		}
		        	case SKY:
		        		if(e.getClickedBlock().getType()==Material.ENDER_STONE)
		        		t = TreeType.CHORUS_PLANT;
		        		break;
		        	case VOID:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TREE;
		        		break;
		        	case DEEP_OCEAN:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TREE;
		        		break;
		        	case BIRCH_FOREST:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
						t = TreeType.BIRCH;
		        		break;
		        	case ROOFED_FOREST:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.DARK_OAK;
		        		break;
		        	case TAIGA_COLD:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 15)
							{
								t = TreeType.REDWOOD;
							}
							else {
								t = TreeType.TALL_REDWOOD;
							}
		        		}
		        		break;
		        	case TAIGA_HILLS:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TALL_REDWOOD;
		        		break;
		        	case EXTREME_HILLS_WITH_TREES:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 20)
							{
								t = TreeType.TREE;
							}
							else {
								t = TreeType.REDWOOD;
							}
		        		}
		        		break;
		        	case SAVANNA:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.ACACIA;
		        		break;
		        	case MESA:
		        		if(e.getClickedBlock().getType()==Material.STAINED_CLAY) {
		        			loc.getBlock().setType(Material.DEAD_BUSH);
	        				return;
	        			}
		        	case RIVER:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TREE;
		        		break;
		        	case FROZEN_RIVER:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.REDWOOD;
		        		break;
		        	case MUSHROOM_ISLAND_SHORE:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 50)
							{
								t = TreeType.BROWN_MUSHROOM;
							}
							else {
								t = TreeType.RED_MUSHROOM;
							}
		        		}
		        		break;
		        	case BEACHES:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TREE;
		        		break;
		        	case DESERT_HILLS:
		        		if(e.getClickedBlock().getType()==Material.SAND) {
		        			loc.getBlock().setType(Material.CACTUS);
		        			loc.add(0, 1, 0).getBlock().setType(Material.CACTUS);
		        			loc.add(0, 2, 0).getBlock().setType(Material.CACTUS);
		        			return;
		        		}
		        	case FOREST_HILLS:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 20)
							{
								t = TreeType.BIG_TREE;
							}
							else {
								t = TreeType.TREE;
							}
		        		}
		        		break;
		        	case TAIGA_COLD_HILLS:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 15)
							{
								t = TreeType.REDWOOD;
							}
							else {
								t = TreeType.TALL_REDWOOD;
							}
		        		}
		        		break;
		        	case JUNGLE_HILLS:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 50)
							{
								t = TreeType.JUNGLE;
							}
							else if(number <= 90){
								t = TreeType.SMALL_JUNGLE;
							}
							else {
								t = TreeType.JUNGLE_BUSH;
							}
		        		}
		        		break;
		        	case JUNGLE_EDGE:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
			        		number = rand.nextInt(100) + 1;
							if(number <= 90){
								t = TreeType.SMALL_JUNGLE;
							}
							else {
								t = TreeType.JUNGLE_BUSH;
							}
		        		}
		        		break;
		        	case STONE_BEACH:
		        		if(e.getClickedBlock().getType()==Material.STONE && e.getClickedBlock().getLocation().getY()>64) {
		        			generateCustomTree(loc, rand.nextInt(3) + 4, Material.STONE, Material.COAL_ORE);
		        			return;
		        		}
		        	case COLD_BEACH:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.REDWOOD;
		        		break;
		        	case BIRCH_FOREST_HILLS:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 20)
							{
								t = TreeType.TALL_BIRCH;
							}
							else {
								t = TreeType.BIRCH;
							}
		        		}
		        		break;
		        	case REDWOOD_TAIGA:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TALL_REDWOOD;
		        		break;
		        	case REDWOOD_TAIGA_HILLS:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TALL_REDWOOD;
		        		break;
		        	case SAVANNA_ROCK:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.ACACIA;
		        		break;
		        	case FROZEN_OCEAN:
		        		if(e.getClickedBlock().getType()==Material.ICE || e.getClickedBlock().getType()==Material.PACKED_ICE) {
		        			generateCustomTree(loc, rand.nextInt(3) + 4, Material.PACKED_ICE, Material.ICE);
		        			return;
		        		}
		        	case ICE_MOUNTAINS:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 50)
							{
								t = TreeType.REDWOOD;
							}
							else {
								t = TreeType.TALL_REDWOOD;
							}
		        		}
		        		break;
		        	case MESA_CLEAR_ROCK:
		        		if(e.getClickedBlock().getType()==Material.STAINED_CLAY) {
		        			loc.getBlock().setType(Material.DEAD_BUSH);
		        			return;
		        		}
		        	case MESA_ROCK:
		        		if(e.getClickedBlock().getType()==Material.STAINED_CLAY) {
		        		loc.getBlock().setType(Material.DEAD_BUSH);
	        				return;
	        			}
		        	case SMALLER_EXTREME_HILLS:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.REDWOOD;
		        		break;
		        	case MUTATED_PLAINS:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TREE;
		        		break;
		        	case MUTATED_DESERT:
		        		if(e.getClickedBlock().getType()==Material.SAND) {
			        		loc.getBlock().setType(Material.CACTUS);
			        		loc.add(0, 1, 0).getBlock().setType(Material.CACTUS);
			        		loc.add(0, 1, 0).getBlock().setType(Material.CACTUS);
		        			return;
		        		}
		        	case MUTATED_EXTREME_HILLS:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 40)
							{
								t = TreeType.BIG_TREE;
							}
							else if(number <= 75){
								t = TreeType.REDWOOD;
							}
							else {
								t = TreeType.TALL_REDWOOD;
							}
		        		}
		        		break;
		        	case MUTATED_FOREST:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TREE;
		        		break;
		        	case MUTATED_TAIGA:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
		        			number = rand.nextInt(100) + 1;
							if(number <= 20)
							{
								t = TreeType.TALL_REDWOOD;
							}
							else {
								t = TreeType.REDWOOD;
							}
		        		}
		        		break;
		        	case MUTATED_SWAMPLAND:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.SWAMP;
		        		break;
		        	case MUTATED_ICE_FLATS:
		        		if(e.getClickedBlock().getType()==Material.SNOW || e.getClickedBlock().getType()==Material.ICE || e.getClickedBlock().getType()==Material.PACKED_ICE) {
		        			generateCustomTree(loc, rand.nextInt(3) + 4, Material.PACKED_ICE, Material.ICE);
	        				return;
		        		}
		        	case MUTATED_JUNGLE:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 50)
							{
								t = TreeType.JUNGLE;
							}
							else if(number <= 90){
								t = TreeType.SMALL_JUNGLE;
							}
							else {
								t = TreeType.JUNGLE_BUSH;
							}
		        		}
		        		break;
		        	case MUTATED_JUNGLE_EDGE:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 90){
								t = TreeType.SMALL_JUNGLE;
							}
							else {
								t = TreeType.JUNGLE_BUSH;
							}
		        		}
		        		break;
		        	case MUTATED_BIRCH_FOREST:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TALL_BIRCH;
		        		break;
		        	case MUTATED_BIRCH_FOREST_HILLS:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TALL_BIRCH;
		        		break;
		        	case MUTATED_ROOFED_FOREST:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.DARK_OAK;
		        		break;
		        	case MUTATED_TAIGA_COLD:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 15)
							{
								t = TreeType.REDWOOD;
							}
							else {
								t = TreeType.TALL_REDWOOD;
							}
		        		}
		        		break;
		        	case MUTATED_REDWOOD_TAIGA:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TALL_REDWOOD;
		        		break;
		        	case MUTATED_REDWOOD_TAIGA_HILLS:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.TALL_REDWOOD;
		        		break;
		        	case MUTATED_EXTREME_HILLS_WITH_TREES:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT) {
							number = rand.nextInt(100) + 1;
							if(number <= 20)
							{
								t = TreeType.TREE;
							}
							else {
								t = TreeType.REDWOOD;
							}
		        		}
		        		break;
		        	case MUTATED_SAVANNA:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.ACACIA;
		        		break;
		        	case MUTATED_SAVANNA_ROCK:
		        		if(e.getClickedBlock().getType()==Material.GRASS || e.getClickedBlock().getType()==Material.DIRT)
		        		t = TreeType.ACACIA;
		        		break;
		        	case MUTATED_MESA_CLEAR_ROCK:
		        		if(e.getClickedBlock().getType()==Material.STAINED_CLAY) {
		        			loc.getBlock().setType(Material.DEAD_BUSH);
		        			return;
		        		}
		        	case MUTATED_MESA_ROCK:
		        		if(e.getClickedBlock().getType()==Material.STAINED_CLAY) {
		        			loc.getBlock().setType(Material.DEAD_BUSH);
		        			return;
		        		}
		        	case MUTATED_MESA:
		        		if(e.getClickedBlock().getType()==Material.STAINED_CLAY) {
		        			loc.getBlock().setType(Material.DEAD_BUSH);
		        			return;
		        		}
				default:
					break;
		        }
		        if(t==null) {
		        	WolvMC.addNbToMission("elf.2", p.getName(), -1.00);
		        	grow.remove(p.getName());
		        	return;
		        }
		        p.getWorld().generateTree(loc, t);
			}
		}
	}
	
    public static int remove(Inventory inventory, Material mat, int amount, short damage)
    {
        ItemStack[] contents = inventory.getContents();
        int removed = 0;
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
 
            if (item == null || !item.getType().equals(mat)) {
                continue;
            }
 
            if (damage != (short) -1 && item.getDurability() != damage) {
                continue;
            }
 
            int remove = item.getAmount() - amount - removed;
 
            if (removed > 0) {
                removed = 0;
            }
 
            if (remove <= 0) {
                removed += Math.abs(remove);
                contents[i] = null;
            } else {
                item.setAmount(remove);
            }
        }
        return removed;
    }
    
    public static void generateCustomTree(Location origin, int height, Material trunk, Material leaves) { 	 
    	World world = origin.getWorld();
    	for (int i = 0; i < height; i++) {
    	    world.getBlockAt(origin.getBlockX(), origin.getBlockY() + i, origin.getBlockZ()).setType(trunk);
    	 
    	    if (i >= height - 2) {
    	        world.getBlockAt(origin.getBlockX() - 1, origin.getBlockY() + i, origin.getBlockZ()).setType(leaves);
    	        world.getBlockAt(origin.getBlockX() + 1, origin.getBlockY() + i, origin.getBlockZ()).setType(leaves);
    	        world.getBlockAt(origin.getBlockX(), origin.getBlockY() + i, origin.getBlockZ() - 1).setType(leaves);
    	        world.getBlockAt(origin.getBlockX(), origin.getBlockY() + i, origin.getBlockZ() + 1).setType(leaves);
    	    }
    	 
    	    if (i >= height - 4 && i < height - 2) {
    	        for (int j = -2; j < 3; j++) {
    	            for (int k = -2; k < 3; k++) {
    	                if (j == 0 && k == 0)
    	                    continue;
    	                world.getBlockAt(origin.getBlockX() + j, origin.getBlockY() + i, origin.getBlockZ() + k).setType(leaves);
    	            }
    	        }
    	    }
    	 
    	}
    	 
    	world.getBlockAt(origin.getBlockX(), origin.getBlockY() + height, origin.getBlockZ()).setType(leaves);
    }

	public static void initElf() {
		//WolvMC.addMission("elf.1", (double) 150, "elf", "Eat %goal% inedibles fruits in leaves of trees", "Night Vision");
	 	//WolvMC.addMission("elf.2", (double) 40, "elf", "Make grow %goal% trees", "Speed III");
		WolvMC.addRace("elf", ChatColor.DARK_GREEN + "Elf", new ItemStack(Material.BOW, 1));
		WolvMC.addMission("elf.1", (double) 150, "elf", "Manger %goal% fruits non comestibles dans les feuilles", "Vision Nocturne");
	 	WolvMC.addMission("elf.2", (double) 40, "elf", "Faire pousser %goal% arbres", "Vitesse III");
		WolvMC.getPlugin(WolvMC.class).getLogger().fine("Elf Class loaded!");
		/*if(WolvMC.getPlugin(WolvMC.class).getServer().getPluginManager().getPlugin("ProtocolLib")!=null) {
 		    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(WolvMC.getPlugin(WolvMC.class), PacketType.Play.Server.NAMED_SOUND_EFFECT) {
 		    	@Override
 		    	public void onPacketSending(PacketEvent event) {
		    		 System.out.println();
  	    	}
 		    });
 		}*/
        /*BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
            @Override
            public void run() {
            	if(glowAPI) {
	    			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
	    				if(WolvMC.getRace(p.getName()).equals("elf")) {
	    					for(Player p2 : Bukkit.getServer().getOnlinePlayers()) {
	    						if(p2.getWorld()==p.getWorld() && p2.getName()!=p.getName()) {
	    							if(p2.getLocation().distance(p.getLocation())<=60) {
	    								GlowAPI.setGlowing(p2, Color.DARK_GREEN, p);
	    							}
	    							else {
	    								GlowAPI.setGlowing(p2, false, p);
	    							}
	    						}
	    					}
	    				}
	    			}
            	}
            }
        }, 0L, 20L);*/
	}
	
}