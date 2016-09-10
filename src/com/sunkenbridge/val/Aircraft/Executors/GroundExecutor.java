package com.sunkenbridge.val.Aircraft.Executors;

import org.bukkit.entity.Player;

import com.sunkenbridge.val.Aircraft.Aircraft;
import com.sunkenbridge.val.Aircraft.AircraftPlugin;
import com.sunkenbridge.val.Aircraft.Direction;

public class GroundExecutor extends AircraftExecutor {

	public GroundExecutor() {
		
		super(0, 0);
	}

	@Override
	public boolean execute(AircraftPlugin plugin, Player sender, String[] args) {
		
		Aircraft pilot = plugin.getAircraft(sender);
		
		if (pilot != null) {
			
			// turn on collisions
			boolean cols = pilot.getCollisionsEnabled();
			pilot.setCollisionsEnabled(true);
			
			// move
			try {
				
				pilot.move(Direction.DOWN, 9999);
			}
			catch (Exception e) { } // will throw collision exception, we know that!
			
			// restore collision settings
			pilot.setCollisionsEnabled(cols);
			plugin.sendMessage(sender, AircraftPlugin.LANG_GROUNDED);
		}
		else
			plugin.sendMessage(sender, AircraftPlugin.LANG_NO_ACTIVE_AIRCRAFT);
		
		return true;
	}
}
