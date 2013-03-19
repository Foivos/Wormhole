package com.foivos.wormhole.transport;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

import com.foivos.wormhole.Spot;
import com.foivos.wormhole.TileManager;
import com.foivos.wormhole.networking.WormholeNetwork;

public class TileWormhole extends TileEntity{
	public byte color;
	public Spot base;
	public WormholeNetwork network;
	
	@Override
    public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            color = tagCompound.getByte("color");
            int world = tagCompound.getInteger("baseWorld");
            int x = tagCompound.getInteger("baseX");
            int y = tagCompound.getInteger("baseY");
            int z = tagCompound.getInteger("baseZ");
            base = new Spot(world, x, y, z);
            world = tagCompound.getInteger("world");
            if(base != null && xCoord == base.x && yCoord == base.y && zCoord == base.z && world == base.world) {
    	    	network = new WormholeNetwork(base, color, tagCompound);
    	    }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            
            tagCompound.setByte("color", color);
            tagCompound.setInteger("world", worldObj.getWorldInfo().getDimension());
            if(base != null) {
	            tagCompound.setInteger("baseWorld", base.world);
	            tagCompound.setInteger("baseX", base.x);
	            tagCompound.setInteger("baseY", base.y);
	            tagCompound.setInteger("baseZ", base.z);
	            if(network != null && xCoord == base.x && yCoord == base.y && zCoord == base.z && worldObj.getWorldInfo().getDimension() == base.world) {
	    	    	network.writeToNBT(tagCompound);
	    	    }
            }
            
            
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
	
	@Override
	public void invalidate() {
		super.invalidate();
		if(color == 0 || base == null)
			return;
		TileWormhole baseTile = (TileWormhole) TileManager.getTile(base, TileWormhole.class, true);
		if(baseTile != null && baseTile.network != null)
			baseTile.network.deactivate();
	}
}
