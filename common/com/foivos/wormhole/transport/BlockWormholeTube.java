package com.foivos.wormhole.transport;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.foivos.wormhole.CommonProxy;
import com.foivos.wormhole.TileManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWormholeTube extends BlockWormhole{

	public Icon[] tubeIcons = new Icon[9];
	
	public BlockWormholeTube(int id) {
		super(id);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileWormholeTube();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void func_94332_a(IconRegister register) {
		for(int i=0;i<9;i++) {
			tubeIcons[i] = register.func_94245_a("Wormhole:tubeFace"+i);
		}
		
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta) {
		return tubeIcons[0];
	}
	
	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		TileWormholeTube tile = (TileWormholeTube) TileManager.getTile(world, x, y, z, TileWormholeTube.class, true);
		return tubeIcons[tile.color];
	}
	
	
	public boolean renderAsNormalBlock()
    {
        return false;
    }
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
	@Override
    public int getRenderType()
    {
        return CommonProxy.WORMHOLE_TUBE_RENDER_ID;
    }
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id) {
		super.onNeighborBlockChange(world, x, y, z, id);
		TileWormholeTube tile = (TileWormholeTube) TileManager.getTile(world, x, y, z, TileWormholeTube.class, true);
		if(tile == null)
			return;
		tile.updateConnections();
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		TileWormholeTube tile = (TileWormholeTube) TileManager.getTile(world, x, y, z, TileWormholeTube.class, true);
		if(tile == null)
			return;
		tile.updateConnections();
	}
	

}
