package com.foivos.wormhole.rendering;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

import com.foivos.wormhole.CommonProxy;
import com.foivos.wormhole.TileManager;
import com.foivos.wormhole.transport.BlockWormholeTube;
import com.foivos.wormhole.transport.TileWormholeTube;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class WormholeTubeRenderer implements ISimpleBlockRenderingHandler{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		TileWormholeTube tile = (TileWormholeTube) TileManager.getTile(world, x, y, z, TileWormholeTube.class, true);
		if(tile == null)
			return false;
		if(!(block instanceof BlockWormholeTube))
			return false;
		byte connections = tile.connections;
		float offset = 0.0625F*3;
		renderer.renderAllFaces = true;
		renderer.setRenderBounds(offset, offset, offset, 1-offset, 1-offset, 1-offset);
		renderer.renderStandardBlock(block, x, y, z);
		if((connections & 1) != 0) {
			renderer.setRenderBounds(offset, 0, offset, 1-offset, offset, 1-offset);
			renderer.renderStandardBlock(block, x, y, z);
		}
		if((connections & 2) != 0) {
			renderer.setRenderBounds(offset, 1-offset, offset, 1-offset, 1, 1-offset);
			renderer.renderStandardBlock(block, x, y, z);
		}
		if((connections & 4) != 0) {
			renderer.setRenderBounds(offset, offset, 0, 1-offset, 1-offset, offset);
			renderer.renderStandardBlock(block, x, y, z);
		}
		if((connections & 8) != 0) {
			renderer.setRenderBounds(offset, offset, 1-offset, 1-offset, 1-offset, 1);
			renderer.renderStandardBlock(block, x, y, z);
		}
		if((connections & 16) != 0) {
			renderer.setRenderBounds(0, offset, offset, offset, 1-offset, 1-offset);
			renderer.renderStandardBlock(block, x, y, z);
		}
		if((connections & 32) != 0) {
			renderer.setRenderBounds(1-offset, offset, offset, 1, 1-offset, 1-offset);
			renderer.renderStandardBlock(block, x, y, z);
		}
        
        return true;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRenderId() {
		return CommonProxy.WORMHOLE_TUBE_RENDER_ID;
	}
	
	
}
