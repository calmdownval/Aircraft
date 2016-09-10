package com.sunkenbridge.val.Aircraft;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Aircraft {
	
	private int x, y, z, w, h, d, slice;
	private double[] spawn;
	private World world;
	private Player owner;
	private AircraftBlock[] components;
	private AircraftBlock[][] collisions;
	
	private int speed = 1;
	private int sizeLimit;
	private boolean enableCollisions = true;
	
	@SuppressWarnings("serial")
	private static List<Material>
		cuts = new ArrayList<Material>() {{
			add(Material.AIR);
			add(Material.WATER);
			add(Material.STATIONARY_WATER);
			add(Material.LAVA);
			add(Material.STATIONARY_LAVA);
			add(Material.STONE);
			add(Material.GRASS);
			add(Material.DIRT);
			add(Material.SANDSTONE);
			add(Material.SAND);
			add(Material.GRAVEL);
		}};
	
	public Aircraft(Block initial, Player owner, int sizeLimit) throws Exception {
		
		world = initial.getWorld();
		this.owner = owner;
		this.sizeLimit = sizeLimit;
		
		// Get aircraft blocks
		List<AircraftBlock> raw = new ArrayList<AircraftBlock>();
		fill(raw, 
			initial.getX(),
			initial.getY(),
			initial.getZ());
		
		// check size
		if (raw.size() == 0)
			throw new Exception(AircraftPlugin.LANG_ZERO_SIZE);
		
		// Process buffered blocks
		processRaw(raw);
	}
	
	private void fill(List<AircraftBlock> buffer, int x, int y, int z) throws Exception {
		
		Block block = world.getBlockAt(x, y, z);
		
		// check material
		if (cuts.contains(block.getType()))
			return;
		
		// check if current block is already in queue
		AircraftBlock temp;
		for (int i = 0, max = buffer.size(); i < max; i++) {
			
			temp = buffer.get(i);
			if (temp.x == x && temp.y == y && temp.z == z)
				return;
		}
		
		// queue
		if (buffer.size() < sizeLimit)
			buffer.add(new AircraftBlock(block));
		
		else
			throw new Exception(AircraftPlugin.LANG_MAX_SIZE);
		
		// spread
		fill(buffer, x - 1, y - 1, z - 1);
		fill(buffer, x - 1, y - 1, z + 1);
		fill(buffer, x - 1, y - 1, z    );
		fill(buffer, x + 1, y - 1, z - 1);
		fill(buffer, x + 1, y - 1, z + 1);
		fill(buffer, x + 1, y - 1, z    );
		fill(buffer, x    , y - 1, z - 1);
		fill(buffer, x    , y - 1, z + 1);
		fill(buffer, x    , y - 1, z    );

		fill(buffer, x - 1, y + 1, z - 1);
		fill(buffer, x - 1, y + 1, z + 1);
		fill(buffer, x - 1, y + 1, z    );
		fill(buffer, x + 1, y + 1, z - 1);
		fill(buffer, x + 1, y + 1, z + 1);
		fill(buffer, x + 1, y + 1, z    );
		fill(buffer, x    , y + 1, z - 1);
		fill(buffer, x    , y + 1, z + 1);
		fill(buffer, x    , y + 1, z    );

		fill(buffer, x - 1, y, z - 1);
		fill(buffer, x - 1, y, z + 1);
		fill(buffer, x - 1, y, z    );
		fill(buffer, x + 1, y, z - 1);
		fill(buffer, x + 1, y, z + 1);
		fill(buffer, x + 1, y, z    );
		fill(buffer, x    , y, z - 1);
		fill(buffer, x    , y, z + 1);
	}
	
	private void processRaw(List<AircraftBlock> raw) {
		
		int maxX, minX, maxY, minY, maxZ, minZ;
		maxX = minX = raw.get(0).x;
		maxY = minY = raw.get(0).y;
		maxZ = minZ = raw.get(0).z;
		
		// get dimensions and position
		AircraftBlock temp;
		for (int i = 0, max = raw.size(); i < max; i++) {
			
			temp = raw.get(i);
			maxX = (temp.x > maxX) ? temp.x : maxX;
			minX = (temp.x < minX) ? temp.x : minX;
			maxY = (temp.y > maxY) ? temp.y : maxY;
			minY = (temp.y < minY) ? temp.y : minY;
			maxZ = (temp.z > maxZ) ? temp.z : maxZ;
			minZ = (temp.z < minZ) ? temp.z : minZ;
		}
		
		x = minX;
		y = minY;
		z = minZ;
		w = maxX - minX + 3;
		h = maxY - minY + 3;
		d = maxZ - minZ + 3;
		
		// create block buffer and fill it
		AircraftBlock[][][] blocks = new AircraftBlock[w][h][d];
		for (int X = 0; X < w; X++)
			for (int Y = 0; Y < h; Y++)
				for (int Z = 0; Z < d; Z++) {
				
					temp = new AircraftBlock();
					temp.x = X - 1;
					temp.y = Y - 1;
					temp.z = Z - 1;
					
					blocks[X][Y][Z] = temp;
				}
		
		for (int i = 0, max = raw.size(); i < max; i++) {
			
			temp = raw.get(i);
			temp.x = temp.x - x;
			temp.y = temp.y - y;
			temp.z = temp.z - z;
			
			blocks[temp.x + 1][temp.y + 1][temp.z + 1] = temp;
		}
		
		// get aircraft surface & blocks
		List<AircraftBlock> temp_a = new ArrayList<AircraftBlock>(); // dependent blocks
		List<AircraftBlock> temp_b = new ArrayList<AircraftBlock>(); // stand-alone blocks
		
		// collision blocks (surface)
		List<AircraftBlock> col_xm = new ArrayList<AircraftBlock>();
		List<AircraftBlock> col_xx = new ArrayList<AircraftBlock>();
		List<AircraftBlock> col_ym = new ArrayList<AircraftBlock>();
		List<AircraftBlock> col_yx = new ArrayList<AircraftBlock>();
		List<AircraftBlock> col_zm = new ArrayList<AircraftBlock>();
		List<AircraftBlock> col_zx = new ArrayList<AircraftBlock>();
		
		AircraftBlock temp2;
		
		for (int i = 0, max = raw.size(); i < max; i++) {
			
			temp = raw.get(i);
			
			if (temp.isSolid)
				temp_b.add(temp);
			
			else
				temp_a.add(temp);
			
			temp2 = blocks[temp.x    ][temp.y + 1][temp.z + 1]; // X - 1 (west)
			if (temp2.isAir) { col_xm.add(temp2); }
			
			temp2 = blocks[temp.x + 2][temp.y + 1][temp.z + 1]; // X + 1 (east)
			if (temp2.isAir) { col_xx.add(temp2); }
			
			temp2 = blocks[temp.x + 1][temp.y    ][temp.z + 1]; // Y - 1 (down)
			if (temp2.isAir) { col_ym.add(temp2); }
			
			temp2 = blocks[temp.x + 1][temp.y + 2][temp.z + 1]; // Y + 1 (up)
			if (temp2.isAir) { col_yx.add(temp2); }
			
			temp2 = blocks[temp.x + 1][temp.y + 1][temp.z    ]; // Z - 1 (north)
			if (temp2.isAir) { col_zm.add(temp2); }
			
			temp2 = blocks[temp.x + 1][temp.y + 1][temp.z + 2]; // Z + 1 (south)
			if (temp2.isAir) { col_zx.add(temp2); }
		}
		
		// append stand-alone blocks AFTER dependent
        slice = temp_a.size();
		temp_a.addAll(temp_b);
		components = temp_a.toArray(new AircraftBlock[temp_a.size()]);
		
		// get colliders
		collisions = new AircraftBlock[][] {

			col_xm.toArray(new AircraftBlock[col_xm.size()]),
			col_xx.toArray(new AircraftBlock[col_xx.size()]),
			col_ym.toArray(new AircraftBlock[col_ym.size()]),
			col_yx.toArray(new AircraftBlock[col_yx.size()]),
			col_zm.toArray(new AircraftBlock[col_zm.size()]),
			col_zx.toArray(new AircraftBlock[col_zx.size()])
		};
		
		// get spawn point
		if (h <= 3)
			spawn = new double[] { (double)(w - 2) / 2.0, h - 2, (double)(d - 2) / 2.0 };
		
		else {
		
			spawn = new double[] { (double)w / 2.0, h - 2, (double)d / 2.0 };
			double[] guess = spawn.clone();
			double a, b, dist = w * w + d * d;
			
			// find space nearest to the center
			for (int Y = 1, toY = h - 1; Y < toY; Y++)
				for (int X = 1, toX = w - 1; X < toX; X++)
					for (int Z = 1, toZ = d - 1; Z < toZ; Z++) 
						if (blocks[X][Y][Z].isSolid && blocks[X][Y + 1][Z].isAir) {
						
							if (Y < h - 2)
								if (!blocks[X][Y + 2][Z].isAir)
									continue;
							
							a = (double)X - spawn[0];
							b = (double)Z - spawn[2];
							double score = a * a + b * b;
							
							if (score < dist) {
							
								guess[0] = (double)X - 0.5;
								guess[1] = (double)Y;
								guess[2] = (double)Z - 0.5;
								dist = score;
							}
						}
			
			spawn = guess;
		}
	}
	
	
	// getters & setters
	public boolean getCollisionsEnabled() {
		
		return enableCollisions;
	}
	
	public void setCollisionsEnabled(boolean enable) {
		
		enableCollisions = enable;
	}
	
	public int getSpeed() {
		
		return speed;
	}
	
	public void setSpeed(int speed) {
		
		this.speed = speed;
	}
	
	public World getWorld() {
		
		return world;
	}
	
	public int getSize() {
		
		return components.length;
	}
	
	public Player getOwner() {
		
		return owner;
	}
	
	public Location getLocation() {
		
		return new Location(world, x, y, z);
	}

	public Location getSpawnPoint() {
	
		return new Location(world, x + spawn[0], y + spawn[1], z + spawn[2]);
	}
	
	public Player[] getAboard() {
	
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		ArrayList<Player> buffer = new ArrayList<Player>();
		
		for (Player player : players) {
			
			// ignore flying players
			if (player.isFlying())
				continue;
			
			if (locationInRegion(player.getLocation()))
				buffer.add(player);
		}
		
		return buffer.toArray(new Player[buffer.size()]);
	}
	
	
	// other methods
	public boolean locationInRegion(Location loc) {
	
		if (!world.equals(loc.getWorld()))
			return false;
		
		int x = loc.getBlockX(),
			y = loc.getBlockY(),
			z = loc.getBlockZ(),
			X = x - this.x, 
			Y = y - this.y, 
			Z = z - this.z;
		
		if (X < 0 || X >= w || Y < 0 || Y >= h || Z < 0 || Z >= d)
			return false;
		
		return true;
	}
	
	public boolean collidesAt(int x, int y, int z, Direction direction) {
	
		AircraftBlock current, temp;
		AircraftBlock[] collider = collisions[direction.getID()];
		boolean check;
		
		int deltaX = x - this.x,
			deltaY = y - this.y,
			deltaZ = z - this.z;
		
		for (int i = 0; i < collider.length; i++) {
			
			current = collider[i];
			if (!world.getBlockAt(
					x + current.x, 
					y + current.y,
					z + current.z).getType().equals(Material.AIR)) {
			
				// check if the block belongs to aircraft
				check = true;
				for (int j = 0; j < components.length; j++) {
				
					temp = components[j];
					if (current.x == temp.x - deltaX &&
						current.y == temp.y - deltaY &&
						current.z == temp.z - deltaZ) {
					
						check = false;
						break;
					}
				}
				
				if (check)
					return true;
			}
		}
		
		return false;
	}
	
	public boolean collidesAt(int x, int y, int z) {
	
		AircraftBlock current;
		
		for (int i = 0; i < components.length; i++) {
			
			current = components[i];
			if (!world.getBlockAt(
					x + current.x, 
					y + current.y,
					z + current.z).getType().equals(Material.AIR))
				return true;
		}
		
		return false;
	}
	
	
	// manipulation
	public void move(Direction direction) throws Exception {
		
		move(direction, speed);
	}
	
	public void move(Direction direction, int speed) throws Exception {
		
		int x = direction.getX(),
			y = direction.getY(),
			z = direction.getZ();
		
		/**
		 * 1/9 - check collisions
		 * ----------------------
		 * We have to check collisions for every single step since the
		 * aircraft can be just 1 block thick and would fly through 
         * walls at higher speeds.
		 */
		if (enableCollisions)
			for (int i = 0; i < speed; i++)
				if (collidesAt(
						this.x + x * i,
						this.y + y * i,
						this.z + z * i,
						direction)) {
					
					if (i == 0)
						throw new Exception(AircraftPlugin.LANG_MOVE_COL);
					
					else {
					
						speed = i;
						break;
					}
				}
		
		/**
		 * 2/9 - get players aboard
		 * ------------------------
		 * We are getting the onboard players now so we don't
		 * have to compute offsets after we've moved the aircraft.
		 */
		Player[] aboard = getAboard();
		
		/**
		 * 3/9 - remove non-solid blocks
		 * ----------------------------------
		 * Non-solid blocks are at the begining of the 'components' array.
		 */
		AircraftBlock current;
		Block temp;
		for (int i = 0; i < slice; i++) {

			current = components[i];
			temp = world.getBlockAt(
					this.x + current.x, 
					this.y + current.y, 
					this.z + current.z);
			
			temp.setType(Material.AIR);
			temp.setData((byte) 0);
		}
		
		/**
		 * 4/9 - queue blocks to be removed
		 * --------------------------------
		 * We use the buffer to avoid unneccessary light updates
		 * which has an insane impact on performance, especially with larger aircrafts.
		 */
		HashMap<String, Block> buffer = new HashMap<String, Block>();
		for (int i = components.length - 1; i >= slice; i--) {
			
			current = components[i];
			temp = world.getBlockAt(
				this.x + current.x, 
				this.y + current.y, 
				this.z + current.z);
			
			buffer.put(
				(this.x + current.x) + ";" + (this.y + current.y) + ";" + (this.z + current.z),
				temp);
		}
		
		/**
		 * 5/9 - change the position
		 * -------------------------
		 * We keep the xAdd, yAdd and zAdd values for
		 * player teleportation (see 9/9)
		 */
		int xAdd = x * speed,
			yAdd = y * speed,
			zAdd = z * speed;
		
		this.x += xAdd;
		this.y += yAdd;
		this.z += zAdd;
		
		/**
		 * 6/9 - rebuild the solid blocks
		 * ------------------------------
		 * Solid blocks are at the end of the 'components' array.
		 */
		for (int i = components.length - 1; i >= slice; i--) {
			
			current = components[i];
			
			// remove this block from the remove queue
			buffer.remove((this.x + current.x) + ";" + (this.y + current.y) + ";" + (this.z + current.z));
			
			temp = world.getBlockAt(
					this.x + current.x, 
					this.y + current.y, 
					this.z + current.z);
			
			temp.setType(current.type);
			temp.setData(current.data);
			
			if (current.meta != null)
				for (int n = 0, max = current.meta.size(); n < max; n++)
					temp.setMetadata(null, current.meta.get(n));
		}
		
		/**
		 * 7/9 - remove remaining blocks
		 * -----------------------------
		 * Remove the blocks that are still in the queue.
		 */
		Collection<Block> changes = buffer.values();
		for (Block next : changes) {
		
			next.setType(Material.AIR);
			next.setData((byte) 0);
		}
		
		/**
		 * 8/9 - rebuild the non-solid blocks
		 * ----------------------------------
		 * Place the non-solid blocks now since all the solid
		 * blocks are alerady placed.
		 */
		for (int i = 0; i < slice; i++) {

			current = components[i];
			temp = world.getBlockAt(
					this.x + current.x, 
					this.y + current.y, 
					this.z + current.z);
			
			temp.setType(current.type);
			temp.setData(current.data);
			
			if (current.meta != null)
				for (int n = 0, max = current.meta.size(); n < max; n++)
					temp.setMetadata(null, current.meta.get(n));
		}
		
		/**
		 * 9/9 - move players aboard
		 * -------------------------
		 * Apply the motion to all the players that we found
		 * in the very first step.
		 */
		Location loc;
		for (int i = 0; i < aboard.length; i++) {
		
			loc = aboard[i].getLocation();
			
			loc.setX(loc.getX() + xAdd);
			loc.setY(loc.getY() + yAdd);
			loc.setZ(loc.getZ() + zAdd);
			aboard[i].teleport(loc);
		}
	}
	
	public void teleport(Location loc) throws Exception {
		
		int x = loc.getBlockX(),
			y = loc.getBlockY(),
			z = loc.getBlockZ();
			
		// teleport 1/4 - check collision
		if (enableCollisions)
			if (collidesAt(x, y, z))
				throw new Exception(AircraftPlugin.LANG_TELEPORT_COL);
		
		// teleport 2/4 - remove aircraft
		delete();

		// teleport 3/4 - change position
		this.x = x;
		this.y = y;
		this.z = z;
		
		// teleport 4/4 - create new shifted aircraft
		build();
	}
	
	public void delete() {
		
		AircraftBlock current;
		
		for (int i = 0; i < components.length; i++) {
			
			current = components[i];
			world.getBlockAt(
				this.x + current.x, 
				this.y + current.y, 
				this.z + current.z).setType(Material.AIR);
		}
	}
	
	public void build() {
	
		AircraftBlock current;
		Block temp;
		
		for (int i = components.length - 1; i >= 0; i--) {
			
			current = components[i];
			temp = world.getBlockAt(
				this.x + current.x, 
				this.y + current.y, 
				this.z + current.z);
			
			// set block type
			temp.setType(current.type);
			temp.setData(current.data);
			
			// transfer block's metadata if any
			if (current.meta != null)
				for (int n = 0, max = current.meta.size(); n < max; n++)
					temp.setMetadata(null, current.meta.get(n));
		}
	}
}