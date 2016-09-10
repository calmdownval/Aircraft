package com.sunkenbridge.val.Aircraft.Executors;

import org.bukkit.entity.Player;

import com.sunkenbridge.val.Aircraft.Aircraft;
import com.sunkenbridge.val.Aircraft.AircraftPlugin;

public class SpeedExecutor extends AircraftExecutor {

	public SpeedExecutor() {
		
		super(1, 1);
	}

	@Override
	public boolean execute(AircraftPlugin plugin, Player sender, String[] args) {
		
		Aircraft pilot = plugin.getAircraft(sender);
		
		if (pilot != null) {
			
			try {
				
				int speed = Math.abs(Integer.parseInt(args[0]));
				pilot.setSpeed(Math.min(speed, 10));
				
				if (speed > 10)
					plugin.sendMessage(sender, AircraftPlugin.LANG_SPEED_OVERFLOW);
				
				else
					plugin.sendMessage(sender, AircraftPlugin.LANG_SPEED_SET);
			}
			catch (Exception e) {
				
				plugin.sendMessage(sender, AircraftPlugin.LANG_INVALID_NUMBER);
			}
		}
		else
			plugin.sendMessage(sender, AircraftPlugin.LANG_NO_ACTIVE_AIRCRAFT);
		
		return true;
	}
}