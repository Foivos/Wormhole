package com.foivos.wormhole.transport;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import com.foivos.wormhole.CommonProxy;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class WormholeTubeRenderer implements ISimpleBlockRenderingHandler{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		int x=0, y=0, z=0;
		Tessellator tes = Tessellator.instance;
		renderer.setRenderBounds(block.getBlockBoundsMinX(),0,block.getBlockBoundsMinZ(),block.getBlockBoundsMaxX(),1,block.getBlockBoundsMaxZ());
        tes.startDrawingQuads();
        tes.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderBottomFace(block, x,y,z,0);
		tes.draw();
		tes.startDrawingQuads();
        tes.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderTopFace(block, x,y,z,0);
		tes.draw();
		tes.startDrawingQuads();
        tes.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderEastFace(block, x,y,z,3);
		tes.draw();
		tes.startDrawingQuads();
        tes.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderWestFace(block, x,y,z,3);
		tes.draw();
		tes.startDrawingQuads();
        tes.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderNorthFace(block, x,y,z,3);
		tes.draw();
		tes.startDrawingQuads();
        tes.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderSouthFace(block, x,y,z,3);
		tes.draw();
	}
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {		
		renderer.setRenderBounds(0, block.getBlockBoundsMinY(),0, 1, 1, 1);
		renderer.renderBottomFace(block, x,y,z,block.getBlockTexture(world, x, y, z, 0));
		renderer.setRenderBounds(0, 0, 0, 1,block.getBlockBoundsMaxY(), 1);
		renderer.renderTopFace(block, x,y,z,block.getBlockTexture(world, x, y, z, 1));
		renderer.setRenderBounds(0, 0, block.getBlockBoundsMinZ(), 1, 1, 1);
		renderer.renderEastFace(block, x,y,z,block.getBlockTexture(world, x, y, z, 2));
		renderer.setRenderBounds(0, 0, 0, 1, 1, block.getBlockBoundsMaxZ());
		renderer.renderWestFace(block, x,y,z,block.getBlockTexture(world, x, y, z, 3));
		renderer.setRenderBounds(block.getBlockBoundsMinX(), 0, 0, 1, 1, 1);
		renderer.renderNorthFace(block, x,y,z,block.getBlockTexture(world, x, y, z, 4));
		renderer.setRenderBounds(0, 0, 0, block.getBlockBoundsMaxX(), 1, 1);
		renderer.renderSouthFace(block, x,y,z,block.getBlockTexture(world, x, y, z, 5));
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return CommonProxy.WORMHOLE_TUBE_RENDER_ID;
	}

}
