package com.foivos.wormhole;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.foivos.wormhole.networking.NetworkManager;
import com.foivos.wormhole.networking.WormholeNetwork;
import com.foivos.wormhole.transport.TileWormhole;

public class ItemWormholeActivator extends Item {

	public ItemWormholeActivator(int id) {
		super(id);
	}
	
	private static byte color = 1;
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int par7, float par8, float par9, float par10) {
		if(world.isRemote)
			return false;
		TileWormhole tile = (TileWormhole) TileManager.getTile(world, x, y ,z, TileWormhole.class, true);
		if (tile == null || !(tile instanceof TileWormhole))
			return false;
		if(tile.color == 0 || tile.base == null) {
			NetworkManager.putNetwork(new Spot(world.getWorldInfo().getDimension(), x, y, z), new WormholeNetwork(new Spot(world, x, y, z), color));
			color = (byte) ((color+1)%9);
		}
		else {
			NetworkManager.deactivate(tile.base);
		}
		return true;
	}

}
