package fr.nashoba24.wolvmc.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;

import fr.nashoba24.wolvmc.WolvMC;

public class WolvMCInitEffectsEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private String race;
 
    public WolvMCInitEffectsEvent(Player p) {
        this.player = p;
        this.race = WolvMC.getRace(p.getName());
        for (PotionEffect effect : player.getActivePotionEffects()) {
        	player.removePotionEffect(effect.getType());
        }
    }

	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public Player getPlayer() {
    	return this.player;
    }
    
    public String getRace() {
    	return this.race;
    }
 
}
