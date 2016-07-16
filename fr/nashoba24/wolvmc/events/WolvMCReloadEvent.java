package fr.nashoba24.wolvmc.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WolvMCReloadEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
 
    public WolvMCReloadEvent() {

    }

	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
        return handlers;
    }
 
}
