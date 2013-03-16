package com.foivos.wormhole.networking;

import net.minecraft.tileentity.TileEntity;

public class NetworkedInventory {
	
	public TileEntity tile;
	
	public NetworkedInventory(TileEntity tile) {
		this.tile = tile;
	}
	
	@Override
	public String toString() {
		return "("+tile.xCoord+", "+tile.yCoord+", "+tile.zCoord+")" ;
	}

}
