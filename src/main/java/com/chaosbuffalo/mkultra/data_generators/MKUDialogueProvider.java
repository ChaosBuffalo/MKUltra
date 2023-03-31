package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mkchat.data.DialogueDataProvider;
import com.chaosbuffalo.mkchat.dialogue.*;
import com.chaosbuffalo.mknpc.dialogue.effects.OpenLearnAbilitiesEffect;
import com.chaosbuffalo.mknpc.quest.dialogue.conditions.HasEntitlementCondition;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.MKUEntitlements;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class MKUDialogueProvider extends DialogueDataProvider {

    public MKUDialogueProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void run(@Nonnull HashCache cache) {

        writeDialogue(getAlphaMovePrompt(), cache);
        writeDialogue(getClericAcolyteDefault(), cache);
        writeDialogue(getNetherMageInitiateDefault(), cache);
    }

    private DialogueTree getAlphaMovePrompt(){
        DialogueTree tree = new DialogueTree(new ResourceLocation(MKUltra.MODID, "open_abilities"));
        DialogueNode open_training = new DialogueNode("open_training", "Let me see what I can teach you.");
        open_training.addEffect(new OpenLearnAbilitiesEffect());
        tree.addNode(open_training);

        DialoguePrompt hail = new DialoguePrompt("hail", "", "", "")
                .addResponse(new DialogueResponse("root"));

        DialoguePrompt needTraining =
                new DialoguePrompt("need_training", "need training", "I need training.", "need training?")
                        .addResponse(new DialogueResponse(open_training));

        DialogueNode root = new DialogueNode("root", String.format("Hello %s, welcome to the MKU alpha. Do you %s",
                DialogueContexts.PLAYER_NAME_CONTEXT, needTraining.getPromptEmbed()));
        tree.addNode(root);

        tree.addPrompt(needTraining);
        tree.addPrompt(hail);
        tree.setHailPrompt(hail);

        return tree;
    }

    private DialogueTree getNetherMageInitiateDefault(){
        DialogueTree tree = new DialogueTree(new ResourceLocation(MKUltra.MODID, "intro_nether_mage_initiate"));

        DialogueNode open_training = new DialogueNode("open_training", "Let me see what I can teach you.");
        open_training.addEffect(new OpenLearnAbilitiesEffect());
        DialoguePrompt openTraining = new DialoguePrompt("open_training", "teach me", "Will you teach me?", "teach you");
        DialogueResponse resp = new DialogueResponse(open_training);
        resp.addCondition(new HasEntitlementCondition(MKUEntitlements.IntroNetherMageTier1));
        openTraining.addResponse(new DialogueResponse(open_training));


        DialogueNode guildNode = new DialogueNode("guild_desc", "The Nether Mage's Guild studies the " +
                "Fire and Shadow Magics associated with the Nether dimension. We have guild halls all over the place, I'm surprised you haven't heard of us!");
        DialoguePrompt guildPrompt = new DialoguePrompt("nether_mage_guild", "guild", "What guild?", "the Guild");
        guildPrompt.addResponse(new DialogueResponse(guildNode));

        DialogueNode hail_wo_ability = new DialogueNode("hail_wo",
                String.format("Greetings. I am %s, I've been sent here on a mission for %s. ",
                DialogueContexts.ENTITY_NAME_CONTEXT, guildPrompt.getPromptEmbed()));

        DialogueNode hail_w_ability = new DialogueNode("hail", String.format("Did you want me to %s?.",
                openTraining.getPromptEmbed()));

        DialoguePrompt hailPrompt = new DialoguePrompt("hail", "", "", "");
        DialogueResponse hailWoResp = new DialogueResponse(hail_wo_ability);

        DialogueResponse hailWResp = new DialogueResponse(hail_w_ability);
        hailWResp.addCondition(new HasEntitlementCondition(MKUEntitlements.IntroNetherMageTier1));

        hailPrompt.addResponse(hailWResp);
        hailPrompt.addResponse(hailWoResp);

        tree.addNode(hail_w_ability);
        tree.addNode(hail_wo_ability);
        tree.addNode(open_training);
        tree.addPrompt(hailPrompt);
        tree.addPrompt(openTraining);
        tree.addNode(guildNode);
        tree.addPrompt(guildPrompt);
        tree.setHailPrompt(hailPrompt);
        return tree;
    }

    private DialogueTree getClericAcolyteDefault(){
        DialogueTree tree = new DialogueTree(new ResourceLocation(MKUltra.MODID, "intro_cleric_acolyte"));

        DialogueNode open_training = new DialogueNode("open_training", "Let me see what I can teach you.");
        open_training.addEffect(new OpenLearnAbilitiesEffect());

        DialoguePrompt openTraining = new DialoguePrompt("open_training", "magical abilities", "what magical abilities?", "magical abilities");
        DialogueResponse resp = new DialogueResponse(open_training);
        resp.addCondition(new HasEntitlementCondition(MKUEntitlements.IntroClericTier1));
        openTraining.addResponse(new DialogueResponse(open_training));

        DialogueNode hail_wo_ability = new DialogueNode("hail_wo", String.format("Greetings. I am %s, a humble servant of the Holy See of Solang. " +
                "I've been sent here to investigate the undead uprising.", DialogueContexts.ENTITY_NAME_CONTEXT));

        DialogueNode hail_w_ability = new DialogueNode("hail", String.format("Are you in need of some additional %s to aid your fight against the undead.",
                openTraining.getPromptEmbed()));

        DialoguePrompt hailPrompt = new DialoguePrompt("hail", "", "", "");
        DialogueResponse hailWoResp = new DialogueResponse(hail_wo_ability);

        DialogueResponse hailWResp = new DialogueResponse(hail_w_ability);
        hailWResp.addCondition(new HasEntitlementCondition(MKUEntitlements.IntroClericTier1));

        hailPrompt.addResponse(hailWResp);
        hailPrompt.addResponse(hailWoResp);

        tree.addNode(hail_w_ability);
        tree.addNode(hail_wo_ability);
        tree.addNode(open_training);
        tree.addPrompt(hailPrompt);
        tree.addPrompt(openTraining);
        tree.setHailPrompt(hailPrompt);
        return tree;
    }

    @Override
    public String getName() {
        return "MKU DIALOGUE GEN";
    }
}
