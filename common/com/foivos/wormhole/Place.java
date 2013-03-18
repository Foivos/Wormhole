package com.foivos.wormhole;

public class Place extends Coord{
	
	public int world;
	
	public Place(int world, int x,int y, int z) {
		super(x, y, z);
		this.world = world;
	}

	public Place(int world, Coord coord) {
		super(coord);
		this.world = world;
	}

	public Place(Place place) {
		super(place.x, place.y, place.z);
		this.world = place.world;
	}

}
