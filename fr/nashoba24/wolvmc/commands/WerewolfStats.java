package fr.nashoba24.wolvmc.commands;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.races.Werewolf;

public class WerewolfStats implements CommandExecutor {
	
	public static String time = ChatColor.GREEN + "Transformation allowed from %hours-1%:%minutes-1%%AM-PM-1% to %hours-2%:%minutes-2%%AM-PM-2%";
	public static String allTime = ChatColor.GREEN + "Transformation allowed at any time";
	public static String effects = ChatColor.GREEN + "Speed %speed-rn% | Strength %strength-rn%";
	public static String noTool = ChatColor.GREEN + "No tools for PvP";
	public static String ToolGW = ChatColor.GREEN + "Golden and wooden tools for PvP";
	public static String ToolGWS = ChatColor.GREEN + "Golden, wooden and stone tools for PvP";
	public static String ToolGWSI = ChatColor.GREEN + "Golden, wooden, stone and iron tools for PvP";
	public static String allTools = ChatColor.GREEN + "Every tools for PvP";
	public static Double diviser = 5.0;
	public static HashMap<String, Integer> levels = new HashMap<String, Integer>();
	public static HashMap<String, Integer> speed = new HashMap<String, Integer>();
	public static HashMap<String, Integer> strength = new HashMap<String, Integer>();
	public static HashMap<String, Integer> tools = new HashMap<String, Integer>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(WolvMC.getRace(p.getName()).equals("werewolf")) {
				Integer tot = Werewolf.getKills(p.getName());
				String ss = effects;
				String lvl = getLevelForKills(tot);
				Integer speedEff = speed.get(lvl);
				Integer strengthEff = strength.get(lvl);
				Integer toolsAllowed = tools.get(lvl);
				ss = ss.replaceAll("%speed-rn%", RomanNumerals(speedEff)).replaceAll("%strength-rn%", RomanNumerals(strengthEff)).replaceAll("%speed%", speedEff.toString()).replaceAll("%strength%", strengthEff.toString());
				if(toolsAllowed==0) {
					p.sendMessage(allTools);
				}
				else if(toolsAllowed==1) {
					p.sendMessage(ToolGWSI);
				}
				else if(toolsAllowed==2) {
					p.sendMessage(ToolGWS);
				}
				else if(toolsAllowed==3) {
					p.sendMessage(ToolGW);
				}
				else if(toolsAllowed==4) {
					p.sendMessage(noTool);
				}
				p.sendMessage(ss);
				if(tot==0) {
					tot = 1;
				}
				double transf = (WolvMC.getTime(p.getName()) / tot) / diviser;
				String msg = time;
				if(transf>140) {
					msg = allTime;
				}
				else if(transf>130) {
					msg = msg.replaceAll("%hours-1%", "7").replaceAll("%minutes-1%", "00").replaceAll("%AM-PM-1%", "AM").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00").replaceAll("%AM-PM-2%", "AM");
				}
				else if(transf>120) {
					msg = msg.replaceAll("%hours-1%", "8").replaceAll("%minutes-1%", "00").replaceAll("%AM-PM-1%", "AM").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00").replaceAll("%AM-PM-2%", "AM");
				}
				else if(transf>110) {
					msg = msg.replaceAll("%hours-1%", "9").replaceAll("%minutes-1%", "00").replaceAll("%AM-PM-1%", "AM").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00").replaceAll("%AM-PM-2%", "AM");
				}
				else if(transf>100) {
					msg = msg.replaceAll("%hours-1%", "10").replaceAll("%minutes-1%", "00").replaceAll("%AM-PM-1%", "AM").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00").replaceAll("%AM-PM-2%", "AM");
				}
				else if(transf>90) {
					msg = msg.replaceAll("%hours-1%", "11").replaceAll("%minutes-1%", "00").replaceAll("%AM-PM-1%", "AM").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00").replaceAll("%AM-PM-2%", "AM");
				}
				else if(transf>80) {
					msg = msg.replaceAll("%hours-1%", "12").replaceAll("%minutes-1%", "00").replaceAll("%AM-PM-1%", "AM").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00").replaceAll("%AM-PM-2%", "AM");
				}
				else if(transf>70) {
					if(msg.contains("%AM-PM-1%")) {
						msg = msg.replaceAll("%hours-1%", "1").replaceAll("%minutes-1%", "00").replaceAll("%AM-PM-1%", "PM").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00").replaceAll("%AM-PM-2%", "AM");
					}
					else {
						msg = msg.replaceAll("%hours-1%", "13").replaceAll("%minutes-1%", "00").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00");
					}
				}
				else if(transf>60) {
					if(msg.contains("%AM-PM-1%")) {
						msg = msg.replaceAll("%hours-1%", "2").replaceAll("%minutes-1%", "00").replaceAll("%AM-PM-1%", "PM").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00").replaceAll("%AM-PM-2%", "AM");
					}
					else {
						msg = msg.replaceAll("%hours-1%", "14").replaceAll("%minutes-1%", "00").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00");
					}
				}
				else if(transf>50) {
					if(msg.contains("%AM-PM-1%")) {
						msg = msg.replaceAll("%hours-1%", "3").replaceAll("%minutes-1%", "00").replaceAll("%AM-PM-1%", "PM").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00").replaceAll("%AM-PM-2%", "AM");
					}
					else {
						msg = msg.replaceAll("%hours-1%", "15").replaceAll("%minutes-1%", "00").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00");
					}
				}
				else if(transf>40) {
					if(msg.contains("%AM-PM-1%")) {
						msg = msg.replaceAll("%hours-1%", "4").replaceAll("%minutes-1%", "00").replaceAll("%AM-PM-1%", "PM").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00").replaceAll("%AM-PM-2%", "AM");
					}
					else {
						msg = msg.replaceAll("%hours-1%", "16").replaceAll("%minutes-1%", "00").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00");
					}
				}
				else if(transf>30) {
					if(msg.contains("%AM-PM-1%")) {
						msg = msg.replaceAll("%hours-1%", "5").replaceAll("%minutes-1%", "00").replaceAll("%AM-PM-1%", "PM").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00").replaceAll("%AM-PM-2%", "AM");
					}
					else {
						msg = msg.replaceAll("%hours-1%", "17").replaceAll("%minutes-1%", "00").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00");
					}
				}
				else if(transf>20) {
					if(msg.contains("%AM-PM-1%")) {
						msg = msg.replaceAll("%hours-1%", "6").replaceAll("%minutes-1%", "00").replaceAll("%AM-PM-1%", "PM").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00").replaceAll("%AM-PM-2%", "AM");
					}
					else {
						msg = msg.replaceAll("%hours-1%", "18").replaceAll("%minutes-1%", "00").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00");
					}
				}
				else if(transf>10) {
					if(msg.contains("%AM-PM-1%")) {
						msg = msg.replaceAll("%hours-1%", "7").replaceAll("%minutes-1%", "00").replaceAll("%AM-PM-1%", "PM").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00").replaceAll("%AM-PM-2%", "AM");
					}
					else {
						msg = msg.replaceAll("%hours-1%", "19").replaceAll("%minutes-1%", "00").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00");
					}
				}
				else {
					if(msg.contains("%AM-PM-1%")) {
						msg = msg.replaceAll("%hours-1%", "8").replaceAll("%minutes-1%", "00").replaceAll("%AM-PM-1%", "PM").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00").replaceAll("%AM-PM-2%", "AM");
					}
					else {
						msg = msg.replaceAll("%hours-1%", "20").replaceAll("%minutes-1%", "00").replaceAll("%hours-2%", "6").replaceAll("%minutes-2%", "00");
					}
				}
				p.sendMessage(msg);
				return true;
			}
		}
		return false;
	}
	
	public static String getLevelForKills(Integer i) {
		String lvl = "1";
		for (Entry<String, Integer> entry : levels.entrySet())
		{
			if(levels.get(lvl)<entry.getValue()) {
				lvl = entry.getKey();
			}
		}
		return lvl;
	}
	
	  static String RomanNumerals(int Int) {
		    LinkedHashMap<String, Integer> roman_numerals = new LinkedHashMap<String, Integer>();
		    roman_numerals.put("M", 1000);
		    roman_numerals.put("CM", 900);
		    roman_numerals.put("D", 500);
		    roman_numerals.put("CD", 400);
		    roman_numerals.put("C", 100);
		    roman_numerals.put("XC", 90);
		    roman_numerals.put("L", 50);
		    roman_numerals.put("XL", 40);
		    roman_numerals.put("X", 10);
		    roman_numerals.put("IX", 9);
		    roman_numerals.put("V", 5);
		    roman_numerals.put("IV", 4);
		    roman_numerals.put("I", 1);
		    String res = "";
		    for(Map.Entry<String, Integer> entry : roman_numerals.entrySet()){
		      int matches = Int/entry.getValue();
		      res += repeat(entry.getKey(), matches);
		      Int = Int % entry.getValue();
		    }
		    return res;
		  }
	  
	static String repeat(String s, int n) {
		    if(s == null) {
		        return null;
		    }
		    final StringBuilder sb = new StringBuilder();
		    for(int i = 0; i < n; i++) {
		        sb.append(s);
		    }
		    return sb.toString();
	}
}
