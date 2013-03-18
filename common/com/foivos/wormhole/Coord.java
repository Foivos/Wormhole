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
	
	public Coord(Coord coord) {
		x = coord.x;
		y = coord.y;
		z = coord.z;
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

	public double distance(int x2, int y2, int z2) {
		int dx = x2 - x, dy = y2 - y, dz = z2 - z;
		return Math.sqrt(dx*dx+dy*dy+dz*dz);
	}
	
	public int distance2(int x2, int y2, int z2) {
		int dx = x2 - x, dy = y2 - y, dz = z2 - z;
		return dx*dx+dy*dy+dz*dz;
	}
}
