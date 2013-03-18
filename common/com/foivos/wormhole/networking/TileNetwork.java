package com.foivos.wormhole.networking;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileNetwork extends TileEntity{
	public int network = -1;
	public boolean activated;
	
		
	public boolean connects(int side) {
		return !activated;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("network", network);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		network = tag.getInteger("network");
	    activated = NetworkManager.isActiveNetwork(network);
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