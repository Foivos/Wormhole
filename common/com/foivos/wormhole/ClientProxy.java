package com.foivos.wormhole;

import com.foivos.wormhole.rendering.WormholeTubeRenderer;

import cpw.mods.fml.client.registry.RenderingRegistry;


/**
 * The client side proxy, that handles renderring
 */
public class ClientProxy extends CommonProxy {
	
	WormholeTubeRenderer wormholeTubeRendered = new WormholeTubeRenderer();

	@Override
    public void registerRenderers() {
            RenderingRegistry.registerBlockHandler(wormholeTubeRendered);
    }
	

}