package com.sunkenbridge.val.Aircraft;

import org.bukkit.Location;

public enum Direction {

	WEST (0, -1,  0,  0), // x -
	EAST (1,  1,  0,  0), // x +
	DOWN (2,  0, -1,  0), // y -
	UP   (3,  0,  1,  0), // y +
	NORTH(4,  0,  0, -1), // z -
	SOUTH(5,  0,  0,  1); // z +
	
	private int id, x, y, z;
	
	private Direction(int id, int x, int y, int z) {
	
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getID() { return id; }
	
	public int getX() { return x; }
	
	public int getY() { return y; }
	
	public int getZ() { return z; }
	
	
	public static Direction point(Location a, Location b) {
		
		double x = b.getX() - a.getX(),
			   y = b.getY() - a.getY(),
			   z = b.getZ() - a.getZ(),
			   m = Math.max(Math.max(Math.abs(x), Math.abs(y)), Math.abs(z));
		
		if (Math.abs(x) == m)
			return (x < 0) ? WEST : EAST;
			
		if (Math.abs(y) == m)
			return (y < 0) ? DOWN : UP;
		
		return (z < 0) ? NORTH : SOUTH;
	}
}
