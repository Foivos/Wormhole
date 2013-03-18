package com.foivos.wormhole.transport;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.foivos.wormhole.CommonProxy;
import com.foivos.wormhole.Wormhole;
import com.foivos.wormhole.networking.NetworkManager;
import com.foivos.wormhole.networking.TileNetwork;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * The Wormhole Manipulator block.
 */
public class BlockWormholeManipulator extends BlockContainer {

	public Icon[] icons = new Icon[12];

	public BlockWormholeManipulator(int id) {
		super(id, Material.rock);
		setHardness(3.0F);
		setResistance(5.0F);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileWormholeManipulator();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int idk, float what, float these, float are) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile == null || !(tile instanceof TileWormholeManipulator) || ((TileWormholeManipulator)tile).activated) 
			return false;
		if(player.isSneaking() || (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().itemID == Wormhole.wormholeActivator.itemID))
			return false;
		player.openGui(Wormhole.instance, 0, world, x, y, z);
		return true;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id) {
		super.onNeighborBlockChange(world, x, y, z, id);
		TileNetwork tile = (TileNetwork) world.getBlockTileEntity(x, y, z);
		if(tile.activated)
			NetworkManager.validate(tile.network, tile.worldObj, x, y ,z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void func_94332_a(IconRegister register) {
		for (int i = 0; i < 12; i++) {
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
		TileWormholeManipulator tile = (TileWormholeManipulator) blockAccess.getBlockTileEntity(x, y, z);
		return icons[side + (tile.activated ? 6 : 0)];
		
	}
}
