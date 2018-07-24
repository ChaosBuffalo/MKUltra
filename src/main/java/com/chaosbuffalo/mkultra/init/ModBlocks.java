package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber
public final class ModBlocks {

    public static Block xpTableBlock;


    // can't be public because this is an ObjectHolder
    private static final Set<Block> ALL_BLOCKS = new HashSet<>();

    static {
        regInternal(xpTableBlock = new XpTableBlock(
                "xpTable", Material.ANVIL, 5.0f, 1000.0f)
                .setLightLevel(1.0f)
                .setCreativeTab(MKUltra.MKULTRA_TAB));
    }

    private static void regInternal(Block block) {
        ALL_BLOCKS.add(block);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ALL_BLOCKS.forEach(event.getRegistry()::register);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        ALL_BLOCKS.stream().filter(block -> block.getRegistryName() != null)
                .map((block -> new ItemBlock(block).setRegistryName(block.getRegistryName())))
                .forEach((item -> event.getRegistry().register(item)));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerItemBlockModels(ModelRegistryEvent event) {
        ALL_BLOCKS.stream()
                .filter(b -> b.getRegistryName() != null)
                .forEach(block ->
                        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
                                new ModelResourceLocation(block.getRegistryName(), "inventory")));
    }
}
