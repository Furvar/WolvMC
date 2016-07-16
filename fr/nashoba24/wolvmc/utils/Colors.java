package fr.nashoba24.wolvmc.utils;

import org.bukkit.ChatColor;

public class Colors {

	public static String get(String s) {
		s.replaceAll(ChatColor.AQUA + "", "&b");
		s.replaceAll(ChatColor.BLACK + "", "&0");
		s.replaceAll(ChatColor.BLUE + "", "&9");
		s.replaceAll(ChatColor.BOLD + "", "&l");
		s.replaceAll(ChatColor.DARK_AQUA + "", "&3");
		s.replaceAll(ChatColor.DARK_BLUE + "", "&1");
		s.replaceAll(ChatColor.DARK_GRAY + "", "&8");
		s.replaceAll(ChatColor.DARK_GREEN + "", "&2");
		s.replaceAll(ChatColor.DARK_PURPLE + "", "&5");
		s.replaceAll(ChatColor.DARK_RED + "", "&4");
		s.replaceAll(ChatColor.GOLD + "", "&6");
		s.replaceAll(ChatColor.GRAY + "", "&7");
		s.replaceAll(ChatColor.GREEN + "", "&a");
		s.replaceAll(ChatColor.ITALIC + "", "&o");
		s.replaceAll(ChatColor.LIGHT_PURPLE + "", "&d");
		s.replaceAll(ChatColor.MAGIC + "", "&k");
		s.replaceAll(ChatColor.RED + "", "&c");
		s.replaceAll(ChatColor.RESET + "", "&r");
		s.replaceAll(ChatColor.STRIKETHROUGH + "", "&m");
		s.replaceAll(ChatColor.UNDERLINE + "", "&n");
		s.replaceAll(ChatColor.WHITE + "", "&f");
		s.replaceAll(ChatColor.YELLOW + "", "&e");
		return s;
	}
	
}
