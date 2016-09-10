package com.sunkenbridge.val.Aircraft.Executors;

import org.bukkit.entity.Player;

import com.sunkenbridge.val.Aircraft.Aircraft;
import com.sunkenbridge.val.Aircraft.AircraftPlugin;

public class UnpilotExecutor extends AircraftExecutor {

	public UnpilotExecutor() {
		
		super(1, 1);
	}

	@Override
	public boolean execute(AircraftPlugin plugin, Player sender, String[] args) {
		
		Aircraft senderAircraft = plugin.getAircraft(sender);
		if (senderAircraft != null) {
			
			Player target = plugin.getServer().getPlayer(args[0]);
			if (target == null) {
				
				plugin.sendMessage(sender, AircraftPlugin.LANG_PLAYER_NOT_FOUND);
				return true;
			}
			
			Aircraft targetAircraft = plugin.getAircraft(target);
			if (senderAircraft.equals(targetAircraft)) {
				
				plugin.removeAircraft(target);
				plugin.sendMessage(sender, AircraftPlugin.LANG_COPILOT_REMOVED);
				plugin.sendMessage(target, AircraftPlugin.LANG_COPILOT_NO_LONGER + sender.getDisplayName());
			}
			else {
			
				plugin.sendMessage(sender, AircraftPlugin.LANG_AIRCRAFT_NOT_MATCH);
			}
		}
		else
			plugin.sendMessage(sender, AircraftPlugin.LANG_NO_ACTIVE_AIRCRAFT);
		
		return true;
	}
}