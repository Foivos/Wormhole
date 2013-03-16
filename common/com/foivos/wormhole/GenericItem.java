package com.foivos.wormhole;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 *The class that is used to define Items, for this mod. It is required mainly because it sets the texture file correctly.
 */
public class GenericItem extends Item {

        public GenericItem(int id) {
                super(id);
                maxStackSize = 64;
                setCreativeTab(CreativeTabs.tabMisc);
        }
}