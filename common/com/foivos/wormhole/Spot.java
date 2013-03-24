package com.foivos.wormhole;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class Spot extends Coord {
	
	public final int world;
	
	public Spot(int world, int x,int y, int z) {
		super(x, y, z);
		this.world = world;
	}

	public Spot(int world, Coord coord) {
		super(coord);
		this.world = world;
	}

	public Spot(Spot place) {
		super(place.x, place.y, place.z);
		this.world = place.world;
	}
	
	public Spot(Place place) {
		this(place.world.getWorldInfo().getDimension(), place);
	}
	
	public Spot(World world, int x, int y, int z) {
		super(x, y, z);
		this.world = world.getWorldInfo().getDimension();
	}

	public Spot move(ForgeDirection dir) {
		return new Spot(world, super.move(dir));
	}
	
	public Place toPlace() {
		return new Place(this);
	}
	
	public int compareTo(Spot o) {
		return world == o.world? super.compareTo(o) : world - o.world;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Spot))
			return false;
		if(world == ((Spot)obj).world)
			return super.equals(obj);
		return false;
	}
	
	@Override
	public String toString() {
		return("(" + world + ", " + x + ", "+ y + ", " + z + ")");
	}
	

}
