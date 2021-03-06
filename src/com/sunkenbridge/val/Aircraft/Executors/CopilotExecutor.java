package com.sunkenbridge.val.Aircraft.Executors;

import org.bukkit.entity.Player;

import com.sunkenbridge.val.Aircraft.Aircraft;
import com.sunkenbridge.val.Aircraft.AircraftPlugin;

public class CopilotExecutor extends AircraftExecutor {

	public CopilotExecutor() {
		
		super(1, 1);
	}

	@Override
	public boolean execute(AircraftPlugin plugin, Player sender, String[] args) {
		
		Aircraft pilot = plugin.getAircraft(sender);
		
		if (pilot != null) {
			
			Player target = plugin.getServer().getPlayer(args[0]);
			if (target == null) {
				
				plugin.sendMessage(sender, AircraftPlugin.LANG_PLAYER_NOT_FOUND);
				return true;
			}
			
			if (plugin.hasAircraft(target)) {
				
				plugin.sendMessage(sender, AircraftPlugin.LANG_PLAYER_HAS_AIRCRAFT);
				return true;
			}
			
			plugin.setAircraft(target, pilot);
			plugin.sendMessage(sender, AircraftPlugin.LANG_COPILOT_ADDED);
			plugin.sendMessage(target, AircraftPlugin.LANG_COPILOT_GOT + sender.getDisplayName());
		}
		else
			plugin.sendMessage(sender, AircraftPlugin.LANG_NO_ACTIVE_AIRCRAFT);
		
		return true;
	}
}