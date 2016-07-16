package fr.nashoba24.wolvmc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import me.kvq.plugin.trails.API.SuperTrailsAPI;
import me.libraryaddict.disguise.DisguiseAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;

import fr.nashoba24.wolvmc.commands.Brume;
import fr.nashoba24.wolvmc.commands.Dracorage;
import fr.nashoba24.wolvmc.commands.Main;
import fr.nashoba24.wolvmc.commands.Missions;
import fr.nashoba24.wolvmc.commands.Nether;
import fr.nashoba24.wolvmc.commands.Pig;
import fr.nashoba24.wolvmc.commands.Transf;
import fr.nashoba24.wolvmc.commands.Untransf;
import fr.nashoba24.wolvmc.commands.VampireCMD;
import fr.nashoba24.wolvmc.commands.WerewolfStats;
import fr.nashoba24.wolvmc.events.WolvMCInitEffectsEvent;
import fr.nashoba24.wolvmc.events.WolvMCReloadEvent;
import fr.nashoba24.wolvmc.events.WolvMCSaveEvent;
import fr.nashoba24.wolvmc.races.Angel;
import fr.nashoba24.wolvmc.races.Daemon;
import fr.nashoba24.wolvmc.races.Draco;
import fr.nashoba24.wolvmc.races.Elf;
import fr.nashoba24.wolvmc.races.Fairy;
import fr.nashoba24.wolvmc.races.Orc;
import fr.nashoba24.wolvmc.races.Vampire;
import fr.nashoba24.wolvmc.races.Werewolf;
import fr.nashoba24.wolvmc.races.Zorlim;
import fr.nashoba24.wolvmc.utils.ArmorListener;
import fr.nashoba24.wolvmc.utils.Colors;
import fr.nashoba24.wolvmc.utils.SexLogo;
import fr.nashoba24.wolvmc.utils.ZipUsingJavaUtils;

public class WolvMC extends JavaPlugin implements Listener {
	
	static HashMap<String, String> races = new HashMap<String, String>();
	static HashMap<String, Double> missions = new HashMap<String, Double>();
	static HashMap<String, Double> pmissions = new HashMap<String, Double>();
	static HashMap<String, Integer> playtime = new HashMap<String, Integer>();
	static HashMap<String, String> prefixs = new HashMap<String, String>();
	static HashMap<String, String> racemissions = new HashMap<String, String>();
	static HashMap<String, String> descrmissions = new HashMap<String, String>();
	static HashMap<String, String> finishmsgmissions = new HashMap<String, String>();
	static HashMap<String, String> cprefix = new HashMap<String, String>();
	static HashMap<String, Integer> changes = new HashMap<String, Integer>();
	static HashMap<String, ItemStack> items = new HashMap<String, ItemStack>();
	static ArrayList<String> regionsblock = new ArrayList<String>();
	static ArrayList<String> regionssafe = new ArrayList<String>();
	static String milk = ChatColor.RED + "Milk Buckets are not allowed on this server!";
	static String power = ChatColor.RED + "You can't use this power here!";
	static String cooldown = ChatColor.RED + "You must wait %secs% seconds before re-use this power!";
	static String cooldown1sec = ChatColor.RED + "You must wait 1 second before re-use this power!";
	public static String noPerm = ChatColor.RED + "You do not have permission to perform that command!";
	static String becomeRace = ChatColor.GREEN + "You are now a %race%" + ChatColor.GREEN + "!";
	static boolean randomRace = true;
	static List<String> defaultRaces = Arrays.asList("vampire", "werewolf", "fairy", "daemon", "angel", "orc", "elf");
	static boolean menuRace = false;
	static Integer changesBase = 1;

	@Override
	  public void onEnable() {
		File file = new File(this.getDataFolder() + "/");
		if(!file.exists()) {
			file.mkdir();
		}
		file = new File(this.getDataFolder() + "/backups/");
		if(!file.exists()) {
			file.mkdir();
		}
		file = new File(this.getDataFolder() + "/saves/");
		if(!file.exists()) {
			file.mkdir();
		}
		  WolvMC.reloadConfig(true);
		  Bukkit.getPluginManager().registerEvents(this, this);
		  if(Vampire.enabled) {
			  Bukkit.getPluginManager().registerEvents(new Vampire(), this);
			  if(Vampire.vampireCMD) {
				   getCommand("vampire").setExecutor(new VampireCMD());
			  }
			  if(Vampire.brumePower) {
				   getCommand("brume").setExecutor(new Brume());
			  }
			  Vampire.initVampire();
		  }
		  if(Werewolf.enabled) {
			  Bukkit.getPluginManager().registerEvents(new Werewolf(), this);
			  getCommand("transf").setExecutor(new Transf());
			  getCommand("untransf").setExecutor(new Untransf());
			  getCommand("werewolfstats").setExecutor(new WerewolfStats());
			  Werewolf.initWerewolf();
		  }
		  if(Fairy.enabled) {
			  Bukkit.getPluginManager().registerEvents(new Fairy(), this);
			  Fairy.initFairy();
		  }
		  if(Daemon.enabled) {
			  Bukkit.getPluginManager().registerEvents(new Daemon(), this);
			  if(Daemon.tpNether) {
				  getCommand("nether").setExecutor(new Nether());
			  }
			  if(WolvMC.canDis() || !Daemon.pigTransf) {
				  getCommand("pig").setExecutor(new Pig());
			  }
			  Daemon.initDaemon();
		  }
		  if(Angel.enabled) {
			  Bukkit.getPluginManager().registerEvents(new Angel(), this);
			  Angel.initAngel();
		  }
		  if(Elf.enabled) {
			  Bukkit.getPluginManager().registerEvents(new Elf(), this);
			  Elf.initElf();
		  }
		  if(Zorlim.enabled) {
			  Bukkit.getPluginManager().registerEvents(new Zorlim(), this);
			  Zorlim.initZorlim();
		  }
		  if(Orc.enabled) {
			  Bukkit.getPluginManager().registerEvents(new Orc(), this);
			  Orc.initOrc();
		  }
		  if(Draco.enabled) {
			  Bukkit.getPluginManager().registerEvents(new Draco(), this);
			  Bukkit.getPluginManager().registerEvents(new ArmorListener(new ArrayList<String>()), this);
			  getCommand("dracorage").setExecutor(new Dracorage());
			  Draco.initDraco();
		  }
		  getCommand("wolvmc").setExecutor(new Main());
		  getCommand("missions").setExecutor(new Missions());
		  getCommand("logo").setExecutor(new SexLogo());
		  Bukkit.getPluginManager().registerEvents(new SexLogo(), this);
		Bukkit.getLogger().fine("WolvMC Enabled!");
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			this.loadSave(p);
		}
	       BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	       scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
	            @Override
	            public void run() {
	            	WolvMC.reloadConfig(true);
	            }
	        }, 20L);
	       scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncRepeatingTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
	            @Override
	            public void run() {
	    			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
	    				Integer time = playtime.get(p.getName());
	    				if(time==null) {
	    					time = 0;
	    				}
	    				++time;
	    				playtime.put(p.getName(), time);
	    			}
	            }
	        }, 0L, 1200L);
		       scheduler = Bukkit.getServer().getScheduler();
		       scheduler.scheduleSyncRepeatingTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
		            @Override
		            public void run() {
		    			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
		    				saveData(p);
		    			}
		    			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-a");
		    			Date date = new Date();
		    			File backup = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/backups/" + dateFormat.format(date) + ".zip");
		    			if(!backup.exists()) {
		    				String saveDir = WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/saves/";
		    				ZipUsingJavaUtils.zipFiles(saveDir, backup.getAbsolutePath());
		    			}
		            }
		        }, 0L, 6000L);
		}
		
		@Override
		public void onDisable() {
			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
				WolvMC.saveData(p);
			}
			Bukkit.getLogger().fine("WolvMC Disabled!");
		}
	  
	  @EventHandler
	  public void onJoin(final PlayerJoinEvent e) {
		  this.loadSave(e.getPlayer());
		  if(WolvMC.getRace(e.getPlayer().getName()).equals("")) {
			  if(randomRace) {
				  Random r = new Random();
				  WolvMC.setRace(e.getPlayer(), defaultRaces.get(r.nextInt(defaultRaces.size())));
			  }
			  else if(menuRace) {
					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
			            @Override
			            public void run() {
			            	WolvMC.openChooseInventory(e.getPlayer(), false);
			            }
			        }, 1L);
			  }
		  }
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
	            @Override
	            public void run() {
	            	WolvMCInitEffectsEvent event = new WolvMCInitEffectsEvent(e.getPlayer());
	            	Bukkit.getServer().getPluginManager().callEvent(event);
	            }
	        }, 1L);
	  }
	  
	  @EventHandler
	  public void onCloseChooseInv(InventoryCloseEvent e) {
		  if(e.getInventory().getName().equals(ChatColor.DARK_BLUE + "Choose your Race!")) {
			  final Player p = (Player) e.getPlayer();
			  final Inventory inv = e.getInventory();
				BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
		            @Override
		            public void run() {
		            	if(WolvMC.getRace(p.getName()).equals("")) {
			            	p.openInventory(inv);
		            	}
		            }
		        }, 1L);
		  }
	  }
	  
	  @EventHandler
	  public void onCloseChangeInv(InventoryCloseEvent e) {
		  if(e.getInventory().getName().equals(ChatColor.DARK_BLUE + "Change your Race!")) {
			  if(changes.get(e.getPlayer().getName())==0 || !e.getPlayer().hasPermission("wolvmc.changes.inf")) {
				  return;
			  }
			  final Player p = (Player) e.getPlayer();
			  final Inventory inv = e.getInventory();
				BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
		            @Override
		            public void run() {
		            	if(WolvMC.getRace(p.getName()).equals("")) {
			            	p.openInventory(inv);
		            	}
		            }
		        }, 1L);
		  }
	  }
	  
	  @EventHandler
	  public void onInventoryChooseClick(InventoryClickEvent e) {
		  if(e.getInventory().getName().equals(ChatColor.DARK_BLUE + "Choose your Race!")) {
			  e.setCancelled(true);
			  ItemStack is = e.getInventory().getItem(e.getSlot());
			  if(is!=null) {
				  for (Entry<String, String> entry : prefixs.entrySet())
				  {
					  String value = ChatColor.translateAlternateColorCodes('&', entry.getValue());
					  if(value.equals(is.getItemMeta().getDisplayName())) {
						  WolvMC.setRace((Player) e.getWhoClicked(), entry.getKey());
						  e.getWhoClicked().closeInventory();
						  return;
					  }
				  }
			  }
		  }
	  }
	  
	  @EventHandler
	  public void onInventoryChangeClick(InventoryClickEvent e) {
		  if(e.getInventory().getName().equals(ChatColor.DARK_BLUE + "Change your Race!")) {
			  if(changes.get(e.getWhoClicked().getName())==0 || !e.getWhoClicked().hasPermission("wolvmc.changes.inf")) {
				  return;
			  }
			  e.setCancelled(true);
			  ItemStack is = e.getInventory().getItem(e.getSlot());
			  if(is!=null) {
				  for (Entry<String, String> entry : prefixs.entrySet())
				  {
					  String value = ChatColor.translateAlternateColorCodes('&', entry.getValue());
					  if(value.equals(is.getItemMeta().getDisplayName())) {
						  WolvMC.setRace((Player) e.getWhoClicked(), entry.getKey());
						  if(!e.getWhoClicked().hasPermission("wolvmc.changes.inf")) {
							  WolvMC.setChanges(e.getWhoClicked().getName(), WolvMC.changesLeft(e.getWhoClicked().getName()) - 1);
						  }
						  e.getWhoClicked().closeInventory();
						  return;
					  }
				  }
			  }
		  }
	  }
	  
	  @EventHandler
	  public void onRespawn(PlayerRespawnEvent e) {
		  final Player p = e.getPlayer();
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
	            @Override
	            public void run() {
	            	WolvMCInitEffectsEvent event = new WolvMCInitEffectsEvent(p);
	            	Bukkit.getServer().getPluginManager().callEvent(event);
	            }
	        }, 1L);
	  }
	  
	  @EventHandler
	  public void onLeave(PlayerQuitEvent e) {
		  WolvMC.saveData(e.getPlayer());
	  }
	  
	  @EventHandler
	  public void onMilkConsume(PlayerItemConsumeEvent e) {
		  if(e.getItem().getType()==Material.MILK_BUCKET) {
			  e.setCancelled(true);
			  e.getPlayer().sendMessage(milk);
		  }
	  }
	  
	  @EventHandler
	  public void onChat(AsyncPlayerChatEvent e) {
		  Player p = e.getPlayer();
		  String race = WolvMC.getRace(p.getName());
		  String logo = SexLogo.getPlayerLogo(p.getName());
		  if(logo==null) {
			  logo = "";
		  }
		  else {
			  logo = logo + " " + ChatColor.RESET;
		  }
		  if(!getCustomPrefix(p.getName()).equals("")) {
			  e.setFormat(ChatColor.translateAlternateColorCodes('&', logo + getCustomPrefix(p.getName())) + ChatColor.RESET + " " + p.getDisplayName()+ ChatColor.RESET + "> " + e.getMessage());
		  }
		  else {
			  if(!race.equals("")) {
				  String prefix = prefixs.get(race);
				  if(prefix!=null) {
					  e.setFormat(logo + prefix + ChatColor.RESET + " " + p.getDisplayName() + "> " + e.getMessage());
				  }
				  else {
					  e.setFormat(logo + ChatColor.BLACK + "???" + ChatColor.RESET + " " + p.getDisplayName() + "> " + e.getMessage());
				  }
			  }
			  else {
				  e.setFormat(logo + p.getDisplayName() + "> " + e.getMessage());
			  }
		  }
	  }
	  
	  public void loadSave(Player p) {
			File file = new File(this.getDataFolder() + "/saves/" + p.getUniqueId().toString() + ".yml");
			if(!file.exists()) {
				try {
					file.createNewFile();
					File customYml = new File(this.getDataFolder() + "/saves/" + p.getUniqueId().toString() + ".yml");
					FileConfiguration conf = YamlConfiguration.loadConfiguration(customYml);
					conf.createSection("data");
					return;
				} catch (IOException e1) {
					e1.printStackTrace();
					Bukkit.getLogger().severe("Cannot create save file for " + p.getName() + "!");
					return;
				}
			}
			File customYml = new File(this.getDataFolder() + "/saves/" + p.getUniqueId().toString() + ".yml");
			FileConfiguration conf = YamlConfiguration.loadConfiguration(customYml);
			String race = conf.getString("data.race");
			if(race != null && !race.isEmpty()) {
				races.put(p.getName(), race);
			}
			else {
				races.put(p.getName(), "");
			}
			if(!conf.isSet("data.time")) {
				playtime.put(p.getName(), 0);
			}
			else {
				playtime.put(p.getName(), conf.getInt("data.time"));
			}
			if(conf.isSet("data.custom-prefix")) {
				setCustomPrefix(p.getName(), conf.getString("data.custom-prefix"));
			}
			else {
				setCustomPrefix(p.getName(), "");
			}
			if(conf.isSet("data.changes-left")) {
				changes.put(p.getName(), conf.getInt("data.changes-left"));
			}
			else {
				changes.put(p.getName(), changesBase);
			}
			if(conf.isSet("data.logo")) {
				SexLogo.setPlayerLogo(p.getName(), conf.getString("data.logo"));
			}
			for (Map.Entry<String, Double> entry : missions.entrySet())
			{
				Double nb = conf.getDouble("data.missions." + entry.getKey());
				if(nb!=null) {
					pmissions.put(p.getName() + " " + entry.getKey(), nb);
				}
			}
	  }
	  
	  public static void saveData(Player p) {
			File customYml = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/saves/" + p.getUniqueId().toString() + ".yml");
			FileConfiguration conf = YamlConfiguration.loadConfiguration(customYml);
			ConfigurationSection section = conf.getConfigurationSection("data");
			if(!conf.contains("data")) {
				section = conf.createSection("data");
			}
			section.set("race", WolvMC.getRace(p.getName()));
			for (Map.Entry<String, Double> entry : missions.entrySet())
			{
				Double nb = pmissions.get(p.getName() + " " + entry.getKey());
				if(nb!=null) {
					section.set("missions." + entry.getKey(), nb);
				}
			}
			Integer time = playtime.get(p.getName());
			section.set("time", time);
			section.set("custom-prefix", getCustomPrefix(p.getName()));
			section.set("changes-left", changes.get(p.getName()));
			if(SexLogo.getPlayerLogo(p.getName())!=null) {
				section.set("logo", SexLogo.getPlayerLogo(p.getName()));
			}
			try {
				conf.save(customYml);
			} catch (IOException e) {
				e.printStackTrace();
			}
			WolvMCSaveEvent event = new WolvMCSaveEvent(p);
			Bukkit.getServer().getPluginManager().callEvent(event);
			HashMap<String, Object> map = event.getSaveList();
    		for (Entry<String, Object> entry : map.entrySet())
    		{
    			section.set(entry.getKey(), entry.getValue());
    		}
			try {
				conf.save(customYml);
			} catch (IOException e) {
				e.printStackTrace();
			}
	  }
	  
	  public static String getRace(String name) {
		  if(name==null) {
			  return "";
		  }
		  if(races.get(name)!=null) {
			  return races.get(name);
		  }
		  else {
			  return "";
		  }
	  }
	  
	  public static Double getPlayerMission(String mission, String name) {
		Double nb = pmissions.get(name + " " + mission);
		if(nb==null) {
			return (double) 0;
		}
		else {
			return nb;
		}
	  }
	  
	  public static void addMission(String mission, Double goal, String race, String descr, String finishmsg) {
		  descr = descr.replaceAll("%goal%", String.valueOf(goal.intValue()));
		  missions.put(mission, goal);
		  racemissions.put(mission, race);
		  descrmissions.put(mission, descr);
		  finishmsgmissions.put(mission, finishmsg);
	  }
	  
	  public static void addNbToMission(String mission, String name, Double nb) {
		  Double current = null;
		  if(pmissions.containsKey(name + " " + mission)) {
			  current = pmissions.get(name + " " + mission);
		  }
		  else {
			  current = 0.00;
		  }
		  pmissions.put(name + " " + mission, current + nb);
	  }
	  
	  public static boolean hasFinishMission(String mission, String name) {
		  Double goal = missions.get(mission);
		  Double current = pmissions.get(name + " " + mission);
		  if(current==null) {
			  current = (double) 0;
		  }
		  if(current >= goal) {
			  return true;
		  }
		  else {
			  return false;
		  }
	  }
	  
	 public static String getPrefix(String race) {
		String prefix = prefixs.get(race);
			if(prefix!=null) {
				return prefix;
			}
			else {
				return ChatColor.BLACK + "???";
			}
	  	}
	  
	  public static Integer getTime(String name) {
		Integer time = playtime.get(name);
		if(time==null) {
			return 0;
		}
		else {
			return time;
		}
	  }
	  
	  public static void setTime(String name, Integer time) {
		playtime.put(name, time);
	  }
	  
	  public static boolean raceExist(String race) {
		  if(prefixs.containsKey(race)) {
			  return true;
		  }
		  else {
			  return false;
		  }
	  }
	  
	  public static ArrayList<String> getAllRaces() {
		  	ArrayList<String> arr = new ArrayList<String>() ; 
  			for (Entry<String, String> entry : prefixs.entrySet())
  			{
  				arr.add(entry.getKey());
  			}  
  			return arr;
	  }
	  
	  public static String getCustomPrefix(String name) {
		  if(cprefix.containsKey(name)) {
			  return cprefix.get(name);
		  }
		  else {
			  return "";
		  }
	  }
	  
	  public static void setCustomPrefix(String name, String prefix) {
		  cprefix.put(name, prefix);
	  }
	  
	  public static void setChatPrefix(String race, String prefix) {
		  prefixs.put(race, prefix);
	  }
	  
	  public static void addRace(String race, String prefix, ItemStack item) {
		  prefixs.put(race, prefix);
		  ItemMeta meta = item.getItemMeta();
		  meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', prefix));
		  item.setItemMeta(meta);
		  items.put(race, item);
	  }
	  
	  public static void openChooseInventory(Player p, boolean b) {
		  Integer nb = 0;
		  ArrayList<String> list = new ArrayList<String>();
		  for(String r : defaultRaces) {
			  if(items.containsKey(r) && prefixs.containsKey(r)) {
				  ++nb;
				  list.add(r);
			  }
		  }
		  Integer i = 0;
		  Integer rows = nb / 9;
		  Inventory inv;
		  if(b) {
			  inv = Bukkit.createInventory(null, (rows + 1) * 9, ChatColor.DARK_BLUE + "Change your Race!");
		  }
		  else {
			  inv = Bukkit.createInventory(null, (rows + 1) * 9, ChatColor.DARK_BLUE + "Choose your Race!");
		  }
		  while(nb>=9) {
			  for(int x = 0; x < 9; ++x) {
				  inv.setItem(i, items.get(list.get(i)));
				  ++i;
			  }
			  nb -= 9;
		  }
		  Integer row = i / 9;
		  if(nb>=0) {
			  switch (nb) {
			  	case 1: 
			  		inv.setItem((row * 9) + 4, items.get(list.get(i)));
			  		break;
			  	case 2: 
			  		inv.setItem((row * 9) + 2, items.get(list.get(i)));
			  		inv.setItem((row * 9) + 6, items.get(list.get(i + 1)));
			  		break;
			  	case 3:
			  		inv.setItem((row * 9) + 3, items.get(list.get(i)));
			  		inv.setItem((row * 9) + 4, items.get(list.get(i + 1)));
			  		inv.setItem((row * 9) + 5, items.get(list.get(i + 2)));
			  		break;
			  	case 4:
			  		inv.setItem((row * 9) + 1, items.get(list.get(i)));
			  		inv.setItem((row * 9) + 3, items.get(list.get(i + 1)));
			  		inv.setItem((row * 9) + 5, items.get(list.get(i + 2)));
			  		inv.setItem((row * 9) + 7, items.get(list.get(i + 3)));
			  		break;
			  	case 5:
			  		inv.setItem((row * 9) + 2, items.get(list.get(i)));
			  		inv.setItem((row * 9) + 3, items.get(list.get(i + 1)));
			  		inv.setItem((row * 9) + 4, items.get(list.get(i + 2)));
			  		inv.setItem((row * 9) + 5, items.get(list.get(i + 3)));
			  		inv.setItem((row * 9) + 6, items.get(list.get(i + 4)));
			  		break;
			  	case 6:
			  		inv.setItem((row * 9) + 1, items.get(list.get(i)));
			  		inv.setItem((row * 9) + 2, items.get(list.get(i + 1)));
			  		inv.setItem((row * 9) + 3, items.get(list.get(i + 2)));
			  		inv.setItem((row * 9) + 4, items.get(list.get(i + 3)));
			  		inv.setItem((row * 9) + 5, items.get(list.get(i + 4)));
			  		inv.setItem((row * 9) + 6, items.get(list.get(i + 5)));
			  		break;
			  	case 7:
			  		inv.setItem((row * 9) + 1, items.get(list.get(i)));
			  		inv.setItem((row * 9) + 2, items.get(list.get(i + 1)));
			  		inv.setItem((row * 9) + 3, items.get(list.get(i + 2)));
			  		inv.setItem((row * 9) + 4, items.get(list.get(i + 3)));
			  		inv.setItem((row * 9) + 5, items.get(list.get(i + 4)));
			  		inv.setItem((row * 9) + 6, items.get(list.get(i + 5)));
			  		inv.setItem((row * 9) + 7, items.get(list.get(i + 6)));
			  		break;
			  	case 8:
			  		inv.setItem((row * 9) + 0, items.get(list.get(i)));
			  		inv.setItem((row * 9) + 1, items.get(list.get(i + 1)));
			  		inv.setItem((row * 9) + 2, items.get(list.get(i + 2)));
			  		inv.setItem((row * 9) + 3, items.get(list.get(i + 3)));
			  		inv.setItem((row * 9) + 5, items.get(list.get(i + 4)));
			  		inv.setItem((row * 9) + 6, items.get(list.get(i + 5)));
			  		inv.setItem((row * 9) + 7, items.get(list.get(i + 6)));
			  		inv.setItem((row * 9) + 8, items.get(list.get(i + 7)));
			  		break;
			  	default:
			  		break;
			  }
		  }
		  p.openInventory(inv);
	  }
	  
	  public static void setRace(Player p, String race) {
		  races.put(p.getName(), race);
		  p.sendMessage(becomeRace.replaceAll("%race%", WolvMC.getPrefix(race)));
		  if(WolvMC.trailPlug()) {
			  SuperTrailsAPI.setTrail(null, p);
		  }
		  if(!p.isOp()) {
			  p.setAllowFlight(false);
		  }
		  if(WolvMC.canDis()) {
			  DisguiseAPI.undisguiseToAll(p);
		  }
		  WolvMCInitEffectsEvent event = new WolvMCInitEffectsEvent(p);
		  Bukkit.getServer().getPluginManager().callEvent(event);
	  }
	  
	  public static Object getSave(String uuid, String data) {
			File customYml = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/saves/" + uuid + ".yml");
			FileConfiguration conf = YamlConfiguration.loadConfiguration(customYml);
			Object donnee = conf.get("data." + data);
			return donnee;
	  }
	  
	  public static void save(String uuid, String data, Object value) {
			File customYml = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/saves/" + uuid + ".yml");
			FileConfiguration conf = YamlConfiguration.loadConfiguration(customYml);
			ConfigurationSection section = conf.getConfigurationSection("data");
			section.set(data, value);
			try {
				conf.save(customYml);
			} catch (IOException e) {
				e.printStackTrace();
			}
	  }
	  
	  public static WorldGuardPlugin getWorldGuard() {
		    Plugin plugin = WolvMC.getPlugin(WolvMC.class).getServer().getPluginManager().getPlugin("WorldGuard");
		    if (plugin == null) {
		        return null;
		    }
		 
		    return (WorldGuardPlugin) plugin;
		}
	  
	  public static boolean canDis() {
		    Plugin plugin = WolvMC.getPlugin(WolvMC.class).getServer().getPluginManager().getPlugin("LibsDisguises");
		    if (plugin == null) {
		        return false;
		    }
		    return true;
	  }
	  
	  public static boolean trailPlug() {
		    Plugin plugin = WolvMC.getPlugin(WolvMC.class).getServer().getPluginManager().getPlugin("SuperTrails");
		    if (plugin == null) {
		        return false;
		    }
		    return true;
	  }
	  
	  public static boolean canUsePowerBlock(Location loc)
	  {
	      WorldGuardPlugin guard = getWorldGuard();
	      if(guard==null) {
	    	  return true;
	      }
	      com.sk89q.worldedit.Vector v = BukkitUtil.toVector(loc);
	      RegionManager manager = guard.getRegionManager(loc.getWorld());
	      ApplicableRegionSet set = manager.getApplicableRegions(v);
	      Object[] list = set.getRegions().toArray();
	      for(Object r : list) {
	    	  if(!regionsblock.contains(r.toString())) {
	    		  return false;
	    	  }
	      }
	      return true;
	  }
	  
	  public static boolean canUsePowerSafe(Location loc)
	  {
	      WorldGuardPlugin guard = getWorldGuard();
	      if(guard==null) {
	    	  return true;
	      }
	      com.sk89q.worldedit.Vector v = BukkitUtil.toVector(loc);
	      RegionManager manager = guard.getRegionManager(loc.getWorld());
	      ApplicableRegionSet set = manager.getApplicableRegions(v);
	      Object[] list = set.getRegions().toArray();
	      for(Object r : list) {
	    	  if(!regionssafe.contains(r.toString())) {
	    		  return false;
	    	  }
	      }
	      return true;
	  }
	  
	  public static boolean canUsePowerBlock(Location loc, Player p)
	  {
	      WorldGuardPlugin guard = getWorldGuard();
	      if(guard==null) {
	    	  return true;
	      }
	      com.sk89q.worldedit.Vector v = BukkitUtil.toVector(loc);
	      RegionManager manager = guard.getRegionManager(loc.getWorld());
	      ApplicableRegionSet set = manager.getApplicableRegions(v);
	      Object[] list = set.getRegions().toArray();
	      for(Object r : list) {
	    	  if(!regionsblock.contains(r.toString())) {
	    		  p.sendMessage(power);
	    		  return false;
	    	  }
	      }
	      return true;
	  }
	  
	  public static Integer changesLeft(String name) {
		  return WolvMC.changes.get(name);
	  }
	  
	  public static void setChanges(String name, Integer changes) {
		  WolvMC.changes.put(name, changes);
	  }
	  
	  public static boolean canUsePowerSafe(Location loc, Player p)
	  {
	      WorldGuardPlugin guard = getWorldGuard();
	      if(guard==null) {
	    	  return true;
	      }
	      com.sk89q.worldedit.Vector v = BukkitUtil.toVector(loc);
	      RegionManager manager = guard.getRegionManager(loc.getWorld());
	      ApplicableRegionSet set = manager.getApplicableRegions(v);
	      Object[] list = set.getRegions().toArray();
	      for(Object r : list) {
	    	  if(!regionssafe.contains(r.toString())) {
	    		  p.sendMessage(power);
	    		  return false;
	    	  }
	      }
	      return true;
	  }
	  
	  public static ArrayList<String> getMissionsForRace(String race) {
		  ArrayList<String> arr = new ArrayList<String>();
		for (Map.Entry<String, Double> entry : missions.entrySet()) {
			if(racemissions.get(entry.getKey()).equals(race)) {
				arr.add(entry.getKey());
			}
		}
		return arr;
	  }
	  
	  public static Double getMissionGoal(String mission) {
		  return missions.get(mission);
	  }
	  
	  public static void setMissionGoal(String mission, Double goal) {
		  missions.put(mission, goal);
	  }
	  
	  public static String getMissionDescr(String mission) {
		  return descrmissions.get(mission);
	  }
	  
	  public static void setMissionDescr(String mission, String descr) {
		  descrmissions.put(mission, descr);
	  }
	  
	  public static String getMissionFinishMsg(String mission) {
		  return finishmsgmissions.get(mission);
	  }
	  
	  public static void setMissionFinishMsg(String mission, String msg) {
		  finishmsgmissions.put(mission, msg);
	  }
	  
	  public static String getMissionRace(String mission) {
		  return racemissions.get(mission);
	  }
	  
	  public static String msgCooldown(Integer i) {
		  if(i>1) {
			  String s = WolvMC.cooldown;
			  s = s.replaceAll("%secs%", i.toString());
			  return s;
		  }
		  else {
			  return WolvMC.cooldown1sec;
		  }
	  }
	  
	  public static void reloadConfig(boolean b) {
			File file = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/language/");
			if(!file.exists()) {
				file.mkdir();
				  InputStream stream = WolvMC.class.getClassLoader().getResourceAsStream("fr/nashoba24/wolvmc/language/french.yml");
				  FileOutputStream fos = null;
				  try {
				      fos = new FileOutputStream(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/language/french.yml");
				      byte[] buf = new byte[2048];
				      int r = stream.read(buf);
				      while(r != -1) {
				          fos.write(buf, 0, r);
				          r = stream.read(buf);
				      }
				      fos.close();
				  } catch (IOException e) {
					e.printStackTrace();
				  }
				  stream = WolvMC.class.getClassLoader().getResourceAsStream("fr/nashoba24/wolvmc/language/english.yml");
				  fos = null;
				  try {
				      fos = new FileOutputStream(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/language/english.yml");
				      byte[] buf = new byte[2048];
				      int r = stream.read(buf);
				      while(r != -1) {
				          fos.write(buf, 0, r);
				          r = stream.read(buf);
				      }
				      fos.close();
				  } catch (IOException e) {
					e.printStackTrace();
				  }
			}
		      WolvMCReloadEvent event = new WolvMCReloadEvent();
		      Bukkit.getServer().getPluginManager().callEvent(event);
			  WolvMC.getPlugin(WolvMC.class).saveDefaultConfig();
			  File configFile = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/config.yml");
			  FileConfiguration conf = YamlConfiguration.loadConfiguration(configFile);
			  if(conf.isSet("whitelist-power-block")) {
				  List<?> regions = (List<?>) conf.get("whitelist-power-block");
				  for(Object r : regions) {
					  regionsblock.add(r.toString());
				  }
			  }
			  if(conf.isSet("whitelist-safe-power")) {
				  List<?> regions = (List<?>) conf.get("whitelist-safe-power");
				  for(Object r : regions) {
					  regionssafe.add(r.toString());
				  }
			  }
			  if(conf.isSet("random-race-first-join")) {
				  WolvMC.randomRace = conf.getBoolean("random-race-first-join");
			  }
			  if(conf.isSet("menu-race-first-join")) {
				  WolvMC.menuRace = conf.getBoolean("menu-race-first-join");
			  }
			  if(conf.isSet("default-races")) {
				  @SuppressWarnings("unchecked")
				List<String> r = (List<String>) conf.get("default-races");
				  WolvMC.defaultRaces = r;
			  }
			  if(conf.isSet("changes-default")) {
				  WolvMC.changesBase = conf.getInt("changes-default");
			  }
			  file = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/language/" + conf.getString("language") + ".yml");
			  if(!file.exists()) {
				  InputStream stream = WolvMC.class.getClassLoader().getResourceAsStream("fr/nashoba24/wolvmc/language/" + conf.getString("language") + ".yml");
				  if(stream!=null) {
					  FileOutputStream fos = null;
					  try {
					      fos = new FileOutputStream(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/language/" + conf.getString("language") + ".yml");
					      byte[] buf = new byte[2048];
					      int r = stream.read(buf);
					      while(r != -1) {
					          fos.write(buf, 0, r);
					          r = stream.read(buf);
					      }
					      fos.close();
					  } catch (IOException e) {
						e.printStackTrace();
					  }
				  }
			  }
		FileConfiguration config;
		ConfigurationSection section = null;
		if(!file.exists()) {
			WolvMC.getPlugin(WolvMC.class).getLogger().severe("Language in the config.yml doesn't exist! Default Messages are used!");
			return;
		}
		else {
			config = YamlConfiguration.loadConfiguration(file);
			section = config.getConfigurationSection("messages");
		}
		if(section==null) {
			config.createSection("messages");
			section = config.getConfigurationSection("messages");
		}
		if(section!=null) {
			if(section.isSet("milk-disable")) {
				WolvMC.milk = ChatColor.translateAlternateColorCodes('&', section.getString("milk-disable"));
			}
			else {
				section.set("milk-disable", Colors.get(WolvMC.milk));
			}
			if(section.isSet("power")) {
				WolvMC.power = ChatColor.translateAlternateColorCodes('&', section.getString("power"));
			}
			else {
				section.set("power", Colors.get(WolvMC.power));
			}
			if(section.isSet("bat-transf")) {
				Vampire.batTransf = ChatColor.translateAlternateColorCodes('&', section.getString("bat-transf"));
			}
			else {
				section.set("bat-transf", Colors.get(Vampire.batTransf));
			}
			if(section.isSet("30secs-left-bat")) {
				Vampire.thirtySecsLeft = ChatColor.translateAlternateColorCodes('&', section.getString("30secs-left-bat"));
			}
			else {
				section.set("30secs-left-bat", Colors.get(Vampire.thirtySecsLeft));
			}
			if(section.isSet("no-longer-bat")) {
				Vampire.noLongerBat = ChatColor.translateAlternateColorCodes('&', section.getString("no-longer-bat"));
			}
			else {
				section.set("no-longer-bat", Colors.get(Vampire.noLongerBat));
			}
			if(section.isSet("can-transf-again-bat")) {
				Vampire.canTransfAgain = ChatColor.translateAlternateColorCodes('&', section.getString("can-transf-again-bat"));
			}
			else {
				section.set("can-transf-again-bat", Colors.get(Vampire.canTransfAgain));
			}
			if(section.isSet("cooldown-1sec")) {
				WolvMC.cooldown1sec = ChatColor.translateAlternateColorCodes('&', section.getString("cooldown-1sec"));
			}
			else {
				section.set("cooldown-1-sec", Colors.get(WolvMC.cooldown1sec));
			}
			if(section.isSet("cooldown")) {
				WolvMC.cooldown = ChatColor.translateAlternateColorCodes('&', section.getString("cooldown"));
			}
			else {
				section.set("cooldown", Colors.get(WolvMC.cooldown));
			}
			if(section.isSet("empty-slot")) {
				Vampire.emptySlot = ChatColor.translateAlternateColorCodes('&', section.getString("empty-slot"));
			}
			else {
				section.set("empty-slot", Colors.get(Vampire.emptySlot));
			}
			if(section.isSet("too-many-bottles")) {
				Vampire.tooManyBottle = ChatColor.translateAlternateColorCodes('&', section.getString("too-many-bottles"));
			}
			else {
				section.set("too-many-bottles", Colors.get(Vampire.tooManyBottle));
			}
			if(section.isSet("not-enough-blood")) {
				Vampire.notEnoughBlood = ChatColor.translateAlternateColorCodes('&', section.getString("not-enough-blood"));
			}
			else {
				section.set("not-enough-blood", Colors.get(Vampire.notEnoughBlood));
			}
			if(section.isSet("blood-bottle")) {
				Vampire.bloodBottle = ChatColor.translateAlternateColorCodes('&', section.getString("blood-bottle"));
			}
			else {
				section.set("blood-bottle", Colors.get(Vampire.bloodBottle));
			}
			if(section.isSet("not-hungry")) {
				Vampire.notHungry = ChatColor.translateAlternateColorCodes('&', section.getString("not-hungry"));
			}
			else {
				section.set("not-hungry", Colors.get(Vampire.notHungry));
			}
			if(section.isSet("blood-in-vessels")) {
				Vampire.bloodVessel = ChatColor.translateAlternateColorCodes('&', section.getString("blood-in-vessels"));
			}
			else {
				section.set("blood-in-vessels", Colors.get(Vampire.bloodVessel));
			}
			if(section.isSet("blood-suck")) {
				Vampire.bloodSucked = ChatColor.translateAlternateColorCodes('&', section.getString("blood-suck"));
			}
			else {
				section.set("blood-suck", Colors.get(Vampire.bloodSucked));
			}
			if(section.isSet("cant-sleep-vampire")) {
				Vampire.cantSleep = ChatColor.translateAlternateColorCodes('&', section.getString("cant-sleep-vampire"));
			}
			else {
				section.set("cant-sleep-vampire", Colors.get(Vampire.cantSleep));
			}
			if(section.isSet("vampire-become-human")) {
				VampireCMD.becomeHuman = ChatColor.translateAlternateColorCodes('&', section.getString("vampire-become-human"));
			}
			else {
				section.set("vampire-become-human", Colors.get(VampireCMD.becomeHuman));
			}
			if(section.isSet("vampire-become-vampire")) {
				VampireCMD.becomeVampire = ChatColor.translateAlternateColorCodes('&', section.getString("vampire-become-vampire"));
			}
			else {
				section.set("vampire-become-vampire", Colors.get(VampireCMD.becomeVampire));
			}
			if(section.isSet("not-a-vampire")) {
				VampireCMD.noVampire = ChatColor.translateAlternateColorCodes('&', section.getString("not-a-vampire"));
			}
			else {
				section.set("not-a-vampire", Colors.get(VampireCMD.noVampire));
			}
			if(section.isSet("howl")) {
				Werewolf.howlMSG = ChatColor.translateAlternateColorCodes('&', section.getString("howl"));
			}
			else {
				section.set("howl", Colors.get(Werewolf.howlMSG));
			}
			if(section.isSet("cant-sleep-werewolf")) {
				Werewolf.cantSleep = ChatColor.translateAlternateColorCodes('&', section.getString("cant-sleep-werewolf"));
			}
			else {
				section.set("cant-sleep-werewolf", Colors.get(Werewolf.cantSleep));
			}
			if(section.isSet("transf")) {
				Werewolf.transfMSG = ChatColor.translateAlternateColorCodes('&', section.getString("transf"));
			}
			else {
				section.set("transf", Colors.get(Werewolf.transfMSG));
			}
			if(section.isSet("untransf")) {
				Werewolf.untransfMSG = ChatColor.translateAlternateColorCodes('&', section.getString("untransf"));
			}
			else {
				section.set("untransf", Colors.get(Werewolf.untransfMSG));
			}
			if(section.isSet("try-to-transf")) {
				Transf.tryTransf = ChatColor.translateAlternateColorCodes('&', section.getString("try-to-transf"));
			}
			else {
				section.set("try-to-transf", Colors.get(Transf.tryTransf));
			}
			if(section.isSet("not-a-werewolf")) {
				Untransf.notWW = ChatColor.translateAlternateColorCodes('&', section.getString("not-a-werewolf"));
			}
			else {
				section.set("not-a-werewolf", Colors.get(Untransf.notWW));
			}
			if(section.isSet("transf-time")) {
				WerewolfStats.time = ChatColor.translateAlternateColorCodes('&', section.getString("transf-time"));
			}
			else {
				section.set("transf-time", Colors.get(WerewolfStats.time));
			}
			if(section.isSet("transf-any-time")) {
				WerewolfStats.allTime = ChatColor.translateAlternateColorCodes('&', section.getString("transf-any-time"));
			}
			else {
				section.set("transf-any-time", Colors.get(WerewolfStats.allTime));
			}
			if(section.isSet("werewolf-speed-strength")) {
				WerewolfStats.effects = ChatColor.translateAlternateColorCodes('&', section.getString("werewolf-speed-strength"));
			}
			else {
				section.set("werewolf-speed-strength", Colors.get(WerewolfStats.effects));
			}
			if(section.isSet("no-tool-pvp")) {
				WerewolfStats.noTool = ChatColor.translateAlternateColorCodes('&', section.getString("no-tool-pvp"));
			}
			else {
				section.set("no-tool-pvp", Colors.get(WerewolfStats.noTool));
			}
			if(section.isSet("tool-pvp-g-w")) {
				WerewolfStats.ToolGW = ChatColor.translateAlternateColorCodes('&', section.getString("tool-pvp-g-w"));
			}
			else {
				section.set("tool-pvp-g-w", Colors.get(WerewolfStats.ToolGW));
			}
			if(section.isSet("tool-pvp-g-w-s")) {
				WerewolfStats.ToolGWS = ChatColor.translateAlternateColorCodes('&', section.getString("tool-pvp-g-w-s"));
			}
			else {
				section.set("tool-pvp-g-w-s", Colors.get(WerewolfStats.ToolGWS));
			}
			if(section.isSet("tool-pvp-g-w-s-i")) {
				WerewolfStats.ToolGWSI = ChatColor.translateAlternateColorCodes('&', section.getString("tool-pvp-g-w-s-i"));
			}
			else {
				section.set("tool-pvp-g-w-s-i", Colors.get(WerewolfStats.ToolGWSI));
			}
			if(section.isSet("tool-pvp-all")) {
				WerewolfStats.allTools = ChatColor.translateAlternateColorCodes('&', section.getString("tool-pvp-all"));
			}
			else {
				section.set("tool-pvp-all", Colors.get(WerewolfStats.allTools));
			}
			if(section.isSet("invi-turn")) {
				Fairy.inviMSG = ChatColor.translateAlternateColorCodes('&', section.getString("invi-turn"));
			}
			else {
				section.set("invi-turn", Colors.get(Fairy.inviMSG));
			}
			if(section.isSet("invi-again")) {
				Fairy.inviAgain = ChatColor.translateAlternateColorCodes('&', section.getString("invi-again"));
			}
			else {
				section.set("invi-again", Colors.get(Fairy.inviAgain));
			}
			if(section.isSet("blind-msg")) {
				Fairy.blindMSG = ChatColor.translateAlternateColorCodes('&', section.getString("blind-msg"));
			}
			else {
				section.set("blind-msg", Colors.get(Fairy.blindMSG));
			}
			if(section.isSet("heal-instinct")) {
				Fairy.instinct = ChatColor.translateAlternateColorCodes('&', section.getString("heal-instinct"));
			}
			else {
				section.set("heal-instinct", Colors.get(Fairy.instinct));
			}
			if(section.isSet("pig-normal")) {
				Pig.normal = ChatColor.translateAlternateColorCodes('&', section.getString("pig-normal"));
			}
			else {
				section.set("pig-normal", Colors.get(Pig.normal));
			}
			if(section.isSet("pig-zpig")) {
				Pig.zpig = ChatColor.translateAlternateColorCodes('&', section.getString("pig-zpig"));
			}
			else {
				section.set("pig-zpig", Colors.get(Pig.zpig));
			}
			if(section.isSet("pig-pig")) {
				Pig.pig = ChatColor.translateAlternateColorCodes('&', section.getString("pig-pig"));
			}
			else {
				section.set("pig-pig", Colors.get(Pig.pig));
			}
			if(section.isSet("already-in-nether")) {
				Nether.already = ChatColor.translateAlternateColorCodes('&', section.getString("already-in-nether"));
			}
			else {
				section.set("already-in-nether", Colors.get(Nether.already));
			}
			if(section.isSet("healed-someone")) {
				Angel.healedSO = ChatColor.translateAlternateColorCodes('&', section.getString("healed-someone"));
			}
			else {
				section.set("healed-someone", Colors.get(Angel.healedSO));
			}
			if(section.isSet("healed-by-angel")) {
				Angel.healAngel = ChatColor.translateAlternateColorCodes('&', section.getString("healed-by-angel"));
			}
			else {
				section.set("healed-by-angel", Colors.get(Angel.healAngel));
			}
			if(section.isSet("death-msg-nether")) {
				Angel.netherMSG = ChatColor.translateAlternateColorCodes('&', section.getString("death-msg-nether"));
			}
			else {
				section.set("death-msg-nether", Colors.get(Angel.netherMSG));
			}
			if(section.isSet("missions-header")) {
				Missions.header = ChatColor.translateAlternateColorCodes('&', section.getString("missions-header"));
			}
			else {
				section.set("missions-header", Colors.get(Missions.header));
			}
			if(section.isSet("missions-footer")) {
				Missions.footer = ChatColor.translateAlternateColorCodes('&', section.getString("missions-footer"));
			}
			else {
				section.set("missions-footer", Colors.get(Missions.footer));
			}
			if(section.isSet("setrace-new-race")) {
				Main.newRaceMSG = ChatColor.translateAlternateColorCodes('&', section.getString("setrace-new-race"));
			}
			else {
				section.set("setrace-new-race", Colors.get(Main.newRaceMSG));
			}
			if(section.isSet("setrace-save-modified")) {
				Main.saveModified = ChatColor.translateAlternateColorCodes('&', section.getString("setrace-save-modified"));
			}
			else {
				section.set("setrace-save-modified", Colors.get(Main.saveModified));
			}
			if(section.isSet("setrace-never-joined")) {
				Main.neverJoined = ChatColor.translateAlternateColorCodes('&', section.getString("setrace-never-joined"));
			}
			else {
				section.set("setrace-never-joined", Colors.get(Main.neverJoined));
			}
			if(section.isSet("no-permission")) {
				WolvMC.noPerm = ChatColor.translateAlternateColorCodes('&', section.getString("no-permission"));
			}
			else {
				section.set("no-permission", Colors.get(WolvMC.noPerm));
			}
			if(section.isSet("wolvmc-info")) {
				Main.infos = ChatColor.translateAlternateColorCodes('&', section.getString("wolvmc-info"));
			}
			else {
				section.set("wolvmc-info", Colors.get(Main.infos));
			}
			if(section.isSet("wolvmc-info-mission")) {
				Main.missions = ChatColor.translateAlternateColorCodes('&', section.getString("wolvmc-info-mission"));
			}
			else {
				section.set("wolvmc-info-mission", Colors.get(Main.missions));
			}
			if(section.isSet("wolvmc-setprefix")) {
				Main.prefixMSG = ChatColor.translateAlternateColorCodes('&', section.getString("wolvmc-setprefix"));
			}
			else {
				section.set("wolvmc-setprefix", Colors.get(Main.prefixMSG));
			}
			if(section.isSet("wolvmc-resetprefix")) {
				Main.prefixReset = ChatColor.translateAlternateColorCodes('&', section.getString("wolvmc-resetprefix"));
			}
			else {
				section.set("wolvmc-resetprefix", Colors.get(Main.prefixReset));
			}
			if(section.isSet("change-race")) {
				WolvMC.becomeRace = ChatColor.translateAlternateColorCodes('&', section.getString("change-race"));
			}
			else {
				section.set("change-race", Colors.get(WolvMC.becomeRace));
			}
			if(section.isSet("race-doesnt-exist")) {
				Main.notExist = ChatColor.translateAlternateColorCodes('&', section.getString("race-doesnt-exist"));
			}
			else {
				section.set("race-doesnt-exist", Colors.get(Main.notExist));
			}
			if(section.isSet("wolvmc-no-change-left")) {
				Main.noChangeLeft = ChatColor.translateAlternateColorCodes('&', section.getString("wolvmc-no-change-left"));
			}
			else {
				section.set("wolvmc-no-change-left", Colors.get(Main.noChangeLeft));
			}
		}
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		config = YamlConfiguration.loadConfiguration(file); //TODO arranger sections
		section = config.getConfigurationSection("missions");
		final ConfigurationSection sect = section;
		section = config.getConfigurationSection("prefixs");
		final ConfigurationSection sect2 = section;
		if(section!=null) {
		       BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		       scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
		            @Override
		            public void run() {
		        		for (Entry<String, Double> entry : missions.entrySet())
		        		{
		        			if(sect.isSet(entry.getKey().replaceAll("/.", "_") + "-descr")) {
		        				descrmissions.put(entry.getKey(), sect.getString(entry.getKey().replaceAll("/.", "_") + "-descr").replaceAll("%goal%", "" + entry.getValue().intValue()));
		        			}
		        			if(sect.isSet(entry.getKey().replaceAll("/.", "_") + "-finish")) {
		        				finishmsgmissions.put(entry.getKey(), sect.getString(entry.getKey().replaceAll("/.", "_") + "-finish").replaceAll("%goal%", "" + entry.getValue().intValue()));
		        			}
		        		}
		        		for(String key : sect2.getKeys(false)) {
		        				prefixs.put(key, ChatColor.translateAlternateColorCodes('&', sect2.getString(key)));
		        				ItemStack is = items.get(key);
		        				ItemMeta meta = is.getItemMeta();
		        				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', sect2.getString(key)));
		        				is.setItemMeta(meta);
		        				items.put(key, is);
		        		}
		        		
		            }
		        }, 100L);
		}
		file = new File(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/missions.yml");
		if(!file.exists()) {
			  InputStream stream = WolvMC.class.getClassLoader().getResourceAsStream("fr/nashoba24/wolvmc/config/missions.yml");
			  FileOutputStream fos = null;
			  try {
			      fos = new FileOutputStream(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/missions.yml");
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
		section = YamlConfiguration.loadConfiguration(file);
		final ConfigurationSection sect3 = section;
		if(section!=null) {
		       BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		       scheduler.scheduleSyncDelayedTask(WolvMC.getPlugin(WolvMC.class), new Runnable() {
		            @Override
		            public void run() {
		        		for (Entry<String, Double> entry : missions.entrySet())
		        		{
		        			if(sect3.isSet(entry.getKey().replaceAll("/.", "_"))) {
		        				missions.put(entry.getKey(), sect3.getDouble(entry.getKey().replaceAll("/.", "_")));
		        			}
		        		}
		        		
		            }
		        }, 100L);
		}
	  }
	  
}
