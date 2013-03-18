package com.foivos.wormhole;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import com.foivos.wormhole.transport.BlockWormholeManipulator;
import com.foivos.wormhole.transport.BlockWormholeTube;
import com.foivos.wormhole.transport.TileWormholeManipulator;
import com.foivos.wormhole.transport.TileWormholeTube;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;




@Mod(modid="Wormhole", name="Wormhole", version="0.0.0")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class Wormhole {
	
	public final static Block wormholeTube = new BlockWormholeTube(500);
	public final static Block wormholeManipulator = new BlockWormholeManipulator(501);
	public final static Item wormholeMatter = new Item(5000).setUnlocalizedName("Wormhole:matter").setCreativeTab(CreativeTabs.tabMisc);
	public final static Item wormholeEssence = new Item(5001).setUnlocalizedName("Wormhole:essence").setCreativeTab(CreativeTabs.tabMisc);;
	public final static Item inventoryInterractor = new Item(5002).setUnlocalizedName("Wormhole:interractor").setCreativeTab(CreativeTabs.tabMisc);;
	public final static Item wormholeActivator = new ItemWormholeActivator(5003).setUnlocalizedName("Wormhole:activator").setCreativeTab(CreativeTabs.tabMisc);;
	
	public final static PacketHandler packetHandler = new PacketHandler();
	public final static IConnectionHandler connectionHandler = new ConnectionHandler();
	// The instance of your mod that Forge uses.
    @Instance("Wormhole")
    public static Wormhole instance;
    
    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide="com.foivos.wormhole.ClientProxy", serverSide="com.foivos.wormhole.CommonProxy")
    public static CommonProxy proxy;
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
            // Stub Method
    }
    
    @Init
    public void load(FMLInitializationEvent event) {
            proxy.registerRenderers();
            GameRegistry.registerBlock(wormholeTube, "wormholeTube");
            GameRegistry.registerBlock(wormholeManipulator, "wormholeManipulator");
            GameRegistry.registerTileEntity(TileWormholeTube.class, "tileWormholeTube");
            GameRegistry.registerTileEntity(TileWormholeManipulator.class, "tileWormholeManipulator");
            NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
            NetworkRegistry.instance().registerChannel(packetHandler, "WHmanipulator");
            NetworkRegistry.instance().registerChannel(packetHandler, "testChannel");
            NetworkRegistry.instance().registerConnectionHandler(connectionHandler);
            LanguageRegistry.addName(wormholeTube, "Wormhole Tube");
            LanguageRegistry.addName(wormholeManipulator, "Wormhole Manipulator");
            LanguageRegistry.addName(wormholeEssence, "Wormhole Essence");
            LanguageRegistry.addName(wormholeMatter, "Wormhole Matter");
            LanguageRegistry.addName(inventoryInterractor, "Inventory Interractor");
            LanguageRegistry.addName(wormholeActivator, "Wormhole Activator");
            
            MinecraftForge.EVENT_BUS.register(new WormholeSaveHandler());
            
            addRecipes();
            
    }
    
    private void addRecipes() {
    	
    	GameRegistry.addRecipe(new ItemStack(wormholeMatter), "iei", "bdb", "iei", 
    	        'i', new ItemStack(Item.ingotIron), 'e', new ItemStack(Item.enderPearl), 'b', Item.blazeRod, 'd', Item.diamond);
    	GameRegistry.addSmelting(wormholeMatter.itemID, new ItemStack(wormholeEssence, 8), 0.2f);
    	GameRegistry.addRecipe(new ItemStack(wormholeTube, 4), "owo", 'o', Block.obsidian, 'w', wormholeEssence);
    	
    	List<ItemStack> dyeList = new ArrayList<ItemStack>(); 	
    	Item.dyePowder.getSubItems(Item.dyePowder.itemID, null, dyeList);
    	for(ItemStack stack1 : dyeList) {
    		for(ItemStack stack2 : dyeList) {
    			GameRegistry.addRecipe(new ItemStack(wormholeManipulator), "oao", "rtr", "obo", 'o', Block.obsidian, 't', wormholeTube, 'a',stack1, 'b', stack2, 'r', Item.redstone);
        	}
    	}
    	 
		GameRegistry.addRecipe(new ItemStack(inventoryInterractor), "gbg", "gwg", "gbg", 'g', Item.goldNugget, 'w', wormholeEssence, 'b', Item.blazePowder);
		GameRegistry.addRecipe(new ItemStack(wormholeActivator), "iwi", " i ", " i ", 'i', Item.ingotIron, 'w', wormholeEssence);
    }

	@PostInit
    public void postInit(FMLPostInitializationEvent event) {
            // Stub Method
    }
}
