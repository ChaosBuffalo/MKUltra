package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mknpc.data.QuestDefinitionProvider;
import com.chaosbuffalo.mknpc.quest.Quest;
import com.chaosbuffalo.mknpc.quest.QuestDefinition;
import com.chaosbuffalo.mknpc.quest.objectives.LootChestObjective;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.MKUWorldGen;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.io.IOException;

public class MKUQuestProvider extends QuestDefinitionProvider {


    public MKUQuestProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        writeDefinition(generateTestQuest(), cache);
    }

    private QuestDefinition generateTestQuest(){

        QuestDefinition def = new QuestDefinition(new ResourceLocation(MKUltra.MODID, "quest.test"));
        Quest quest = new Quest("test_loot");
        LootChestObjective chestObj = new LootChestObjective("loot_chest", MKUWorldGen.CRYPT_NAME, "test", new StringTextComponent("Loot the test chest"));
        chestObj.addItemStack(new ItemStack(Blocks.COBBLESTONE, 10));
        chestObj.addItemStack(new ItemStack(Blocks.OAK_PLANKS, 10));
        chestObj.addItemStack(new ItemStack(Items.STRING, 10));
        chestObj.addItemStack(new ItemStack(Items.LEATHER, 20));
        chestObj.addItemStack(new ItemStack(Items.COAL, 10));
        quest.addObjective(chestObj);
        def.addQuest(quest);
        return def;
    }
}
