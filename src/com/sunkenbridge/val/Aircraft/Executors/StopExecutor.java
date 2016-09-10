package com.sunkenbridge.val.Aircraft.Executors;

import org.bukkit.entity.Player;

import com.sunkenbridge.val.Aircraft.Aircraft;
import com.sunkenbridge.val.Aircraft.AircraftPlugin;

public class StopExecutor extends AircraftExecutor {

	public StopExecutor() {
		
		super(0, 0);
	}

	@Override
	public boolean execute(AircraftPlugin plugin, Player sender, String[] args) {
		
		Aircraft aircraft = plugin.getAircraft(sender);
		
		if (aircraft != null) {
			
			if (sender.equals(aircraft.getOwner())) {
			
				Player[] pilots = plugin.getCopilots(aircraft);
				for (Player pilot : pilots) {
				
					plugin.removeAircraft(pilot);
					plugin.sendMessage(pilot, AircraftPlugin.LANG_AIRCRAFT_UNLOADED);
				}
			}
			else {
				
				plugin.removeAircraft(sender);
				plugin.sendMessage(sender, AircraftPlugin.LANG_AIRCRAFT_UNLOADED);
			}
		}
		else
			plugin.sendMessage(sender, AircraftPlugin.LANG_NO_ACTIVE_AIRCRAFT);
		
		return true;
	}
}
