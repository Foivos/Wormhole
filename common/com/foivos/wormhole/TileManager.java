package com.foivos.wormhole;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class TileManager {
	public static TileEntity getTile(IBlockAccess world, int x, int y, int z, boolean force) {
		if(force || !(world instanceof World) || ((World) world).checkChunksExist(x, y, z, x, y, z))
			return world.getBlockTileEntity(x, y, z);
		return null;
	}
	
	public static TileEntity getTile(IBlockAccess world, Coord coord, boolean force) {
		return getTile(world, coord.x, coord.y, coord.z, force);
	}
	
	public static TileEntity getTile(Spot spot, boolean force) {
		IBlockAccess world = DimensionManager.getWorld(spot.world);
		return getTile(world, spot.x, spot.y, spot.z, force);
	}
	
	public static TileEntity getTile(Place place, boolean force) {
		return getTile(place.world,  place.x, place.y, place.z, force);
	}
	
	public static TileEntity getTile(IBlockAccess world, int x, int y, int z, Class clazz, boolean force) {
		TileEntity tile = getTile(world, x, y, z, force);
		if(tile != null && clazz.isInstance(tile))
			return tile;
		return null;
	}
	
	public static TileEntity getTile(IBlockAccess world, Coord coord, Class clazz, boolean force) {
		return getTile(world, coord.x, coord.y, coord.z, clazz, force);
	}
	
	public static TileEntity getTile(Spot spot, Class clazz, boolean force) {
		IBlockAccess world = DimensionManager.getWorld(spot.world);
		return getTile(world, spot.x, spot.y, spot.z, clazz, force);
	}
	
	public static TileEntity getTile(Place place, Class clazz, boolean force) {
		return getTile(place.world,  place.x, place.y, place.z, clazz, force);
	}
}
