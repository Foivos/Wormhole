package com.foivos.wormhole.transport;


import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.foivos.wormhole.Wormhole;
import com.foivos.wormhole.networking.ItemSet;

public class TileWormholeManipulator extends TileWormhole implements ISidedInventory {
	
	public static final int SIZE = 21;
	public static final int ITEM_START = 3;
	private ItemStack[][] inv = new ItemStack[6][];
	private boolean[] inclGet = {true, true, true, true, true, true};
	private boolean[] inclPut = {true, true, true, true, true, true};
	
	public TileWormholeManipulator() {
		super();
		for(int i=0; i<6; i++) {
			inv[i] = new ItemStack[SIZE];
		}
	}
	
	@Override
	public int getSizeInventory() {
		return SIZE*6;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inv[index/21][index%21];
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
			inv[index/SIZE][index%SIZE] = null;
			return;
		}
		if(index == 0) {
			if(stack.itemID != Wormhole.inventoryInterractor.itemID)
				return;
			stack.stackSize = 1;
			inv[index/SIZE][index%SIZE] = stack;
			return;
		}
		inv[index/21][index%21] = stack;
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
	
	@Override
    public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            
            NBTTagList tagList = tagCompound.getTagList("Inventory");
            for (int i = 0; i < tagList.tagCount(); i++) {
                    NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
                    byte b = tag.getByte("Slot");
                    inv[b/SIZE][b%SIZE] = ItemStack.loadItemStackFromNBT(tag);
            }
            tagList = tagCompound.getTagList("incl");
            for(int i=0;i < tagList.tagCount() ; i++) {
            	NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
            	byte b= tag.getByte("incl");
            	int side = b>>2;
            	inclPut[side] = (b & 1) == 1;
            	inclGet[side] = (b & 2) == 2;
            }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            
            NBTTagList itemList = new NBTTagList();
            for (int i=0;i<6*SIZE;i++) {
        		ItemStack stack = inv[i/SIZE][i%SIZE];
        		if (stack != null) {
                    NBTTagCompound tag = new NBTTagCompound();
                    tag.setByte("Slot", (byte) i);
                    stack.writeToNBT(tag);
                    itemList.appendTag(tag);
        		}
            	
            }
            tagCompound.setTag("Inventory", itemList);
            NBTTagList inclList = new NBTTagList();
            for(int i=0;i<6;i++) {
            	if(inclPut[i] || inclGet[i]) {
            		NBTTagCompound tag = new NBTTagCompound();
            		byte b = (byte) (((inclPut[i] ? 1 : 0) + (inclGet[i] ? 2 : 0) + (i << 2)) & 31);
            		tag.setByte("incl", (byte) b);
            		inclList.appendTag(tag);
            	}
            }
            tagCompound.setTag("incl", inclList);
            
    }

	public void dropItems(int start, int end) {
		Random random = new Random();
		for(int i=start;i<end;i++) {
			ItemStack stack = getStackInSlot(i);

            if (stack != null && ! worldObj.isRemote)
            {
                float xOffset = random.nextFloat() * 0.8F + 0.1F;
                float zOffset = random.nextFloat() * 0.8F + 0.1F;
                EntityItem entityItem;

                for (float yOffset = random.nextFloat() * 0.8F + 0.1F; stack.stackSize > 0; worldObj.spawnEntityInWorld(entityItem))
                {
                    int dropSize = random.nextInt(21) + 10;

                    if (dropSize > stack.stackSize)
                    	dropSize = stack.stackSize;

                    stack.stackSize -= dropSize;
                    entityItem = new EntityItem(worldObj, (double)((float)xCoord + xOffset), (double)((float)yCoord + zOffset), (double)((float)zCoord + yOffset), new ItemStack(stack.itemID, dropSize, stack.getItemDamage()));
                    float maxMotion = 0.05F;
                    entityItem.motionX = (double)((float)random.nextGaussian() * maxMotion);
                    entityItem.motionY = (double)((float)random.nextGaussian() * maxMotion + 0.2F);
                    entityItem.motionZ = (double)((float)random.nextGaussian() * maxMotion);

                    if (stack.hasTagCompound())
                    {
                    	entityItem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
                    }
                }
            }
            setInventorySlotContents(i, null);
		}
		
	}

	@Override
	public boolean func_94042_c() {
		return false;
	}

	@Override
	public boolean func_94041_b(int side, ItemStack stack) {
		return true;
	}

	@Override
	public int func_94127_c(int i) {
		return 0;
	}

	@Override
	public int func_94128_d(int i) {
		return 0;
	}

	public boolean isPushing(int side, ItemStack stack) {
		if(stack == null)
			return false;
		for(int i=SIZE-9;i<SIZE;i++) {
			if(inv[side][i] != null && stack.isItemEqual(inv[side][i]))
				return true;
		}
		return false;
	}

	public void setInclPut(byte i, boolean inclPut) {
		this.inclPut[i] = inclPut;
		markForUpdate();
	}
	public void setInclGet(byte i, boolean inclGet) {
		this.inclGet[i] = inclGet;
		markForUpdate();
	}

	public void togglePut(byte i) {
		setInclPut(i, inclPut[i]^true);
	}

	public void toggleGet(byte i) {
		setInclGet(i, inclGet[i]^true);
	}

	public boolean getInclPut(byte i) {
		return inclPut[i];
	}
	
	public boolean getInclGet(byte i) {
		return inclGet[i];
	}

	public void writeSets(ItemSet putting, ItemSet getting, int side) {
		putting.incl = inclPut[side];
		putting.items.clear();
		for(int i=ITEM_START;i<ITEM_START+9;i++) {
			ItemStack stack = inv[side][i];
			if(stack != null);
			putting.items.add(stack);
		}
		getting.incl = inclGet[side];
		getting.items.clear();
		for(int i=ITEM_START+9;i<ITEM_START+18;i++) {
			ItemStack stack = inv[side][i];
			if(stack != null);
			getting.items.add(stack);
		}
		
	}

	public boolean isPuller() {
		// TODO add puller module
		return false;
	}



	
}
