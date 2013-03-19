package com.foivos.wormhole.networking;

import com.foivos.wormhole.Coord;
import com.foivos.wormhole.Spot;

public class NetworkedInventory extends Spot{

	public int side;
	
	public NetworkedInventory(Spot spot, int side) {
		super(spot);
		this.side = side;
	}

	public NetworkedInventory(int world, Coord coord) {
		super(world, coord);
	}

	public NetworkedInventory(int world, int x, int y, int z) {
		super(world, x, y, z);
	}

}
