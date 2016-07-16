package fr.nashoba24.wolvmc.events;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WolvMCSaveEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
    private Player p;
    private HashMap<String, Object> data = new HashMap<String, Object>();
 
    public WolvMCSaveEvent(Player p) {
        this.p = p;
    }
 
    public Player getPlayer() {
        return this.p;
    }

	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
        return handlers;
    }
	
	public void save(String data, Object value) {
		this.data.put(data, value);
	}
	
	public HashMap<String, Object> getSaveList() {
		return data;
	}
 
}
