package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mkchat.data.DialogueDataProvider;
import com.chaosbuffalo.mkchat.dialogue.*;
import com.chaosbuffalo.mknpc.dialogue.effects.OpenLearnAbilitiesEffect;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class MKUDialogueProvider extends DialogueDataProvider {

    public MKUDialogueProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void act(@Nonnull DirectoryCache cache) {
        writeDialogue(getAlphaMovePrompt(), cache);
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

    @Override
    public String getName() {
        return "MKU DIALOGUE GEN";
    }
}
