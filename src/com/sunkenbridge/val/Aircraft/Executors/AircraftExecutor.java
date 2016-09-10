package com.sunkenbridge.val.Aircraft.Executors;

import org.bukkit.entity.Player;

import com.sunkenbridge.val.Aircraft.AircraftPlugin;

public abstract class AircraftExecutor {
	
	private int
		minArgumentCount,
		maxArgumentCount;
	
	public AircraftExecutor(int minArgumentCount, int maxArgumentCount) {

		this.minArgumentCount = minArgumentCount;
		this.maxArgumentCount = maxArgumentCount;
	}
	
	public int getMinArgumentCount() {
		
		return minArgumentCount;
	}
	
	public int getMaxArgumentCount() {
		
		return maxArgumentCount;
	}
	
	
	public abstract boolean execute(AircraftPlugin plugin, Player sender, String[] args);
}
