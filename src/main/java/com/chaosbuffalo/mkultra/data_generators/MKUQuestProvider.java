package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mkchat.dialogue.DialogueNode;
import com.chaosbuffalo.mkchat.dialogue.DialoguePrompt;
import com.chaosbuffalo.mkchat.dialogue.DialogueResponse;
import com.chaosbuffalo.mknpc.data.QuestDefinitionProvider;
import com.chaosbuffalo.mknpc.dialogue.effects.OpenLearnAbilitiesEffect;
import com.chaosbuffalo.mknpc.quest.Quest;
import com.chaosbuffalo.mknpc.quest.QuestDefinition;
import com.chaosbuffalo.mknpc.quest.dialogue.conditions.HasSpentTalentPointsCondition;
import com.chaosbuffalo.mknpc.quest.dialogue.conditions.HasTrainedAbilitiesCondition;
import com.chaosbuffalo.mknpc.quest.dialogue.conditions.HasWeaponInHandCondition;
import com.chaosbuffalo.mknpc.quest.dialogue.conditions.ObjectivesCompleteCondition;
import com.chaosbuffalo.mknpc.quest.dialogue.effects.AdvanceQuestChainEffect;
import com.chaosbuffalo.mknpc.quest.dialogue.effects.GrantEntitlementEffect;
import com.chaosbuffalo.mknpc.quest.dialogue.effects.ObjectiveCompleteEffect;
import com.chaosbuffalo.mknpc.quest.objectives.KillNpcDefObjective;
import com.chaosbuffalo.mknpc.quest.objectives.LootChestObjective;
import com.chaosbuffalo.mknpc.quest.objectives.TalkToNpcObjective;
import com.chaosbuffalo.mknpc.quest.rewards.XpReward;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.green_knight.NaturesRemedyAbility;
import com.chaosbuffalo.mkultra.abilities.green_knight.SkinLikeWoodAbility;
import com.chaosbuffalo.mkultra.init.MKUEntitlements;
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
        DialogueNode hailNode = new DialogueNode("hail", String.format("Hail and well met. " +
                "You're lucky we were able to grab you before your soul drifted too far into the aether. %s in an archival zone?",
                startQuestPrompt.getPromptEmbed()));
        DialogueNode questStart = new DialogueNode("start_quest", "This world is on the verge of deletion, the dead rise from the ground everywhere, " +
                "there may still be time to save it if we act now. " +
                "We're in need of another hero: go talk to our smith and get equipped.");
        def.setHailPrompt(startQuestPrompt);
        def.setStartQuestResponse(questStart);
        def.setStartQuestHail(hailNode);


        Quest talk1 = new Quest("talk_to_smith", new StringTextComponent("The Green Lady wants you to go talk to the smith and equip yourself for an unknown task."));
        talk1.setAutoComplete(true);
        TalkToNpcObjective talkObj = new TalkToNpcObjective(
                "talk_to_smith",
                MKUWorldGen.INTRO_CASTLE_NAME, 0,
                new ResourceLocation("mkultra", "green_smith"),
                new StringTextComponent("Talk to the smith"));
        DialogueNode smithHail = new DialogueNode("hail", "We ain't got much left after the crash. " +
                "Check that chest over there we got a few things. You can use my crafting table as well. Talk to me again when you have made a weapon.");
        smithHail.addEffect(new ObjectiveCompleteEffect(talkObj.getObjectiveName()));
        talkObj.withHailResponse(smithHail, new DialogueResponse(smithHail.getId()));
        talk1.addObjective(talkObj);
        talk1.addReward(new XpReward(25));
        def.addQuest(talk1);


        Quest lootSmithChest = new Quest("equip_yourself", new StringTextComponent("The Green Smith points you towards a chest in his workshop."));
        LootChestObjective chestObj = new LootChestObjective("loot_chest", MKUWorldGen.INTRO_CASTLE_NAME, "intro_chest", new StringTextComponent("Loot the smith's chest"));
        chestObj.addItemStack(new ItemStack(Blocks.COBBLESTONE, 20));
        chestObj.addItemStack(new ItemStack(Blocks.OAK_PLANKS, 20));
        chestObj.addItemStack(new ItemStack(Items.STRING, 10));
        chestObj.addItemStack(new ItemStack(Items.LEATHER, 40));
        chestObj.addItemStack(new ItemStack(Items.COAL, 10));
        chestObj.addItemStack(new ItemStack(Items.FLINT, 10));
        chestObj.addItemStack(new ItemStack(Items.PORKCHOP, 10));
        lootSmithChest.addObjective(chestObj);
        lootSmithChest.setAutoComplete(true);
        lootSmithChest.addReward(new XpReward(25));
        def.addQuest(lootSmithChest);


        Quest returnToSmith = new Quest("return_to_smith", new StringTextComponent("Use the Green Smith's supplies to craft your desired weapon and perhaps some armor for the battle ahead."));
        returnToSmith.setAutoComplete(true);
        TalkToNpcObjective retSmithTalk = new TalkToNpcObjective(
                "return_to_smith",
                MKUWorldGen.INTRO_CASTLE_NAME, 0,
                new ResourceLocation("mkultra", "green_smith"),
                new StringTextComponent("Talk to the Green Smith with a weapon in your hand."));
        DialogueNode hailWithWeapon = new DialogueNode("hail_w_weapon", "Great, but you're going to need more than just a sharp rock where we're going. " +
                "Go back and talk to the Green Lady, ask her about learning to develop your magical talents.");
        DialogueResponse hailWithWeaponResp = new DialogueResponse(hailWithWeapon.getId());
        hailWithWeapon.addEffect(new ObjectiveCompleteEffect(retSmithTalk.getObjectiveName()));
        hailWithWeaponResp.addCondition(new HasWeaponInHandCondition());
        DialogueNode hailWithoutWeapon = new DialogueNode("hail_wo_weapon", "Come back to me with a weapon in your hand.");
        DialogueResponse hailWithoutWeaponResp = new DialogueResponse(hailWithoutWeapon.getId());
        hailWithoutWeaponResp.addCondition(new HasWeaponInHandCondition().setInvert(true));
        retSmithTalk.withHailResponse(hailWithWeapon, hailWithWeaponResp);
        retSmithTalk.withHailResponse(hailWithoutWeapon, hailWithoutWeaponResp);
        returnToSmith.addObjective(retSmithTalk);
        returnToSmith.addReward(new XpReward(25));
        def.addQuest(returnToSmith);


        Quest greenLadyTrainTalent = new Quest("green_lady_talent",
                new StringTextComponent("Talk to the Green Lady to learn more about developing your magical abilities"));
        greenLadyTrainTalent.setAutoComplete(true);
        TalkToNpcObjective greenLadyTalent = new TalkToNpcObjective(
                "green_lady_talent",
                MKUWorldGen.INTRO_CASTLE_NAME, 0,
                new ResourceLocation("mkultra", "green_lady"),
                new StringTextComponent("Talk to the Green Lady about the talent system."));
        DialogueNode greenLadyTalentConvoStart = new DialogueNode("talent_1","We can help you awaken your magical gifts, the first step is learning how " +
                "to train your talents. You should have gained a talent point upon initiating this conversation. " +
                "Open your player screen and go to the talent section, train any of the first talents in order to unlock your first ability slot. Talk to me again when you have finished this.");
        greenLadyTalentConvoStart.addEffect(new ObjectiveCompleteEffect(greenLadyTalent.getObjectiveName()));
        greenLadyTalent.withHailResponse(greenLadyTalentConvoStart, new DialogueResponse(greenLadyTalentConvoStart.getId()));
        greenLadyTrainTalent.addReward(new XpReward(25));
        greenLadyTrainTalent.addObjective(greenLadyTalent);
        def.addQuest(greenLadyTrainTalent);


        Quest returnToGreenLady = new Quest("return_to_green_lady",
                new StringTextComponent("The Green Lady wants you to learn about spending talent points."));
        returnToGreenLady.setAutoComplete(true);
        TalkToNpcObjective retGreenLady = new TalkToNpcObjective(
                "return_to_green_lady",
                MKUWorldGen.INTRO_CASTLE_NAME, 0,
                new ResourceLocation("mkultra", "green_lady"),
                new StringTextComponent("Talk to the Green Lady after training a talent."));
        DialogueNode open_training = new DialogueNode("open_training",
                "Let me see what I can teach you. Talk to me again when you're done.");
        open_training.addEffect(new OpenLearnAbilitiesEffect());
        retGreenLady.withAdditionalNode(open_training);
        DialoguePrompt needTraining = new DialoguePrompt("need_training", "want to learn",
                "I want to learn.", "ready to learn");
        needTraining.addResponse(new DialogueResponse(open_training.getId()));
        retGreenLady.withAdditionalPrompts(needTraining);
        DialogueNode hailWithTraining = new DialogueNode("hail_w_training", String.format(
                "Alright you're now %s your first ability.", needTraining.getPromptEmbed()));
        DialogueResponse hailWithTrainingResp = new DialogueResponse(hailWithTraining.getId());
        hailWithTraining.addEffect(new ObjectiveCompleteEffect(retGreenLady.getObjectiveName()));
        hailWithTraining.addEffect(new GrantEntitlementEffect(MKUEntitlements.GreenKnightTier1));
        hailWithTrainingResp.addCondition(new HasSpentTalentPointsCondition(1));
        DialogueNode hailWithoutTraining = new DialogueNode("hail_wo_training",
                "Come back to me when you have spent your first talent point.");
        DialogueResponse hailWithoutTrainingResp = new DialogueResponse(hailWithoutTraining.getId());
        hailWithoutTrainingResp.addCondition(new HasSpentTalentPointsCondition(1).setInvert(true));
        retGreenLady.withHailResponse(hailWithTraining, hailWithTrainingResp);
        retGreenLady.withHailResponse(hailWithoutTraining, hailWithoutTrainingResp);
        returnToGreenLady.addObjective(retGreenLady);
        returnToGreenLady.addReward(new XpReward(50));
        def.addQuest(returnToGreenLady);


        Quest afterAbility = new Quest("after_ability",
                new StringTextComponent("Talk to the Green Lady and learn your first ability, then speak to her again."));
        afterAbility.setAutoComplete(true);
        TalkToNpcObjective afterGreenLady = new TalkToNpcObjective(
                "after_green_lady",
                MKUWorldGen.INTRO_CASTLE_NAME, 0,
                new ResourceLocation("mkultra", "green_lady"),
                new StringTextComponent("Talk to the Green Lady after learning your first ability."));
        DialogueNode hailWithAbilities = new DialogueNode("hail_w_ability", "Now we must test your mettle in combat. " +
                "Go kill some of the zombies on the first floor to try out your new magic, and don't forget you can always return to me to learn more.");
        afterGreenLady.withAdditionalPrompts(needTraining);
        afterGreenLady.withAdditionalNode(open_training);
        DialogueResponse hailWithAbilityResp = new DialogueResponse(hailWithAbilities.getId());
        hailWithAbilities.addEffect(new ObjectiveCompleteEffect(afterGreenLady.getObjectiveName()));
        hailWithAbilityResp.addCondition(new HasTrainedAbilitiesCondition(false, SkinLikeWoodAbility.INSTANCE.getAbilityId(), NaturesRemedyAbility.INSTANCE.getAbilityId()));
        DialogueNode hailWithoutAbilities = new DialogueNode("hail_wo_abilities", "Come back to me once you've learned one of our abilities.");
        DialogueResponse hailWithoutAbilitiesResp = new DialogueResponse(hailWithoutAbilities.getId());
        hailWithoutAbilitiesResp.addCondition(new HasWeaponInHandCondition().setInvert(true));
        afterGreenLady.withHailResponse(hailWithAbilities, hailWithAbilityResp);
        afterGreenLady.withHailResponse(hailWithoutAbilities, hailWithoutAbilitiesResp);
        afterAbility.addObjective(afterGreenLady);
        afterAbility.addReward(new XpReward(50));
        def.addQuest(afterAbility);


        Quest killQuest = new Quest("first_kill",
                new StringTextComponent("The Green Lady wants you to clear out some of the zombies on the first floor of the castle"));
        killQuest.setAutoComplete(true);
        KillNpcDefObjective killObjective = new KillNpcDefObjective("kill_zombies",
                new ResourceLocation(MKUltra.MODID, "decaying_piglin"), 4);
        KillNpcDefObjective killArcherObjective = new KillNpcDefObjective("kill_archers",
                new ResourceLocation(MKUltra.MODID, "decaying_piglin_archer"), 4);
        killQuest.addObjective(killObjective);
        killQuest.addObjective(killArcherObjective);
        killQuest.addReward(new XpReward(50));
        TalkToNpcObjective talkAfterKill = new TalkToNpcObjective(
                "after_kill",
                MKUWorldGen.INTRO_CASTLE_NAME, 0,
                new ResourceLocation("mkultra", "green_lady"),
                new StringTextComponent("Talk to the Green Lady after completing the other objectives.")
        );
        DialogueNode talkAfterKillComplete = new DialogueNode("hail_w_kills", "Good: it is done. " +
                "The dead rise everywhere, to cull the damned is a blessed pursuit. Our order is dedicated to cleansing " +
                "this land. You are welcome to stay here and learn of our ways or go as you please.");
        DialogueResponse talkAfterKillCompleteResp = new DialogueResponse(talkAfterKillComplete.getId());
        talkAfterKillComplete.addEffect(new ObjectiveCompleteEffect(talkAfterKill.getObjectiveName()));
        talkAfterKillCompleteResp.addCondition(new ObjectivesCompleteCondition(killObjective.getObjectiveName(), killArcherObjective.getObjectiveName()));
        DialogueNode withoutKill = new DialogueNode("hail_wo_kill", "Come back to me after you've proven yourself.");
        DialogueResponse withoutKillResp = new DialogueResponse(withoutKill.getId());
        withoutKillResp.addCondition(new ObjectivesCompleteCondition(killObjective.getObjectiveName(), killArcherObjective.getObjectiveName()).setInvert(true));
        talkAfterKill.withHailResponse(talkAfterKillComplete, talkAfterKillCompleteResp);
        talkAfterKill.withHailResponse(withoutKill, withoutKillResp);
        killQuest.addObjective(talkAfterKill);
        def.addQuest(killQuest);

        return def;
    }
}
