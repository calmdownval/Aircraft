package com.sunkenbridge.val.Aircraft.Executors;

import org.bukkit.entity.Player;

import com.sunkenbridge.val.Aircraft.AircraftPlugin;

public class HelpExecutor extends AircraftExecutor {

	public HelpExecutor() {
		
		super(0, 0);
	}

	@Override
	public boolean execute(AircraftPlugin plugin, Player sender, String[] args) {
		
		plugin.sendMessage(sender, "Aircraft help:");
		sender.sendMessage("§a/ac pilot §e- creates a new aircraft");
		sender.sendMessage("§a/ac copilot <player> §e- grants the pilot rights to a player");
		sender.sendMessage("§a/ac unpilot <player> §e- removes the pilot rights from a player");
		sender.sendMessage("§a/ac stop §e- unloads the aircraft (if any)");
		sender.sendMessage("§a/ac delete §e- unloads the aircraft and deletes it");
		sender.sendMessage("§a/ac noclip §e- toggles collisions on/off");
		sender.sendMessage("§a/ac ground §e- moves the aircraft to the ground level");
		sender.sendMessage("§a/ac board [player] §e- teleports the player to the aircraft");
		sender.sendMessage("§a/ac speed <speed> §e- sets the speed of the aircraft");
		sender.sendMessage("§a/ac tp §e- teleports the aircraft to you");
		sender.sendMessage("§a/ac help §e- shows this help");
		return true;
	}
}