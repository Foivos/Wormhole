package com.foivos.wormhole;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;

public class Place extends Coord{
	
	public final World world;

	public Place(int world, int x,int y, int z) {
		super(x, y, z);
		this.world = DimensionManager.getWorld(world);
	}

	public Place(int world, Coord coord) {
		super(coord);
		this.world = DimensionManager.getWorld(world);
	}

	public Place(Spot spot) {
		super(spot.x, spot.y, spot.z);
		this.world = DimensionManager.getWorld(spot.world);
	}
	
	public Place(World world, int x,int y, int z) {
		super(x, y, z);
		this.world = world;
	}

	public Place(World world, Coord coord) {
		super(coord);
		this.world = world;
	}

	public Place(Place place) {
		super(place.x, place.y, place.z);
		this.world = place.world;
	}
	
	public Place move(ForgeDirection dir) {
		return new Place(world, super.move(dir));
	}

	public Spot toSpot() {
		return new Spot(world.getWorldInfo().getDimension(), this);
	}

}
