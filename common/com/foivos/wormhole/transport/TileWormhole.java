package com.foivos.wormhole.transport;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.foivos.wormhole.Spot;
import com.foivos.wormhole.TileManager;
import com.foivos.wormhole.networking.NetworkManager;
import com.foivos.wormhole.networking.WormholeNetwork;

public class TileWormhole extends TileEntity{
	public byte color;
	public Spot base;
	
	@Override
    public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            color = tagCompound.getByte("color");
            if(color != 0) {
	            int world = tagCompound.getInteger("baseWorld");
	            int x = tagCompound.getInteger("baseX");
	            int y = tagCompound.getInteger("baseY");
	            int z = tagCompound.getInteger("baseZ");
	            base = new Spot(world, x, y, z);
            }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            
            tagCompound.setByte("color", color);
            if(base != null) {
	            tagCompound.setInteger("baseWorld", base.world);
	            tagCompound.setInteger("baseX", base.x);
	            tagCompound.setInteger("baseY", base.y);
	            tagCompound.setInteger("baseZ", base.z);
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
		if(color == 0 || base == null || (worldObj != null && worldObj.isRemote))
			return;
		NetworkManager.deactivate(base);
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if(isBase() && worldObj != null && !worldObj.isRemote)
			NetworkManager.update(base);
	}

	public boolean isBase() {
		if(color == 0  || worldObj == null)
			return false;
		if(worldObj.getWorldInfo().getDimension() == base.world && xCoord == base.x && yCoord == base.y && zCoord == base.z)
			return true;
		return false;
	}

	public void markForUpdate() {
		worldObj.getChunkFromBlockCoords(xCoord, zCoord).isModified = true;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		
	}
}
