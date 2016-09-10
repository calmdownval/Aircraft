package com.sunkenbridge.val.Aircraft.Executors;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sunkenbridge.val.Aircraft.Aircraft;
import com.sunkenbridge.val.Aircraft.AircraftPlugin;

public class DeleteExecutor extends AircraftExecutor {

	public DeleteExecutor() {
		
		super(0, 0);
	}

	@Override
	public boolean execute(AircraftPlugin plugin, Player sender, String[] args) {
		
		Aircraft aircraft = plugin.getAircraft(sender);
		
		if (aircraft != null) {
			
			if (sender.equals(aircraft.getOwner())) {
				
				// destroy effect
				Location loc = aircraft.getSpawnPoint();
				loc.getWorld().playEffect(loc, Effect.POTION_BREAK, 16456);
				
				// delete aircraft
				aircraft.delete();
				Player[] pilots = plugin.getCopilots(aircraft);
				
				for (Player pilot : pilots) {
				
					plugin.removeAircraft(pilot);
					plugin.sendMessage(pilot, AircraftPlugin.LANG_AIRCRAFT_DELETED);
				}
			}
			else {
				
				plugin.sendMessage(sender, AircraftPlugin.LANG_AIRCRAFT_NOT_DELETED);
				plugin.sendMessage(sender, AircraftPlugin.LANG_AIRCRAFT_USE_STOP);
			}
		}
		else
			plugin.sendMessage(sender, AircraftPlugin.LANG_NO_ACTIVE_AIRCRAFT);
		
		return true;
	}
}
