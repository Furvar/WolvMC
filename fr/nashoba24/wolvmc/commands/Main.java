package fr.nashoba24.wolvmc.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.utils.UUIDFetcher;

public class Main implements CommandExecutor {
	
	public static String newRaceMSG = ChatColor.GREEN + "%player% is now a %race%";
	public static String saveModified = ChatColor.GREEN + "No player named %player% is not online. So his save file has been modified!";
	public static String neverJoined = ChatColor.RED + "The player %player% has never joined the server!";
	public static String infos = ChatColor.translateAlternateColorCodes('&', "&6Uuid: &b%uuid%/n/&6Race: &b%race%/n/&r&6Race name: %race-name%/n/&r&6Play time: &b%hours%h%minutes%min&r");
	public static String missions = ChatColor.GOLD + "Mission %mission%: " + ChatColor.AQUA + "%number%/%total%";
	public static String prefixMSG = ChatColor.GREEN + "The prefix of %player% has been set to %prefix%" + ChatColor.GREEN + "!";
	public static String prefixReset = ChatColor.GREEN + "The prefix of %player% has been reset!";
	public static String notExist = ChatColor.RED + "The race %race% doesn't exist!";
	public static String noChangeLeft = ChatColor.RED + "You don't have change available!";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length==0) {
			if(sender.hasPermission("wolvmc.setrace")) {
				sender.sendMessage(ChatColor.GOLD + "/wolvmc " + ChatColor.GREEN + "setrace <player> <race>");
			}
			if(sender.hasPermission("wolvmc.reload")) {
				sender.sendMessage(ChatColor.GOLD + "/wolvmc " + ChatColor.GREEN + "reload");
			}
			if(sender.hasPermission("wolvmc.info.other")) {
				sender.sendMessage(ChatColor.GOLD + "/wolvmc " + ChatColor.GREEN + "info [player]");
			}
			else if(sender.hasPermission("wolvmc.info.self")) {
				sender.sendMessage(ChatColor.GOLD + "/wolvmc " + ChatColor.GREEN + "info");
			}
			if(sender.hasPermission("wolvmc.setprefix")) {
				sender.sendMessage(ChatColor.GOLD + "/wolvmc " + ChatColor.GREEN + "setprefix <player> <prefix>");
			}
			if(sender.hasPermission("wolvmc.resetprefix")) {
				sender.sendMessage(ChatColor.GOLD + "/wolvmc " + ChatColor.GREEN + "resetprefix <player>");
			}
			if(sender.hasPermission("wolvmc.list")) {
				sender.sendMessage(ChatColor.GOLD + "/wolvmc " + ChatColor.GREEN + "list");
			}
			return true;
		}
		else if(args.length>0) {
			if(args.length==1) {
				if(args[0].equalsIgnoreCase("help")) {
					WolvMC.getPlugin(WolvMC.class).getServer().dispatchCommand(sender, "wolvmc");
					return true;
				}
				else if(args[0].equalsIgnoreCase("setrace")) {
					if(sender.hasPermission("wolvmc.setrace")) {
						sender.sendMessage(ChatColor.GOLD + "/wolvmc " + ChatColor.GREEN + "setrace <player> <race>");
						return true;
					}
					else {
						sender.sendMessage(WolvMC.noPerm);
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("list")) {
					if(sender.hasPermission("wolvmc.list")) {
						ArrayList<String> list = WolvMC.getAllRaces();
						String msg = String.join(", ", list);
						sender.sendMessage(ChatColor.GREEN + msg);
						return true;
					}
					else {
						sender.sendMessage(WolvMC.noPerm);
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("reload")) {
					if(sender.hasPermission("wolvmc.reload")) {
						WolvMC.reloadConfig(true);
						sender.sendMessage(ChatColor.GREEN + "Reloaded!");
						return true;
					}
					else {
						sender.sendMessage(WolvMC.noPerm);
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("change")) {
					if(sender instanceof Player) {
						Player p = (Player) sender;
						if(p.hasPermission("wolvmc.change")) {
							if(WolvMC.changesLeft(p.getName())>0 || p.hasPermission("wolvmc.changes.inf")) {
								WolvMC.openChooseInventory(p, true);
								return true;
							}
							else {
								p.sendMessage(noChangeLeft);
								return true;
							}
						}
						else {
							sender.sendMessage(WolvMC.noPerm);
							return true;
						}
					}
					else {
						sender.sendMessage(ChatColor.RED + "This command can only be executed by a player!");
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("info")) {
					if(sender.hasPermission("wolvmc.info.self")) {
						if(sender instanceof Player) {
							Player p = (Player) sender;
							String msg = infos.replaceAll("%uuid%", p.getUniqueId().toString()).replaceAll("%race%", WolvMC.getRace(p.getName())).replaceAll("%race-name%", WolvMC.getPrefix(WolvMC.getRace(p.getName())));
							Integer time = WolvMC.getTime(p.getName());
							Integer minutes = time % 60;
							Integer hours = (time - minutes) / 60;
							msg = msg.replaceAll("%hours%", hours.toString()).replaceAll("%minutes%", minutes.toString());
							String[] list = msg.split("/n/");
							for(String s : list) {
								p.sendMessage(s);
							}
							ArrayList<String> miss = WolvMC.getMissionsForRace(WolvMC.getRace(p.getName()));
							for(String s : miss) {
								p.sendMessage(missions.replaceAll("%mission%", s).replaceAll("%number%", WolvMC.getPlayerMission(s, p.getName()).intValue() + "").replaceAll("%total%", WolvMC.getMissionGoal(s).intValue() + ""));
							}
							return true;
						}
						else {
							sender.sendMessage(ChatColor.RED + "This command can only be executed by a player");
							return true;
						}
					}
					else {
						sender.sendMessage(WolvMC.noPerm);
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("setprefix")) {
					if(sender.hasPermission("wolvmc.setprefix")) {
						sender.sendMessage(ChatColor.GOLD + "/wolvmc " + ChatColor.GREEN + "setprefix <player> <prefix>");
						return true;
					}
					else {
						sender.sendMessage(WolvMC.noPerm);
						return true;
					}
				}
			}
			else if(args.length==2) {
				if(args[0].equalsIgnoreCase("setrace")) {
					if(sender.hasPermission("wolvmc.setrace")) {
						sender.sendMessage(ChatColor.GOLD + "/wolvmc " + ChatColor.GREEN + "setrace <player> <race>");
						return true;
					}
					else {
						sender.sendMessage(WolvMC.noPerm);
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("resetprefix")) {
					if(sender.hasPermission("wolvmc.resetprefix")) {
						for(Player player : WolvMC.getPlugin(WolvMC.class).getServer().getOnlinePlayers()) {
							if(player.getName().equalsIgnoreCase(args[1])) {
								WolvMC.setCustomPrefix(player.getName(), "");
								sender.sendMessage(prefixReset.replaceAll("%player%", player.getName()));
								return true;
							}
						}
						UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(args[1]));
						Map<String, UUID> response = null;
						try {
							response = fetcher.call();
						} catch (Exception e) {
							WolvMC.getPlugin(WolvMC.class).getLogger().warning("Exception while running UUIDFetcher");
							e.printStackTrace();
						}
						File file = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/saves/" + response.get(args[1]) + ".yml");
						if(file.exists()) {
							FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
							ConfigurationSection section = conf.getConfigurationSection("data");
							section.set("custom-prefix", "");
							try {
								conf.save(file);
							} catch (IOException e) {
								e.printStackTrace();
							}
							sender.sendMessage(saveModified);
							return true;
						}
						else {
							sender.sendMessage(neverJoined.replaceAll("%player%", args[1]));
							return true;
						}
					}
				}
				else if(args[0].equalsIgnoreCase("info")) {
					if(sender.hasPermission("wolvmc.info.other")) {
						for(Player player : WolvMC.getPlugin(WolvMC.class).getServer().getOnlinePlayers()) {
							if(player.getName().equalsIgnoreCase(args[1])) {
								String msg = infos.replaceAll("%uuid%", player.getUniqueId().toString()).replaceAll("%race%", WolvMC.getRace(player.getName())).replaceAll("%race-name%", WolvMC.getPrefix(WolvMC.getRace(player.getName())));
								Integer time = WolvMC.getTime(player.getName());
								Integer minutes = time % 60;
								Integer hours = (time - minutes) / 60;
								msg = msg.replaceAll("%hours%", hours.toString()).replaceAll("%minutes%", minutes.toString());
								String[] list = msg.split("/n/");
								for(String s : list) {
									sender.sendMessage(s);
								}
								ArrayList<String> miss = WolvMC.getMissionsForRace(WolvMC.getRace(player.getName()));
								for(String s : miss) {
									sender.sendMessage(missions.replaceAll("%mission%", s).replaceAll("%number%", WolvMC.getPlayerMission(s, player.getName()).intValue() + "").replaceAll("%total%", WolvMC.getMissionGoal(s).intValue() + ""));
								}
								return true;
							}
						}
							UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(args[1]));
							Map<String, UUID> response = null;
							try {
								response = fetcher.call();
							} catch (Exception e) {
								WolvMC.getPlugin(WolvMC.class).getLogger().warning("Exception while running UUIDFetcher");
								e.printStackTrace();
							}
							File file = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/saves/" + response.get(args[1]) + ".yml");
							if(file.exists()) {
								FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
								ConfigurationSection section = conf.getConfigurationSection("data");
								String msg = infos.replaceAll("%uuid%", response.get(args[1]).toString()).replaceAll("%race%", section.getString("race")).replaceAll("%race-name%", WolvMC.getPrefix(section.getString("race")));
								Integer time = section.getInt("time");
								Integer minutes = time % 60;
								Integer hours = (time - minutes) / 60;
								msg = msg.replaceAll("%hours%", hours.toString()).replaceAll("%minutes%", minutes.toString());
								String[] list = msg.split("/n/");
								for(String s : list) {
									sender.sendMessage(s);
								}
								ArrayList<String> miss = WolvMC.getMissionsForRace(WolvMC.getRace(section.getString("race")));
								for(String s : miss) {
									sender.sendMessage(missions.replaceAll("%mission%", s).replaceAll("%number%", ((int) section.getDouble(s)) + "").replaceAll("%total%", WolvMC.getMissionGoal(s).intValue() + ""));
								}
								return true;
							}
							else {
								sender.sendMessage(neverJoined.replaceAll("%player%", args[1]));
								return true;
						}
					}
				}
				else if(args[0].equalsIgnoreCase("setprefix")) {
					if(sender.hasPermission("wolvmc.setprefix")) {
						sender.sendMessage(ChatColor.GOLD + "/wolvmc " + ChatColor.GREEN + "setprefix <player> <prefix>");
						return true;
					}
					else {
						sender.sendMessage(WolvMC.noPerm);
						return true;
					}
				}
			}
			else {
				if(args[0].equalsIgnoreCase("setrace")) {
					if(sender.hasPermission("wolvmc.setrace")) {
						if(!WolvMC.raceExist(args[2])) {
							sender.sendMessage(notExist.replaceAll("%race%", args[2]));
							return true;
						}
						for(Player p : WolvMC.getPlugin(WolvMC.class).getServer().getOnlinePlayers()) {
							if(p.getName().equalsIgnoreCase(args[1])) {
								WolvMC.setRace(p, args[2]);
								sender.sendMessage(newRaceMSG.replaceAll("%player%", args[1]).replaceAll("%race%", WolvMC.getPrefix(args[2])));
								return true;
							}
						}
						UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(args[1]));
						Map<String, UUID> response = null;
						try {
							response = fetcher.call();
						} catch (Exception e) {
							WolvMC.getPlugin(WolvMC.class).getLogger().warning("Exception while running UUIDFetcher");
							e.printStackTrace();
						}
						File file = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/saves/" + response.get(args[1]) + ".yml");
						if(file.exists()) {
							FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
							ConfigurationSection section = conf.getConfigurationSection("data");
							section.set("race", args[2]);
							try {
								conf.save(file);
							} catch (IOException e) {
								e.printStackTrace();
							}
							sender.sendMessage(saveModified);
							return true;
						}
						else {
							sender.sendMessage(neverJoined.replaceAll("%player%", args[1]));
							return true;
						}
					}
					else {
						sender.sendMessage(WolvMC.noPerm);
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("setprefix")) {
					if(sender.hasPermission("wolvmc.setprefix")) {
						for(Player p : WolvMC.getPlugin(WolvMC.class).getServer().getOnlinePlayers()) {
							if(p.getName().equalsIgnoreCase(args[1])) {
								if(args.length==3) {
									WolvMC.setCustomPrefix(p.getName(), args[2]);
									sender.sendMessage(prefixMSG.replaceAll("%player%", args[1]).replaceAll("%prefix%", ChatColor.translateAlternateColorCodes('&', args[2])));
									return true;
								}
								else {
									Integer i = args.length;
									i = i - 3;
									String prefix = "";
									while(i>=0) {
										if(prefix.equalsIgnoreCase("")) {
											prefix = args[2 + i];
										}
										else {
											prefix = args[2 + i] + " " + prefix;
										}
										--i;
									}
									WolvMC.setCustomPrefix(p.getName(), ChatColor.translateAlternateColorCodes('&', prefix));
									sender.sendMessage(prefixMSG.replaceAll("%player%", args[1]).replaceAll("%prefix%", ChatColor.translateAlternateColorCodes('&', prefix)));
									return true;
								}
							}
						}
						UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(args[1]));
						Map<String, UUID> response = null;
						try {
							response = fetcher.call();
						} catch (Exception e) {
							WolvMC.getPlugin(WolvMC.class).getLogger().warning("Exception while running UUIDFetcher");
							e.printStackTrace();
						}
						File file = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/saves/" + response.get(args[1]) + ".yml");
						if(file.exists()) {
							FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
							ConfigurationSection section = conf.getConfigurationSection("data");
							if(args.length==3) {
								section.set("custom-prefix", args[2]);
							}
							else {
								Integer i = args.length;
								i = i - 2;
								String prefix = "";
								while(i>0) {
									if(prefix.equalsIgnoreCase("")) {
										prefix = args[2 + i];
									}
									else {
										prefix = args[2 + i] + " " + prefix;
									}
									--i;
								}
								section.set("custom-prefix", prefix);
							}
							try {
								conf.save(file);
							} catch (IOException e) {
								e.printStackTrace();
							}
							sender.sendMessage(saveModified);
							return true;
						}
						else {
							sender.sendMessage(neverJoined.replaceAll("%player%", args[1]));
							return true;
						}
					}
					else {
						sender.sendMessage(WolvMC.noPerm);
						return true;
					}
				}
				else if(args[0].equals("setnick")) {
					if(sender.hasPermission("wolvmc.setnick")) {
					    Plugin plugin = WolvMC.getPlugin(WolvMC.class).getServer().getPluginManager().getPlugin("SuperTrails");
					    if (plugin != null) {
							for(Player p : WolvMC.getPlugin(WolvMC.class).getServer().getOnlinePlayers()) {
								if(p.getName().equalsIgnoreCase(args[1])) {
							    	Essentials ess = ((Essentials) WolvMC.getPlugin(WolvMC.class).getServer().getPluginManager().getPlugin("Essentials"));
							    	User user = ess.getUser(p);
							    	user.setNickname(ChatColor.translateAlternateColorCodes('&', args[2]));
							    	return true;
								}
							}
					    }
					}
				}
			}
		}
		WolvMC.getPlugin(WolvMC.class).getServer().dispatchCommand(sender, "wolvmc");
		return true;
	}
}
