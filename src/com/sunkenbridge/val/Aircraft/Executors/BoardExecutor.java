package com.sunkenbridge.val.Aircraft.Executors;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sunkenbridge.val.Aircraft.Aircraft;
import com.sunkenbridge.val.Aircraft.AircraftPlugin;

public class BoardExecutor extends AircraftExecutor {

	public BoardExecutor() {
		
		super(0, 1);
	}

	@Override
	public boolean execute(AircraftPlugin plugin, Player sender, String[] args) {
		
		Aircraft pilot = plugin.getAircraft(sender);
		
		if (pilot != null) {
			
			Player target;
			if (args.length > 0) {
			
				target = plugin.getServer().getPlayer(args[0]);
				if (target == null) {
					
					plugin.sendMessage(sender, AircraftPlugin.LANG_PLAYER_NOT_FOUND);
					return true;
				}
			}
			else {
			
				target = sender;
			}
			
			Location
				src = target.getLocation(),
				dst = pilot.getSpawnPoint();
			
			src.setX(dst.getX());
			src.setY(dst.getY());
			src.setZ(dst.getZ());
			target.teleport(src);
			
			plugin.sendMessage(sender, AircraftPlugin.LANG_TELEPORTED_ONBOARD_SELF);
			
			if (!target.equals(sender))
				plugin.sendMessage(target, AircraftPlugin.LANG_TELEPORTED_ONBOARD);
		}
		else
			plugin.sendMessage(sender, AircraftPlugin.LANG_NO_ACTIVE_AIRCRAFT);
		
		return true;
	}
}
