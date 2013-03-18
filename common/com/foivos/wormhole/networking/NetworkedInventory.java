package com.foivos.wormhole.networking;

import com.foivos.wormhole.Coord;
import com.foivos.wormhole.Place;

public class NetworkedInventory extends Place{

	
	public NetworkedInventory(Place place) {
		super(place);
	}

	public NetworkedInventory(int world, Coord coord) {
		super(world, coord);
	}

	public NetworkedInventory(int world, int x, int y, int z) {
		super(world, x, y, z);
	}

}
