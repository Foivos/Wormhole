package com.foivos.wormhole.transport;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

import com.foivos.wormhole.Wormhole;

import cpw.mods.fml.common.network.PacketDispatcher;

public class TileWormholeManipulator extends TileEntity implements ISidedInventory {

	private ItemStack[][] inv = new ItemStack[6][];
	public byte selected = 0;
	
	public TileWormholeManipulator() {
		for(int i=0; i<6; i++) {
			inv[i] = new ItemStack[21];
		}
	}
	
	@Override
	public int getSizeInventory() {
		return inv[selected].length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inv[selected][index];
	}

	@Override
	public ItemStack decrStackSize(int index, int n) {
		ItemStack stack = getStackInSlot(index);
        if (stack != null) {
                if (stack.stackSize <= n) {
                        setInventorySlotContents(index, null);
                } else {
                        stack = stack.splitStack(n);
                        if (stack.stackSize == 0) {
                                setInventorySlotContents(index, null);
                        }
                }
        }
        return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		ItemStack stack = getStackInSlot(index);
        if (stack != null) {
                setInventorySlotContents(index, null);
        }
        return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if(stack == null) {
			inv[selected][index] = null;
			return;
		}
		if(index == 0) {
			if(stack.itemID != Wormhole.inventoryInterractor.itemID)
				return;
			stack.stackSize = 1;
			inv[selected][index] = stack;
			return;
		}
		inv[selected][index] = stack;
	}

	@Override
	public String getInvName() {
		return "com.foivos.wormhole.TileWormholeManipulator";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openChest() {
		
	}

	@Override
	public void closeChest() {
		
	}

	@Override
	public int getStartInventorySide(ForgeDirection side) {
		return 0;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 0;
	}

	public int getSlotStackLimit(int slot) {
		if(slot == 0)
			return 1;
		if(slot <3)
			return 0;
		return 64;
		
	}

	public boolean isItemValid(int slot, ItemStack stack) {
		switch(slot) {
		case 0:
			return stack.itemID == Wormhole.inventoryInterractor.itemID;
		case 1:
			break;
		case 2:
			break;
		}
		return true;
	}

	public void setSelected(byte selected) {
		if(this.selected == selected)
			return;
		this.selected = selected;
		updateSelected();
	}
	
	public void updateSelected() {
		if(worldObj.isRemote) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
	        DataOutputStream outputStream = new DataOutputStream(bos);
	        try
	        {
	            outputStream.writeInt(worldObj.provider.dimensionId);
	            outputStream.writeInt(xCoord);
	            outputStream.writeInt(yCoord);
	            outputStream.writeInt(zCoord);
	            outputStream.writeByte(selected);
	        }
	        catch (Exception ex)
	        {
	            ex.printStackTrace();
	        }
	 
	        Packet250CustomPayload packet = new Packet250CustomPayload();
	        packet.channel = "WHmanipulator";
	        packet.data = bos.toByteArray();
	        packet.length = bos.size();
	        
	        PacketDispatcher.sendPacketToServer(packet);
		}
		else {	
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
	}
	
	@Override
    public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            
            selected = tagCompound.getByte("selected");
            
            NBTTagList tagList = tagCompound.getTagList("Inventory");
            for (int i = 0; i < tagList.tagCount(); i++) {
                    NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
                    byte b = tag.getByte("Slot");
                    inv[b/21][b%21] = ItemStack.loadItemStackFromNBT(tag);
            }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            
            tagCompound.setByte("selected", selected);
            NBTTagList itemList = new NBTTagList();
            
            
            for (int i=0;i<6;i++) {
            	ItemStack[] stacks = inv[i];
            	for(int j=0;j<stacks.length;j++) {
            		ItemStack stack = stacks[j];
            		if (stack != null) {
                        NBTTagCompound tag = new NBTTagCompound();
                        tag.setByte("Slot", (byte) (i*21+j));
                        stack.writeToNBT(tag);
                        itemList.appendTag(tag);
            		}
            	}
            }
            tagCompound.setTag("Inventory", itemList);
            
    }
	
}
