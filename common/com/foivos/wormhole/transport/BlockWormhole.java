package com.foivos.wormhole.transport;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.foivos.wormhole.Place;
import com.foivos.wormhole.TileManager;
import com.foivos.wormhole.networking.NetworkManager;
import com.foivos.wormhole.networking.WormholeNetwork;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *The Wormhole Structure block. 
 */
public class BlockWormhole extends BlockContainer
{

	
	public BlockWormhole (int id) {
        super(id, Material.rock);
        setHardness(3.0F);
        setResistance(5.0F);
        setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
    public TileEntity createNewTileEntity(World world) {
            return new TileWormhole();
    }
	

	


	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id) {
		super.onNeighborBlockChange(world, x, y, z, id);
		if(world.isRemote)
			return;
		TileWormhole tile = (TileWormhole) TileManager.getTile(world, x, y, z, TileWormhole.class, true);
		if(tile == null || tile.color == 0 || tile.base == null)
			return;

		WormholeNetwork network = NetworkManager.getNetwork(tile.base);
		if(network != null)
			network.changed(new Place(world, x, y ,z));
	}
	
	
	
	
	

}
