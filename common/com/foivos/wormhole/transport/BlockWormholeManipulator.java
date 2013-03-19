package com.foivos.wormhole.transport;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.foivos.wormhole.Place;
import com.foivos.wormhole.TileManager;
import com.foivos.wormhole.Wormhole;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * The Wormhole Manipulator block.
 */
public class BlockWormholeManipulator extends BlockWormhole {

	public Icon[] icons = new Icon[12];

	public BlockWormholeManipulator(int id) {
		super(id);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileWormholeManipulator();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int idk, float what, float these, float are) {
		TileWormholeManipulator tile = (TileWormholeManipulator) TileManager.getTile(world, x, y, z, TileWormholeManipulator.class, true);
		if (tile == null)
			return false;
		if(player.isSneaking() || (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().itemID == Wormhole.wormholeActivator.itemID))
			return false;
		player.openGui(Wormhole.instance, 0, world, x, y, z);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void func_94332_a(IconRegister register) {
		for (int i = 0; i < 54; i++) {
			icons[i] = register.func_94245_a("Wormhole:manipulator" + i);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta) {
		return icons[side];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		TileWormholeManipulator tile = (TileWormholeManipulator) TileManager.getTile(blockAccess, x, y, z, TileWormholeManipulator.class, true);
		return icons[side + tile.color*6];
		
	}
	
	
}
