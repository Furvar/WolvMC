package fr.nashoba24.wolvmc.races;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.events.WolvMCInitEffectsEvent;

public class Orc implements Listener {

	public static boolean enabled = true;
	static String diamondGive = ChatColor.GREEN + "Vous avez réussi a recupérer un diamant lors du craft!";
	static Double chanceDiamond = 33.0;
	static String ironGive = ChatColor.GREEN + "Vous avez réussi a recupérer un lingot de fer lors du craft!";
	static Double chanceIron = 33.0;
	static String goldGive = ChatColor.GREEN + "Vous avez réussi a recupérer un lingot d'or lors du craft!";
	static Double chanceGold = 33.0;
	public HashMap<String, Long> steal = new HashMap<String, Long>();
	public HashMap<String, Long> stealbug = new HashMap<String, Long>();
	static String noEmptySlot = ChatColor.RED + "Vous n'avez pas de place dans votre inventaire!";
	static String nothingToSteal = ChatColor.RED + "Il n'y a rien à voler chez ce joueur!";
	static String stealSuccess = ChatColor.GREEN + "Vous avez volé: %itemstack% chez %player%";
	static String stealed = ChatColor.RED + "%player% vous volé: %itemstack%";
	static boolean expl = false;
	static Integer strengthMission = 4;
	static Integer strengthDefault = 2;
	static Integer healBoost = 3;
	static String massName = ChatColor.GRAY + "Masse en fer";
	static String cudgelName = ChatColor.GRAY + "Gourdin en os";
	static String hunterBow = ChatColor.GOLD + "Arc du chasseur";
	static String chamanStick = ChatColor.DARK_PURPLE + "Bâton du chaman";
	static String notAnOrc = ChatColor.RED + "Vous n'êtes pas un Orc!";
	static HashMap<String, Long> stick = new HashMap<String, Long>();
	static HashMap<String, Long> stickbug = new HashMap<String, Long>();
	static ShapedRecipe MassRecipe;
	static ShapedRecipe CudgelRecipe;
	static ShapedRecipe ChamanRecipe;
	
	@EventHandler
	public void onInitEffects(WolvMCInitEffectsEvent e) {
		Player p  = e.getPlayer();
		if(e.getRace().equals("orc")) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 2147483647, healBoost - 1));
			if(!WolvMC.hasFinishMission("orc.2", p.getName())) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2147483647, 0));
			}
			else {
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 0));
			}
			if(WolvMC.hasFinishMission("orc.1", p.getName())) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, strengthMission - 1));
			}
			else {
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, strengthDefault - 1));
			}
		}
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(WolvMC.getRace(p.getName()).equals("orc")) {
			if(e.getCurrentItem().getType()==Material.DIAMOND_PICKAXE || e.getCurrentItem().getType()==Material.DIAMOND_SPADE || e.getCurrentItem().getType()==Material.DIAMOND_SWORD || e.getCurrentItem().getType()==Material.DIAMOND_HOE || e.getCurrentItem().getType()==Material.DIAMOND_AXE) {
				Random rand = new Random();
				int number = rand.nextInt(100) + 1;
				if(number <= chanceDiamond)
				{
					p.sendMessage(diamondGive);
					p.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
				}
			}
			if(e.getCurrentItem().getType()==Material.IRON_PICKAXE || e.getCurrentItem().getType()==Material.IRON_SPADE || e.getCurrentItem().getType()==Material.IRON_SWORD || e.getCurrentItem().getType()==Material.IRON_HOE || e.getCurrentItem().getType()==Material.IRON_AXE) {
				Random rand = new Random();
				int number = rand.nextInt(100) + 1;
				if(number <= chanceIron)
				{
					p.sendMessage(ironGive);
					p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 1));
				}
			}
			if(e.getCurrentItem().getType()==Material.GOLD_PICKAXE || e.getCurrentItem().getType()==Material.GOLD_SPADE || e.getCurrentItem().getType()==Material.GOLD_SWORD || e.getCurrentItem().getType()==Material.GOLD_HOE || e.getCurrentItem().getType()==Material.GOLD_AXE) {
				Random rand = new Random();
				int number = rand.nextInt(100) + 1;
				if(number <= chanceGold)
				{
					p.sendMessage(goldGive);
					p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1));
				}
			}
		}
	}
	
/*	@EventHandler
	public void onSteal(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("orc")) {
			if(p.isSneaking() && WolvMC.canUsePowerSafe(e.getRightClicked().getLocation(), p) && e.getRightClicked() instanceof Player) {
				Player p2 = (Player) e.getRightClicked();
			       int cooldownTime = 30;
			        if(steal.containsKey(p.getName())) {
						Long bug = stealbug.get(p.getName());
						if(System.currentTimeMillis() - bug < 20) {
							return;
						}
			            long secondsLeft = ((steal.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
			            if(secondsLeft>0) {
			            	stealbug.put(p.getName(), System.currentTimeMillis());
			            	int seconds = (int) secondsLeft;
			            	p.sendMessage(WolvMC.msgCooldown(seconds));
			                return;
			            }
			        }
			        stealbug.put(p.getName(), System.currentTimeMillis());
			        boolean air = false;
		        	for(int i = 0 ; i < p.getInventory().getSize() ; i++) {
		        		if(!(i>=36 && i<=40)) {
			        	    ItemStack item = p.getInventory().getItem(i);
			        	    if(item==null) {
			        	    	air = true;
			        	    	break;
			        	    }
		        		}
		        	}
		        	if(!air) {
		        		p.sendMessage(noEmptySlot);
		        		return;
		        	}
			        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
			        for(ItemStack i : p2.getInventory().getContents()) {
			        	list.add(i);
			        }
			        if(list.size()==0) {
			        	p.sendMessage(nothingToSteal);
			        	return;
			        }
			        Random rand = new Random();
			        ItemStack is = list.get(rand.nextInt(list.size()));
			        for(int i = 0 ; i < p2.getInventory().getSize() ; i++) {
			        	if(is==p.getInventory().getItem(i)) {
			        		if(is.getAmount()==1) {
			        			p.getInventory().setItem(i, null);
			        		}
			        		else {
			        			is.setAmount(is.getAmount() - 1);
			        			p.getInventory().setItem(i, is);
			        		}
			        	}
			        }
			        is.setAmount(1);
			        p.getInventory().addItem(is);
			        p.sendMessage(stealSuccess.replaceAll("%player%", p2.getName()).replaceAll("%itemstack%", is.toString()));
			        p2.sendMessage(stealed.replaceAll("%player%", p.getName()).replaceAll("%itemstack%", is.toString()));
			        WolvMC.addNbToMission("orc.1", p.getName(), 1.0);
			        steal.put(p.getName(), System.currentTimeMillis());
			}
		}
	}*/
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player && e.getCause()==DamageCause.FALL) {
			if(WolvMC.getRace(((Player) e.getEntity()).getName()).equals("orc")) {
				WolvMC.addNbToMission("orc.1", ((Player) e.getEntity()).getName(), e.getFinalDamage());
//				if(WolvMC.canUsePowerBlock(e.getEntity().getLocation())) {
//					e.getEntity().getWorld().createExplosion(e.getEntity().getLocation().getX(), e.getEntity().getLocation().getY(), e.getEntity().getLocation().getZ(), e.getEntity().getFallDistance() / 10, expl, expl);
//				}
			}
		}
	}
	
	@EventHandler
	public void onDamageItems(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			ItemStack is = p.getInventory().getItemInMainHand();
			if(is!=null) {
				if(is.hasItemMeta()) {
					if(is.getItemMeta().getLore() != null) {
						if(is.getItemMeta().getLore().size()>0) {
							if(is.getItemMeta().getLore().get(0).equals("Orc's Mass")) {
								if(WolvMC.getRace(p.getName()).equals("orc") && e.getEntity() instanceof LivingEntity) {
									Random rand = new Random();
									Integer number = rand.nextInt(100) + 1;
									if(number <= 10)
									{
										number = rand.nextInt(100) + 1;
										if(number <=50) {
											((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
										}
										else {
											((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
										}
									}
								}
								else {
									e.setCancelled(true);
									p.damage(2);
									p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
								}
							}
							else if(is.getItemMeta().getLore().get(0).equals("Orc's Cudgel")) {
								if(WolvMC.getRace(p.getName()).equals("orc") && e.getEntity() instanceof LivingEntity) {
									Random rand = new Random();
									Integer number = rand.nextInt(100) + 1;
									if(number <= 5)
									{
										if(((LivingEntity) e.getEntity()).getEquipment().getItemInMainHand()!=null) {
											if(((LivingEntity) e.getEntity()).getEquipment().getItemInMainHand().getType()!=Material.AIR) {
												e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), ((LivingEntity) e.getEntity()).getEquipment().getItemInMainHand());
												((LivingEntity) e.getEntity()).getEquipment().setItemInMainHand(null);
											}
										}
									}
									number = rand.nextInt(100) + 1;
									if(number <= 20)
									{
										Entity attacker = e.getDamager();
										Entity attacked = e.getEntity();
										float knockback = 2.0F;
										attacked.setVelocity(attacked.getVelocity().add(attacked.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize().multiply(knockback)));
									}
								}
								else {
									e.setCancelled(true);
									p.damage(2);
									p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onCraftOrc(CraftItemEvent e) {
		if(e.getRecipe().getResult().hasItemMeta()) {
			if(e.getRecipe().getResult().getItemMeta().getDisplayName().equals(massName) || e.getRecipe().getResult().getItemMeta().getDisplayName().equals(cudgelName) || e.getRecipe().getResult().getItemMeta().getDisplayName().equals(chamanStick)) {
				if(!WolvMC.getRace(e.getWhoClicked().getName()).equals("orc")) {
					e.setCancelled(true);
					e.getWhoClicked().sendMessage(notAnOrc);
				}
				else {
					WolvMC.addNbToMission("orc.2", e.getWhoClicked().getName(), 1.00);
				}
			}
		}
	}
	
	@EventHandler
	public void onChamanStick(PlayerInteractEvent e) {
		if(e.getAction()==Action.RIGHT_CLICK_BLOCK) {
			Player p = e.getPlayer();
			if(p.isSneaking() && p.getInventory().getItemInMainHand()!=null) {
				if(p.getInventory().getItemInMainHand().getItemMeta()!=null && p.getInventory().getItemInMainHand().getItemMeta().getLore()!=null) {
					if(p.getInventory().getItemInMainHand().getItemMeta().getLore().get(0).equals("Orc's Chaman Stick")) {
						if(WolvMC.getRace(p.getName()).equals("orc")) {
						       int cooldownTime = 90;
						        if(stick.containsKey(p.getName())) {
						        	if(stickbug.containsKey(p.getName())) {
										Long bug = stickbug.get(p.getName());
										if(System.currentTimeMillis() - bug < 20) {
											return;
										}
						        	}
						            long secondsLeft = ((stick.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
						            if(secondsLeft>0) {
						            	stickbug.put(p.getName(), System.currentTimeMillis());
						            	int seconds = (int) secondsLeft;
						            	p.sendMessage(WolvMC.msgCooldown(seconds));
						                return;
						            }
						        }
					        stick.put(p.getName(), System.currentTimeMillis());
					        stickbug.put(p.getName(), System.currentTimeMillis());
						    if(!WolvMC.canUsePowerBlock(e.getClickedBlock().getLocation(), p)) {
						    	stickbug.put(p.getName(), System.currentTimeMillis());
						    	return;
							}
						    p.setHealth(p.getMaxHealth());
					        List<Entity> list = p.getNearbyEntities(10, 10, 10);
					        for(Entity ent : list) {
					        	if(ent instanceof Player) {
					        		if(WolvMC.getRace(((Player) ent).getName()).equals("orc")) {
					        			((Player) ent).setHealth(((Player) ent).getMaxHealth());
					        		}
					        	}
					        }
						}
						else {
							p.sendMessage(notAnOrc);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onTameHorse(PlayerInteractEntityEvent e) {
		if(e.getRightClicked() instanceof Horse) {
			Player p = e.getPlayer();
			if(p.getInventory().getItemInMainHand()!=null) {
				if(p.getInventory().getItemInMainHand().getItemMeta()!=null && p.getInventory().getItemInMainHand().getItemMeta().getLore()!=null) {
					if(p.getInventory().getItemInMainHand().getItemMeta().getLore().get(0).equals("Orc's Chaman Stick")) {
						if(WolvMC.getRace(p.getName()).equals("orc")) {
							Horse horse = (Horse) e.getRightClicked();
							if(!horse.isTamed()) {
								horse.setTamed(true);
								horse.getWorld().playEffect(horse.getEyeLocation(), Effect.SPELL, 60);
								p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0F, 1.0F);
							}
						}
						else {
							p.sendMessage(notAnOrc);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if((e.getItem().getType()!=Material.ROTTEN_FLESH || e.getItem().getType()!=Material.POTION) && WolvMC.getRace(e.getPlayer().getName()).equals("orc")) {
			e.getPlayer().damage(2);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void initOrc() {
		//WolvMC.addMission("orc.1", (double) 750, "orc", "Take %goal% unit of damages", "Strength IV");
	 	//WolvMC.addMission("orc.2", (double) 1, "orc", "Craft one of the three Orc's items", "No longer slowness");
	 	WolvMC.addRace("orc", ChatColor.GREEN + "Orc", new ItemStack(Material.SLIME_BLOCK, 1));
		WolvMC.addMission("orc.1", (double) 750, "orc", "Encaisser un équivalent de %goal% unités de dégâts", "Force IV");
	 	WolvMC.addMission("orc.2", (double) 1, "orc", "Crafter un des trois items de l'Orc", "Plus de lenteur");
	 	ItemStack is = new ItemStack(Material.IRON_SPADE, 1);
	 	is.addUnsafeEnchantment(Enchantment.DURABILITY, 6);
	 	is.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
	 	ItemMeta meta = is.getItemMeta();
	 	meta.setDisplayName(massName);
	 	meta.setLore(Arrays.asList("Orc's Mass"));
	 	is.setItemMeta(meta);
	 	ShapedRecipe recipe = new ShapedRecipe(is);
	 	recipe.shape("IWI", " S ", " S ");
	 	recipe.setIngredient('I', Material.IRON_INGOT).setIngredient('W', Material.SKULL_ITEM, 1).setIngredient('S', Material.STICK);
	 	WolvMC.getPlugin(WolvMC.class).getServer().addRecipe(recipe);
	 	MassRecipe = recipe;
	 	is = new ItemStack(Material.BONE, 1);
	 	is.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
	 	meta = is.getItemMeta();
	 	meta.setDisplayName(cudgelName);
	 	meta.setLore(Arrays.asList("Orc's Cudgel"));
	 	is.setItemMeta(meta);
	 	recipe = new ShapedRecipe(is);
	 	recipe.shape("BWB", " BA", " B ");
	 	recipe.setIngredient('B', Material.BONE).setIngredient('W', Material.SKULL_ITEM, 1);
	 	WolvMC.getPlugin(WolvMC.class).getServer().addRecipe(recipe);
	 	CudgelRecipe = recipe;
/*	 	is = new ItemStack(Material.BOW, 1);
	 	is.getItemMeta().setDisplayName(hunterBow);
	 	is.getItemMeta().setLore(Arrays.asList("Orc's Hunter Bow"));
	 	recipe = new ShapedRecipe(is);
	 	recipe.shape("FSA", "FWS", "FSA");
	 	recipe.setIngredient('F', Material.STRING).setIngredient('W', Material.SKULL_ITEM, 1).setIngredient('A', Material.AIR).setIngredient('S', Material.STICK);
	 	WolvMC.getPlugin(WolvMC.class).getServer().addRecipe(recipe);*/
	 	is = new ItemStack(Material.STICK, 1);
	 	is.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
	 	meta = is.getItemMeta();
	 	meta.setDisplayName(chamanStick);
	 	meta.setLore(Arrays.asList("Orc's Chaman Stick"));
	 	is.setItemMeta(meta);
	 	recipe = new ShapedRecipe(is);
	 	recipe.shape(" W ", " S ", " S ");
	 	recipe.setIngredient('S', Material.STICK).setIngredient('W', Material.SKULL_ITEM, 1);
	 	WolvMC.getPlugin(WolvMC.class).getServer().addRecipe(recipe);
	 	ChamanRecipe = recipe;
	 	is = new ItemStack(Material.STICK, 1);
	 	is.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
	 	is.setItemMeta(meta);
	 	recipe = new ShapedRecipe(is);
	 	recipe.shape("W  ", "S  ", "S  ");
	 	recipe.setIngredient('S', Material.STICK).setIngredient('W', Material.SKULL_ITEM, 1);
	 	WolvMC.getPlugin(WolvMC.class).getServer().addRecipe(recipe);
	 	is = new ItemStack(Material.STICK, 1);
	 	is.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
	 	is.setItemMeta(meta);
	 	recipe = new ShapedRecipe(is);
	 	recipe.shape("  W", "  S", "  S");
	 	recipe.setIngredient('S', Material.STICK).setIngredient('W', Material.SKULL_ITEM, 1);
	 	WolvMC.getPlugin(WolvMC.class).getServer().addRecipe(recipe);
	 	WolvMC.getPlugin(WolvMC.class).getLogger().fine("Orc Class loaded!");
	}
}
