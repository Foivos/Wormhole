package com.foivos.wormhole.networking;

import net.minecraft.tileentity.TileEntity;

public class TileNetwork extends TileEntity{
	public WormholeNetwork network;
		
	public boolean connects(int side) {
		return network == null || !network.activated;
	}
	
}