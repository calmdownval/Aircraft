package com.sunkenbridge.val.Aircraft.Executors;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sunkenbridge.val.Aircraft.Aircraft;
import com.sunkenbridge.val.Aircraft.AircraftPlugin;

public class TpExecutor extends AircraftExecutor {

	public TpExecutor() {
		
		super(0, 0);
	}

	@Override
	public boolean execute(AircraftPlugin plugin, Player sender, String[] args) {
		
		Aircraft pilot = plugin.getAircraft(sender);
		
		if (pilot != null) {
			
			Location
				dst = sender.getLocation(),
				spawn = pilot.getSpawnPoint(),
				loc = pilot.getLocation();

			dst.setX(dst.getBlockX() - (spawn.getBlockX() - loc.getBlockX()));
			dst.setY(dst.getBlockY() - (spawn.getBlockY() - loc.getBlockY()));
			dst.setZ(dst.getBlockZ() - (spawn.getBlockZ() - loc.getBlockZ()));
			
			try {
				
				pilot.teleport(dst);
				plugin.sendMessage(sender, AircraftPlugin.LANG_TELEPORTED_LOC);
			}
			catch (Exception e) {
			
				plugin.sendMessage(sender, e.getMessage());
			}
		}
		else
			plugin.sendMessage(sender, AircraftPlugin.LANG_NO_ACTIVE_AIRCRAFT);
		
		return true;
	}
}
