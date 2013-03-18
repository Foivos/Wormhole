package com.foivos.wormhole;

import java.lang.management.GarbageCollectorMXBean;

import com.foivos.wormhole.networking.NetworkManager;
import com.foivos.wormhole.networking.TileNetwork;
import com.foivos.wormhole.networking.WormholeNetwork;

import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemWormholeActivator extends Item {

	public ItemWormholeActivator(int id) {
		super(id);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int par7, float par8, float par9, float par10) {
		TileEntity t = world.getBlockTileEntity(x, y, z);
		if (t == null || !(t instanceof TileNetwork))
			return false;
		NetworkManager.toggleNetwork(world, x, y, z);
		return true;
	}

}
