package com.chaosbuffalo.mkultra.data_generators;


import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.MKUItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;


public class MKUItemModelProvider extends ItemModelProvider {


    public MKUItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MKUltra.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        makeSimpleItem(MKUItems.corruptedPigIronPlate);
        makeSimpleItem(MKUItems.greenKnightHelmet);
        makeSimpleItem(MKUItems.greenKnightChestplate);
        makeSimpleItem(MKUItems.greenKnightBoots);
        makeSimpleItem(MKUItems.greenKnightLeggings);
        makeSimpleItem(MKUItems.trooperKnightBoots);
        makeSimpleItem(MKUItems.trooperKnightChestplate);
        makeSimpleItem(MKUItems.trooperKnightHelmet);
        makeSimpleItem(MKUItems.trooperKnightLeggings);
        makeSimpleItem(MKUItems.destroyedTrooperBoots);
        makeSimpleItem(MKUItems.destroyedTrooperChestplate);
        makeSimpleItem(MKUItems.destroyedTrooperLeggings);
        makeSimpleItem(MKUItems.destroyedTrooperHelmet);
    }

    private void makeSimpleItem(Item item) {
        String path = ForgeRegistries.ITEMS.getKey(item).getPath();

        ItemModelBuilder builder = getBuilder(path)
                .parent(getExistingFile(new ResourceLocation("item/generated")))
                .texture("layer0", modLoc(String.format("items/%s", path)));
    }
}
