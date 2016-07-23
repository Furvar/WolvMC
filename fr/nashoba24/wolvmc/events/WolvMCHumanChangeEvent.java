package fr.nashoba24.wolvmc.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WolvMCHumanChangeEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Player player;
 
    public WolvMCHumanChangeEvent(Player p) {
        this.player = p;
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
 
}
