package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mkchat.dialogue.DialogueNode;
import com.chaosbuffalo.mkchat.dialogue.DialoguePrompt;
import com.chaosbuffalo.mkchat.dialogue.DialogueResponse;
import com.chaosbuffalo.mknpc.data.QuestDefinitionProvider;
import com.chaosbuffalo.mknpc.quest.Quest;
import com.chaosbuffalo.mknpc.quest.QuestDefinition;
import com.chaosbuffalo.mknpc.quest.dialogue.effects.AdvanceQuestChainEffect;
import com.chaosbuffalo.mknpc.quest.dialogue.effects.ObjectiveCompleteEffect;
import com.chaosbuffalo.mknpc.quest.objectives.LootChestObjective;
import com.chaosbuffalo.mknpc.quest.objectives.TalkToNpcObjective;
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
        writeDefinition(generateIntroQuest(), cache);
    }

    private QuestDefinition generateIntroQuest(){

        QuestDefinition def = new QuestDefinition(new ResourceLocation(MKUltra.MODID, "intro_quest"));
        def.setQuestName(new StringTextComponent("Intro Quest"));
        DialoguePrompt startQuestPrompt = new DialoguePrompt("start_quest", "don't know",
                "I don't know", "What are you doing");
        startQuestPrompt.addResponse(new DialogueResponse("start_quest"));
        DialogueNode hailNode = new DialogueNode("hail", String.format("Welcome to this cursed ground, " +
                "you're lucky we were able to grab you before you drifted too far into the aether. %s in an archival zone?",
                startQuestPrompt.getPromptEmbed()));
        DialogueNode questStart = new DialogueNode("start_quest", "I can explain more later. " +
                "We gotta take care of these zombies; go talk to our smith and get equipped.");
        def.setHailPrompt(startQuestPrompt);
        def.setStartQuestResponse(questStart);
        def.setStartQuestHail(hailNode);
        Quest talk1 = new Quest("talk_to_smith");
        talk1.setAutoComplete(true);
        TalkToNpcObjective talkObj = new TalkToNpcObjective("talk_to_smith", MKUWorldGen.INTRO_CASTLE_NAME, 0,
                new ResourceLocation("mkultra", "green_smith"), new StringTextComponent("Talk to the smith"));
        DialogueNode smithHail = new DialogueNode("hail", "We ain't got much left after the crash. " +
                "Check that chest over there we got a few things. We're going to need you to 'gather' up some supplies to put together anything fancier.");
        smithHail.addEffect(new ObjectiveCompleteEffect("talk_to_smith"));
        talkObj.withHailResponse(smithHail);
        talk1.addObjective(talkObj);
        def.addQuest(talk1);



        Quest quest = new Quest("equip_yourself");
        LootChestObjective chestObj = new LootChestObjective("loot_chest", MKUWorldGen.INTRO_CASTLE_NAME, "intro_chest", new StringTextComponent("Loot the smith's chest"));
        chestObj.addItemStack(new ItemStack(Blocks.COBBLESTONE, 20));
        chestObj.addItemStack(new ItemStack(Blocks.OAK_PLANKS, 20));
        chestObj.addItemStack(new ItemStack(Items.STRING, 10));
        chestObj.addItemStack(new ItemStack(Items.LEATHER, 40));
        chestObj.addItemStack(new ItemStack(Items.COAL, 10));
        chestObj.addItemStack(new ItemStack(Items.FLINT, 10));
        chestObj.addItemStack(new ItemStack(Items.PORKCHOP, 10));
        quest.addObjective(chestObj);
        def.addQuest(quest);
        return def;
    }
}
