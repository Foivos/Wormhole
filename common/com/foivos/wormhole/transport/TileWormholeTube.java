package com.foivos.wormhole.transport;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.foivos.wormhole.Place;
import com.foivos.wormhole.TileManager;

public class TileWormholeTube extends TileWormhole{
	public byte connections;
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setByte("connections", connections);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		connections = tagCompound.getByte("connections");
	}
	
	public void updateConnections() {
		connections=0;
		Place thisPlace = new Place(worldObj, xCoord, yCoord ,zCoord);
		for(int i=0; i<6; i++) {
			ForgeDirection dir = ForgeDirection.getOrientation(i);
			Place place = thisPlace.move(dir);
			TileEntity tile = TileManager.getTile(place, true);
			if(tile == null)
				continue;
			if(tile instanceof TileWormhole) {
				if(((TileWormhole)tile).color == color) {
					connections |= 1<<i;
					if(tile instanceof TileWormholeTube && (((TileWormholeTube)tile).connections & 1<<(i^1)) == 0)
						((TileWormholeTube)tile).updateConnections();
				}
				continue;
			}
			if(tile instanceof ISidedInventory) {
				if(((ISidedInventory)tile).func_94128_d(i^1) > 0)
					connections |= 1<<i;
				continue;
			}
			if(tile instanceof IInventory) {
				if(((IInventory)tile).getSizeInventory() > 0)
					connections |= 1<<i;
				continue;
			}
 		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public Packet getDescriptionPacket() {
		updateConnections();
		return super.getDescriptionPacket();
	}
}
