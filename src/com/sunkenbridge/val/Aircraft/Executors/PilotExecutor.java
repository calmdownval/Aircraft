package com.sunkenbridge.val.Aircraft.Executors;

import org.bukkit.entity.Player;

import com.sunkenbridge.val.Aircraft.AircraftPlugin;

public class PilotExecutor extends AircraftExecutor {

	public PilotExecutor() {
		
		super(0, 0);
	}

	@Override
	public boolean execute(AircraftPlugin plugin, Player sender, String[] args) {
		
		plugin.enqueue(sender);
		plugin.sendMessage(sender, AircraftPlugin.LANG_CREATE_MESSAGE);
		return true;
	}
}
