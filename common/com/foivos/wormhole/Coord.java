package com.foivos.wormhole;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ForgeDummyContainer;

public class Coord implements Comparable<Coord>{
	public int x,y,z;

	public Coord(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Coord move(ForgeDirection dir) {
		return new Coord(x+dir.offsetX,y+dir.offsetY,z+dir.offsetZ);
	}

	@Override
	public int compareTo(Coord o) {
		if(x==o.x) {
			if(y==o.y)
				return z-o.z;
			return y-o.y;
		}
		return x-o.x;
	}
}
