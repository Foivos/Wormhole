package com.foivos.wormhole;

import net.minecraftforge.client.MinecraftForgeClient;

import com.foivos.wormhole.transport.WormholeTubeRenderer;

import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * The client side proxy,, that handles renderring
 */
public class ClientProxy extends CommonProxy {
	
	WormholeTubeRenderer wormholeTubeRendered = new WormholeTubeRenderer();
	
	@Override
    public void registerRenderers() {
            MinecraftForgeClient.preloadTexture(ITEMS_PNG);
            MinecraftForgeClient.preloadTexture(BLOCK_PNG);
            MinecraftForgeClient.preloadTexture(WORMHOLE_TUBE_PNG);
            RenderingRegistry.registerBlockHandler(wormholeTubeRendered);
    }
}