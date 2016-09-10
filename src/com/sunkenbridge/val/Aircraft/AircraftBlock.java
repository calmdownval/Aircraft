package com.sunkenbridge.val.Aircraft;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.MetadataValue;

public class AircraftBlock {

	public Material type;
	public byte data;
	public boolean isAir;
	public boolean isSolid;
	public List<MetadataValue> meta;
	
	public int x, y, z;
	
	public AircraftBlock(Block block) {
	
		type = block.getType();
		data = block.getData();
		isAir = type.equals(Material.AIR);
		isSolid = (type.isBlock() && type.isSolid());
		
		meta = block.hasMetadata(null) ? block.getMetadata(null) : null;
		
		x = block.getX();
		y = block.getY();
		z = block.getZ();
	}
	
	public AircraftBlock() {
		
		type = Material.AIR;
		isAir = true;
		isSolid = false;
	}
}
