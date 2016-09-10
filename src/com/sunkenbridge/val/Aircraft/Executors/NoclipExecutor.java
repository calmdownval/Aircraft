package com.sunkenbridge.val.Aircraft.Executors;

import org.bukkit.entity.Player;

import com.sunkenbridge.val.Aircraft.Aircraft;
import com.sunkenbridge.val.Aircraft.AircraftPlugin;

public class NoclipExecutor extends AircraftExecutor {

	public NoclipExecutor() {
		
		super(0, 0);
	}

	@Override
	public boolean execute(AircraftPlugin plugin, Player sender, String[] args) {
		
		Aircraft pilot = plugin.getAircraft(sender);
		
		if (pilot != null) {
			
			boolean cols = !pilot.getCollisionsEnabled();
			pilot.setCollisionsEnabled(cols);
			
			plugin.sendMessage(sender, cols ?
				AircraftPlugin.LANG_AIRCRAFT_COL_ON :
				AircraftPlugin.LANG_AIRCRAFT_COL_OFF);
		}
		else
			plugin.sendMessage(sender, AircraftPlugin.LANG_NO_ACTIVE_AIRCRAFT);
		
		return true;
	}
}
