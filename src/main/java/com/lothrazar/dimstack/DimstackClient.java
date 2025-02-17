package com.lothrazar.dimstack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DimstackClient {

	@SubscribeEvent
	public void models(ModelRegistryEvent e) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(DimstackMod.PORTAL), 0, new ModelResourceLocation(DimstackMod.PORTAL.getRegistryName(), "normal"));
		for (int i = 0; i < 20; i++)
			ModelLoader.setCustomModelResourceLocation(DimstackMod.KEY, i, new ModelResourceLocation(DimstackMod.KEY.getRegistryName(), "inventory"));
	}

	@SubscribeEvent
	public void colors(ColorHandlerEvent.Item e) {
		e.getItemColors().registerItemColorHandler((s, m) -> {
			if (DimstackMod.config.dimKeyColors.containsKey(s.getMetadata())) {
				return DimstackMod.config.dimKeyColors.get(s.getMetadata());
			}
			return -1;
		}, DimstackMod.KEY);
	}

	@SubscribeEvent
	public void colors(ColorHandlerEvent.Block e) {
		e.getBlockColors().registerBlockColorHandler((state, world, pos, tint) -> {
			int dim = Minecraft.getMinecraft().world.provider.getDimension();
			if (DimstackMod.config.dimPortalColors.containsKey(dim)) {
				int[] colors = DimstackMod.config.dimPortalColors.get(dim);
				return colors[pos.getY() < 50 ? 0 : 1];
			}
			return -1;
		}, DimstackMod.PORTAL);
	}

}
