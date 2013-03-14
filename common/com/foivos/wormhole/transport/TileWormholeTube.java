package com.foivos.wormhole.transport;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileWormholeTube extends TileEntity {

	private byte connections = 0;
	
	
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            
            connections = tagCompound.getByte("connections");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            
            tagCompound.setByte("connections", connections);
            
    }
    
    public byte getConnections() {
    	return connections;
    }

    public void setConnections(byte connections) {
    	if(connections == this.connections)
    		return;
    	this.connections = connections;
    	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);      
    }
    
	public void updateConnections() {
		byte connections = 0;
		for(int i = 0;i<6;i++) {
			ForgeDirection dir = ForgeDirection.getOrientation(i);
			int x = xCoord + dir.offsetX;
			int y = yCoord + dir.offsetY;
			int z = zCoord + dir.offsetZ;
			TileEntity tile = worldObj.getBlockTileEntity(x, y, z);
			if(tile == null)
				continue;
			if(tile instanceof TileWormholeTube) {
				connections |= 1<<i;
				continue;
			}
			if(tile instanceof ISidedInventory) {
				if(((ISidedInventory)tile).getSizeInventorySide(ForgeDirection.getOrientation(i^1)) > 0)
					connections |= 1<<i;
				continue;
			}
			if(tile instanceof IInventory && ((IInventory)tile).getSizeInventory() > 0)
				connections |= 1<<i;
				
		}
		setConnections(connections);
			
		
	}
	
	@Override
	public Packet getDescriptionPacket ()
	{
	    NBTTagCompound tag = new NBTTagCompound();
	    writeToNBT(tag);
	    return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
	}
	
	@Override
	public void onDataPacket (INetworkManager net, Packet132TileEntityData packet)
	{
	    readFromNBT(packet.customParam1);
	    worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}
}
