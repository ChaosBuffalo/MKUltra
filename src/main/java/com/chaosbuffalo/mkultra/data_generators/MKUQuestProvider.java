package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mkchat.dialogue.*;
import com.chaosbuffalo.mkchat.dialogue.conditions.DialogueCondition;
import com.chaosbuffalo.mknpc.data.QuestDefinitionProvider;
import com.chaosbuffalo.mknpc.dialogue.effects.OpenLearnAbilitiesEffect;
import com.chaosbuffalo.mknpc.quest.Quest;
import com.chaosbuffalo.mknpc.quest.QuestDefinition;
import com.chaosbuffalo.mknpc.quest.dialogue.NpcDialogueUtils;
import com.chaosbuffalo.mknpc.quest.dialogue.conditions.HasSpentTalentPointsCondition;
import com.chaosbuffalo.mknpc.quest.dialogue.conditions.HasTrainedAbilitiesCondition;
import com.chaosbuffalo.mknpc.quest.dialogue.conditions.HasWeaponInHandCondition;
import com.chaosbuffalo.mknpc.quest.dialogue.conditions.ObjectivesCompleteCondition;
import com.chaosbuffalo.mknpc.quest.dialogue.effects.ObjectiveCompleteEffect;
import com.chaosbuffalo.mknpc.quest.objectives.*;
import com.chaosbuffalo.mknpc.quest.requirements.HasEntitlementRequirement;
import com.chaosbuffalo.mknpc.quest.rewards.GrantEntitlementReward;
import com.chaosbuffalo.mknpc.quest.rewards.MKLootReward;
import com.chaosbuffalo.mknpc.quest.rewards.QuestReward;
import com.chaosbuffalo.mknpc.quest.rewards.XpReward;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.green_knight.NaturesRemedyAbility;
import com.chaosbuffalo.mkultra.abilities.green_knight.SkinLikeWoodAbility;
import com.chaosbuffalo.mkultra.init.MKUEntitlements;
import com.chaosbuffalo.mkultra.init.MKUItems;
import com.chaosbuffalo.mkultra.init.MKUWorldGen;
import com.chaosbuffalo.mkweapons.items.randomization.slots.LootSlotManager;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class MKUQuestProvider extends QuestDefinitionProvider {


    public MKUQuestProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        writeDefinition(generateIntroQuest(), cache);
        writeDefinition(generateTrooperArmorQuest(), cache);
        writeDefinition(generateIntroClericQuest(), cache);
    }

    private QuestDefinition generateIntroClericQuest(){
        QuestBuilder.QuestLocation introCastle = new QuestBuilder.QuestLocation(MKUWorldGen.INTRO_CASTLE_NAME, 0);
        QuestBuilder.QuestNpc acolyte = new QuestBuilder.QuestNpc(introCastle, new ResourceLocation(MKUltra.MODID, "solangian_acolyte"));
        QuestBuilder.QuestNpc apprentice = new QuestBuilder.QuestNpc(introCastle, new ResourceLocation(MKUltra.MODID, "solangian_apprentice"));

        QuestDefinition def = new QuestDefinition(new ResourceLocation(MKUltra.MODID, "cleric_intro"));
        def.setRepeatable(false);
        def.setQuestName(new StringTextComponent("A Missing Apprentice"));

        DialogueNode councilNode = new DialogueNode("council",
                "The leadership of my order is called the Council of the Nine. " +
                        "They are tasked with overseeing all affairs of the church.");
        DialoguePrompt councilPrompt = new DialoguePrompt("council",  "Council", "the Council?", "Council");
        councilPrompt.addResponse(new DialogueResponse(councilNode));

        DialogueNode holySeeNode = new DialogueNode("holySee", String.format(
                "Our order is dedicated to the worship of the Sun God, Solang. We work to preserve order and prosperity in the realm. " +
                "This plague of undeath is of great concern to the %s and we believe that this castle is somehow connected.", councilPrompt.getPromptEmbed()));
        DialoguePrompt holySeePrompt = new DialoguePrompt("holySee", "the Holy See", "Who are the Holy See?", "Holy See of Solang");
        holySeePrompt.addResponse(new DialogueResponse(holySeeNode));

        DialoguePrompt startQuestPrompt = new DialoguePrompt("start_quest", "task",
                "What task?", "task");
        DialogueNode hailNode = new DialogueNode("hail", String.format(
                "I am %s, sent here under the authority of the %s " +
                "to investigate the appearance of this castle. I hear you are going into the castle; " +
                "if you're interested, I have a %s for you.", DialogueContexts.ENTITY_NAME_CONTEXT, holySeePrompt.getPromptEmbed(), startQuestPrompt.getPromptEmbed()));


        DialoguePrompt apprenticePrompt = new DialoguePrompt("apprentice", "apprentice", "Where did you last see your apprentice?", "my apprentice");
        DialogueNode apprenticeNode = new DialogueNode("apprentice", String.format("I last saw %s in the library on the upper floors of the castle.", apprentice.getDialogueLink()));
        apprenticePrompt.addResponse(new DialogueResponse(apprenticeNode));

        DialogueNode findMyApprentice = new DialogueNode("start_quest",
                String.format("While you are exploring the castle, could you search for %s?" +
                        " We were ambushed by zombies while investigating the library and had to split up. " +
                        "I made it back but %s has yet to return.", apprenticePrompt.getPromptEmbed(), apprentice.getDialogueLink()));
        startQuestPrompt.addResponse(new DialogueResponse(findMyApprentice));

        def.setupStartQuestResponse(apprenticeNode, apprenticePrompt);
        def.addHailResponse(hailNode);
        def.addStartNode(councilNode);
        def.addStartNode(holySeeNode);
        def.addStartNode(findMyApprentice);
        def.addStartPrompt(councilPrompt);
        def.addStartPrompt(holySeePrompt);
        def.addStartPrompt(apprenticePrompt);
        def.addStartPrompt(startQuestPrompt);

        Quest talkToApprentice = new QuestBuilder("talk_to_apprentice",
                new StringTextComponent("You need to find the Apprentice somewhere in the castle. Perhaps near the library.."))
                .autoComplete(true)
                .simpleHail("talk_to_apprentice",
                        new StringTextComponent("Talk to the apprentice"),
                        apprentice,
                        "Oh thank goodness, it is good to see a friendly face. One of the zombies chased me into " +
                                "here and I wasn't certain if I'd ever get out. ",
                        false,
                        null
                )
                .reward(new XpReward(25))
                .quest();
        def.addQuest(talkToApprentice);

        return def;
    }

    private QuestDefinition generateTrooperArmorQuest(){
        QuestDefinition def = new QuestDefinition(new ResourceLocation(MKUltra.MODID, "trooper_armor"));
        def.addRequirement(new HasEntitlementRequirement(MKUEntitlements.GreenKnightTier1));
        def.setRepeatable(true);
        def.setQuestName(new StringTextComponent("Salvaged Trooper Armor"));
        def.setMode(QuestDefinition.QuestMode.UNSORTED);
        DialoguePrompt startQuestPrompt = new DialoguePrompt("start_quest", "need some armor",
                "I need some armor", "need some armor.");
        startQuestPrompt.addResponse(new DialogueResponse("start_quest"));
        DialogueNode hailNode = new DialogueNode("hail", String.format("I make armor for the Green Knights. " +
                        "We're running low on supplies but if you can salvage some parts from the zombies in the castle " +
                        "If you %s I should be able to put something together.",
                startQuestPrompt.getPromptEmbed()));
        DialogueNode questStart = new DialogueNode("start_quest",
                String.format("For the full set I will need %s, a %s, a %s, a %s, and a %s.", DialogueUtils.getStackCountItemProvider(new ItemStack(MKUItems.corruptedPigIronPlate, 20)),
                        DialogueUtils.getItemNameProvider(MKUItems.destroyedTrooperHelmet), DialogueUtils.getItemNameProvider(MKUItems.destroyedTrooperLeggings),
                        DialogueUtils.getItemNameProvider(MKUItems.destroyedTrooperChestplate), DialogueUtils.getItemNameProvider(MKUItems.destroyedTrooperBoots)));
        def.setupStartQuestResponse(questStart, startQuestPrompt);
        def.addHailResponse(hailNode);



        Quest helmet = new Quest("tradeHelmet", new StringTextComponent("The Green Smith needs " +
                "some scrap metal and a helmet from the pigs in the castle."));
        helmet.setAutoComplete(true);
        TradeItemsObjective helmetTrade = new TradeItemsObjective(
                "tradeHelmetObj",
                MKUWorldGen.INTRO_CASTLE_NAME, 0,
                new ResourceLocation(MKUltra.MODID, "green_smith"));
        helmetTrade.addItemStack(new ItemStack(MKUItems.corruptedPigIronPlate, 2));
        helmetTrade.addItemStack(new ItemStack(MKUItems.destroyedTrooperHelmet));
        helmet.addObjective(helmetTrade);
        helmet.addReward(new XpReward(25));
        helmet.addReward(new MKLootReward(new ResourceLocation(MKUltra.MODID, "trooper_knight_armor"), LootSlotManager.HEAD.getName(),
                new TranslationTextComponent("mkultra.quest_reward.receive_item.name", MKUItems.trooperKnightHelmet.getName())));
        def.addQuest(helmet);

        Quest leggings = new Quest("tradeLeggings", new StringTextComponent("The Green Smith needs " +
                "some scrap metal and a pair of leggings from the pigs in the castle."));
        leggings.setAutoComplete(true);
        TradeItemsObjective leggingsTrade = new TradeItemsObjective(
                "tradeHelmetObj",
                MKUWorldGen.INTRO_CASTLE_NAME, 0,
                new ResourceLocation(MKUltra.MODID, "green_smith"));
        leggingsTrade.addItemStack(new ItemStack(MKUItems.corruptedPigIronPlate, 6));
        leggingsTrade.addItemStack(new ItemStack(MKUItems.destroyedTrooperLeggings));
        leggings.addObjective(leggingsTrade);
        leggings.addReward(new XpReward(25));
        leggings.addReward(new MKLootReward(new ResourceLocation(MKUltra.MODID, "trooper_knight_armor"), LootSlotManager.LEGS.getName(),
                new TranslationTextComponent("mkultra.quest_reward.receive_item.name", MKUItems.trooperKnightLeggings.getName())));
        def.addQuest(leggings);

        Quest boots = new Quest("tradeBoots", new StringTextComponent("The Green Smith needs " +
                "some scrap metal and a pair of boots from the pigs in the castle."));
        boots.setAutoComplete(true);
        TradeItemsObjective bootTrade = new TradeItemsObjective(
                "tradeLeggingsObj",
                MKUWorldGen.INTRO_CASTLE_NAME, 0,
                new ResourceLocation(MKUltra.MODID, "green_smith"));
        bootTrade.addItemStack(new ItemStack(MKUItems.corruptedPigIronPlate, 4));
        bootTrade.addItemStack(new ItemStack(MKUItems.destroyedTrooperBoots));
        boots.addObjective(bootTrade);
        boots.addReward(new XpReward(25));
        boots.addReward(new MKLootReward(new ResourceLocation(MKUltra.MODID, "trooper_knight_armor"), LootSlotManager.FEET.getName(),
                new TranslationTextComponent("mkultra.quest_reward.receive_item.name", MKUItems.trooperKnightBoots.getName())));
        def.addQuest(boots);

        Quest chestplate = new Quest("tradeChestplate", new StringTextComponent("The Green Smith needs " +
                "some scrap metal and the chestplate from the pigs in the castle."));
        chestplate.setAutoComplete(true);
        TradeItemsObjective chestplateTrade = new TradeItemsObjective(
                "tradeChestObj",
                MKUWorldGen.INTRO_CASTLE_NAME, 0,
                new ResourceLocation(MKUltra.MODID, "green_smith"));
        chestplateTrade.addItemStack(new ItemStack(MKUItems.corruptedPigIronPlate, 8));
        chestplateTrade.addItemStack(new ItemStack(MKUItems.destroyedTrooperChestplate));
        chestplate.addObjective(chestplateTrade);
        chestplate.addReward(new XpReward(25));
        chestplate.addReward(new MKLootReward(new ResourceLocation(MKUltra.MODID, "trooper_knight_armor"), LootSlotManager.CHEST.getName(),
                new TranslationTextComponent("mkultra.quest_reward.receive_item.name", MKUItems.trooperKnightChestplate.getName())));
        def.addQuest(chestplate);

        return def;
    }

    private QuestDefinition generateIntroQuest(){

        QuestBuilder.QuestLocation introCastle = new QuestBuilder.QuestLocation(MKUWorldGen.INTRO_CASTLE_NAME, 0);
        QuestBuilder.QuestNpc greenLady = new QuestBuilder.QuestNpc(introCastle, new ResourceLocation(MKUltra.MODID, "green_lady"));
        QuestBuilder.QuestNpc piglinCaptain = new QuestBuilder.QuestNpc(introCastle, new ResourceLocation(MKUltra.MODID, "trooper_captain"));
        QuestBuilder.QuestNpc greenSmith = new QuestBuilder.QuestNpc(introCastle, new ResourceLocation(MKUltra.MODID, "green_smith"));
        QuestBuilder.QuestNpc forlornGhost = new QuestBuilder.QuestNpc(introCastle, new ResourceLocation(MKUltra.MODID, "forlorn_ghost"));
        QuestBuilder.QuestNpc burningRevenant = new QuestBuilder.QuestNpc(introCastle, new ResourceLocation(MKUltra.MODID, "burning_skeleton"));

        QuestDefinition def = new QuestDefinition(new ResourceLocation(MKUltra.MODID, "intro_quest"));
        def.setQuestName(new StringTextComponent("The Green Knights"));
        DialoguePrompt startQuestPrompt = new DialoguePrompt("start_quest", "don't know",
                "I don't know", "What are you doing");
        startQuestPrompt.addResponse(new DialogueResponse("start_quest"));
        DialogueNode hailNode = new DialogueNode("hail", String.format("Hail and well met. " +
                "You're lucky we were able to grab you before your soul drifted too far into the aether. %s in an archival zone?",
                startQuestPrompt.getPromptEmbed()));
        DialogueNode questStart = new DialogueNode("start_quest", "This world is on the verge of deletion, the dead rise from the ground everywhere, " +
                "there may still be time to save it if we act now. " +
                "We're in need of another hero: go talk to our smith and get equipped.");
        def.addHailResponse(hailNode);
        def.setupStartQuestResponse(questStart, startQuestPrompt);


        Quest talk1 = new QuestBuilder("talk_to_smith",
                new StringTextComponent("The Green Lady wants you to go talk to the smith and equip yourself for an unknown task."))
                .autoComplete(true)
                .simpleHail("talk_to_smith",
                        new StringTextComponent("Talk to the smith"),
                        greenSmith,
                        "We ain't got much left after the crash. " +
                                "Check that chest over there we got a few things. You can use my crafting table as well. " +
                                "Talk to me again when you have made a weapon.",
                        true,
                        null
                )
                .reward(new XpReward(25))
                .quest();
        def.addQuest(talk1);


        Quest lootSmithChest = new QuestBuilder("equip_yourself", new StringTextComponent("The Green Smith points you towards a chest in his workshop."))
                .autoComplete(true)
                .lootChest("loot_chest", new StringTextComponent("Loot the smith's chest"), introCastle, "intro_chest",
                        new ItemStack(Blocks.COBBLESTONE, 20),
                        new ItemStack(Blocks.OAK_PLANKS, 20),
                        new ItemStack(Items.STRING, 10),
                        new ItemStack(Items.LEATHER, 40),
                        new ItemStack(Items.COAL, 10),
                        new ItemStack(Items.FLINT, 10),
                        new ItemStack(Items.PORKCHOP, 10)
                )
                .reward(new XpReward(25))
                .quest();
        def.addQuest(lootSmithChest);

        Quest returnToSmith = new QuestBuilder("return_to_smith", new StringTextComponent("Use the Green Smith's supplies to craft your desired weapon and perhaps some armor for the battle ahead."))
                .autoComplete(true)
                .hailWithCondition("return_to_smith",
                        new StringTextComponent("Talk to the Green Smith with a weapon in your hand."),
                        greenSmith,
                        "Great, but you're going to need more than just a sharp rock where we're going. " +
                                "Go back and talk to the Green Lady, ask her about learning to develop your magical talents.",
                        "Come back to me with a weapon in your hand.",
                        new HasWeaponInHandCondition(),
                        null
                        )
                .reward(new XpReward(25))
                .quest();
        def.addQuest(returnToSmith);

        Quest greenLadyTrainTalent = new QuestBuilder("green_lady_talent",
                new StringTextComponent("Talk to the Green Lady to learn more about developing your magical abilities"))
                .autoComplete(true)
                .simpleHail(
                        "green_lady_talent",
                        new StringTextComponent("Talk to the Green Lady about the talent system."),
                        greenLady,
                        "We can help you awaken your magical gifts, the first step is learning how " +
                                "to train your talents. You should have gained a talent point upon initiating this conversation. " +
                                "Open your player screen and go to the talent section, train any of the first talents in " +
                                "order to unlock your first ability slot. Talk to me again when you have finished this.",
                        true,
                        null
                        )
                .reward(new XpReward(25))
                .quest();
        def.addQuest(greenLadyTrainTalent);

        DialogueNode openTraining = new DialogueNode("open_training",
                "Let me see what I can teach you. Talk to me again when you're done.");
        openTraining.addEffect(new OpenLearnAbilitiesEffect());
        DialoguePrompt needTraining = new DialoguePrompt("need_training", "want to learn",
                "I want to learn.", "ready to learn");
        needTraining.addResponse(new DialogueResponse(openTraining.getId()));

        Quest returnToGreenLady = new QuestBuilder("return_to_green_lady",
                new StringTextComponent("The Green Lady wants you to learn about spending talent points."))
                .autoComplete(true)
                .hailWithCondition("return_to_green_lady",
                        new StringTextComponent("Talk to the Green Lady after training a talent."),
                        greenLady,
                        String.format(
                                "Alright you're now %s your first ability.", needTraining.getPromptEmbed()),
                        "Come back to me when you have spent your first talent point.",
                        new HasSpentTalentPointsCondition(1),
                        (convo) ->{
                            convo.withAdditionalNode(openTraining);
                            convo.withAdditionalPrompts(needTraining);
                        }
                        )
                .reward(new XpReward(50))
                .reward(new GrantEntitlementReward(MKUEntitlements.GreenKnightTier1))
                .quest();
        def.addQuest(returnToGreenLady);


        DialogueNode openTraining2 = new DialogueNode("open_training_ability",
                "Let me see what I can teach you. Talk to me again when you're done.");
        openTraining2.addEffect(new OpenLearnAbilitiesEffect());
        DialoguePrompt needTraining2 = new DialoguePrompt("need_training_ability", "want to learn",
                "I want to learn.", "ready to learn");
        needTraining2.addResponse(new DialogueResponse(openTraining2.getId()));

        Quest afterAbility = new QuestBuilder("after_ability",
                new StringTextComponent("Talk to the Green Lady and learn your first ability, then speak to her again."))
                .autoComplete(true)
                .hailWithCondition("after_green_lady",
                        new StringTextComponent("Talk to the Green Lady after learning your first ability."),
                        greenLady,
                        "Now we must test your mettle in combat. " +
                                "Go kill some of the zombies on the first floor to try out your new magic, and don't forget you can always return to me to learn more.",
                        "Come back to me once you've learned one of our abilities.",
                        new HasTrainedAbilitiesCondition(false, SkinLikeWoodAbility.INSTANCE.getAbilityId(), NaturesRemedyAbility.INSTANCE.getAbilityId()),
                        (convo) ->{
                            convo.withAdditionalNode(openTraining2);
                            convo.withAdditionalPrompts(needTraining2);
                        })
                .reward(new XpReward(50))
                .quest();
        def.addQuest(afterAbility);

        Quest killQuest = new QuestBuilder("first_kill",
                new StringTextComponent("The Green Lady wants you to clear out some of the zombies on the first floor of the castle"))
                .autoComplete(true)
                .killNpc("kill_zombies", new ResourceLocation(MKUltra.MODID, "decaying_piglin"), 4)
                .killNpc("kill_archers", new ResourceLocation(MKUltra.MODID, "decaying_piglin_archer"), 4)
                .hailWithObjectives("after_kill",
                        new StringTextComponent("Talk to the Green Lady after completing the other objectives."),
                        greenLady,
                        String.format("Your skills have not gone unnoticed. " +
                                        "The dead rise everywhere, to cull the damned is a blessed pursuit. " +
                                        "Would you be willing to go back into that cursed hall and destroy %s.",
                                piglinCaptain.getDialogueLink()),
                        "Come back to me after you've proven yourself.",
                        Arrays.asList("kill_zombies", "kill_archers"),
                        null)
                .reward(new XpReward(50))
                .quest();
        def.addQuest(killQuest);

        Quest killCaptain = new QuestBuilder("kill_captain", new StringTextComponent("The Green Lady wants you to find and kill the Piglin Captain"))
                .autoComplete(true)
                .killNotable("kill_captain", piglinCaptain)
                .hailWithObjectives(
                        "after_kill_captain",
                        new StringTextComponent("Talk to the Green Lady after completing the other objectives."),
                        greenLady,
                        "I need you to return to the castle and delve even deeper. I know not why it appeared here at this time, but I do sense a residual life force beneath the castle. " +
                                "Perhaps this spirit will be amiable to conversation. Go find it.",
                        String.format("Come back to me after you've taken care of %s.", piglinCaptain.getDialogueLink()),
                        Collections.singletonList("kill_captain"),
                        null
                        )
                .reward(new XpReward(100))
                .reward(new GrantEntitlementReward(MKUEntitlements.GreenKnightTier2))
                .quest();
        def.addQuest(killCaptain);

        DialogueNode finalOffer = new DialogueNode("offer", "The skeleton that stands ever-burning in the chamber beyond. Destroy this revenant and return to your Green Lady. Leave me to infinity.");
        DialoguePrompt offerPrompt = new DialoguePrompt("offer", "soulless husk", "a soulless husk?", "soulless husk");
        offerPrompt.addResponse(new DialogueResponse(finalOffer));
        finalOffer.addEffect(new ObjectiveCompleteEffect("talk_to_ghost", "talk_to_ghost"));

        DialogueNode attempt = new DialogueNode("attempt", String.format("I was mortally wounded when the time came, and something went wrong. My partner and I were unable to complete the ritual. " +
                "My body was destroyed immediately, leaving my soul bound to the castle grounds. My partner's spirit was shorn from their body as the spell completed, " +
                "leaving only a %s to stalk the depths of this cursed castle.", offerPrompt.getPromptEmbed()));
        DialoguePrompt attemptPrompt = new DialoguePrompt("attempt", "the attempt", "What happened during the attempt?", "attempt");
        attemptPrompt.addResponse(new DialogueResponse(attempt));

        DialogueNode empire = new DialogueNode("empire", String.format("Those pigs just can't stop killing and stealing. Been doing it for 500 years or so. " +
                "I died here in an early %s at banishing a Piglin Castle back to their home-plane.", attemptPrompt.getPromptEmbed()));
        DialoguePrompt empirePrompt = new DialoguePrompt("empire", "Piglin Empire", "the Piglin Empire?", "Piglin Empire");
        empirePrompt.addResponse(new DialogueResponse(empire));

        DialogueNode myTime = new DialogueNode("my_time", String.format("Best I can tell, that was 500 or so years ago. " +
                "This castle dates back to the %s of my time. We were in the early days of the first invasion.", empirePrompt.getPromptEmbed()));
        DialoguePrompt myTimePrompt = new DialoguePrompt("my_time", "your time", "When was your time?", "my time");
        myTimePrompt.addResponse(new DialogueResponse(myTime));

        DialogueNode greenLadyDesc = new DialogueNode("green_lady_desc", "There's always only one Green Lady. " +
                "I don't know if it's always the same one. The Green Lady is in charge, whatever that means. The GK's are a secretive bunch. I never dealt with them when I was alive.");
        DialoguePrompt greenLadyPrompt = new DialoguePrompt("green_lady_desc", "Green Lady", "What do you know about the Green Lady?", "Green Lady");
        greenLadyPrompt.addResponse(new DialogueResponse(greenLadyDesc));

        DialogueNode peril = new DialogueNode("peril", "Oh the same old hogwash about the world being overrun by the undead and then deleted. " +
                "In the Hyborean religion we believe the world, which is a giant cube, travels around in the dice bag of an interplanar vagrant." +
                " Obviously the world will end when said villain abandons, forgets, or perhaps loses us in a game of chance to an even iller-suited caretaker.");
        DialoguePrompt perilPrompt = new DialoguePrompt("peril", "grave peril", "What grave peril?", "grave peril");
        perilPrompt.addResponse(new DialogueResponse(peril));

        DialogueNode crusaders = new DialogueNode("crusaders", String.format("They call themselves the Green Knights, we had them in %s as well. " +
                "Serve some orc called the %s. They're laser-focused on seeking out and destroying corruption throughout the known planes; " +
                "think we're all in %s and so on.", myTimePrompt.getPromptEmbed(), greenLadyPrompt.getPromptEmbed(), perilPrompt.getPromptEmbed()));
        DialoguePrompt crusadersPrompt = new DialoguePrompt("crusaders", "crusaders", "What crusaders?", "dimension-hopping crusaders");
        crusadersPrompt.addResponse(new DialogueResponse(crusaders));

        Quest talkToGhost = new QuestBuilder("talk_to_ghost", new StringTextComponent("The Green Lady wants you to seek out a spirit in the depths."))
                .autoComplete(true)
                .simpleHail("talk_to_ghost", new StringTextComponent("Find the spirit in the castle."), forlornGhost,
                        String.format("Those %s sent you after me didn't they?", crusadersPrompt.getPromptEmbed()), false,
                        obj -> obj
                                .withAdditionalNode(finalOffer)
                                .withAdditionalPrompts(offerPrompt)
                                .withAdditionalNode(attempt)
                                .withAdditionalPrompts(attemptPrompt)
                                .withAdditionalNode(empire)
                                .withAdditionalPrompts(empirePrompt)
                                .withAdditionalNode(myTime)
                                .withAdditionalPrompts(myTimePrompt)
                                .withAdditionalNode(greenLadyDesc)
                                .withAdditionalPrompts(greenLadyPrompt)
                                .withAdditionalNode(peril)
                                .withAdditionalPrompts(perilPrompt)
                                .withAdditionalNode(crusaders)
                                .withAdditionalPrompts(crusadersPrompt)
                )
                .reward(new XpReward(25))
                .quest();
        def.addQuest(talkToGhost);

        Quest killBurning = new QuestBuilder("kill_burning", new StringTextComponent("The Forlorn Ghost has asked you you to kill the Burning Revenant"))
                .autoComplete(true)
                .killNotable("kill_burning", burningRevenant)
                .hailWithObjectives(
                        "after_kill_burning",
                        new StringTextComponent("Return to the Green Lady after completing the other objectives."),
                        greenLady,
                        "Good: it is done. Our order is dedicated to cleansing this land. You are welcome to stay here and learn of our ways or go as you please.",
                        String.format("Has the castle been cleansed, is the %s dead?", burningRevenant.getDialogueLink()),
                        Collections.singletonList("kill_burning"),
                        null
                )
                .reward(new XpReward(100))
                .reward(new GrantEntitlementReward(MKUEntitlements.GreenKnightTier3))
                .quest();
        def.addQuest(killBurning);

        return def;
    }

    public static class QuestBuilder {
        private Quest quest;

        public QuestBuilder(String questName, IFormattableTextComponent description){
            this.quest = new Quest(questName, description);
        }

        public QuestBuilder autoComplete(boolean value){
            quest.setAutoComplete(value);
            return this;
        }

        public QuestBuilder objective(QuestObjective<?> objective){
            quest.addObjective(objective);
            return this;
        }

        public QuestBuilder reward(QuestReward reward){
            quest.addReward(reward);
            return this;
        }

        public QuestBuilder killNotable(String objectiveName, QuestNpc npc){
            KillNotableNpcObjective kill = new KillNotableNpcObjective(objectiveName, npc.location.structureName,
                    npc.location.structureIndex, npc.npcDef);
            objective(kill);
            return this;
        }

        public QuestBuilder killNpc(String objectiveName, ResourceLocation npcDef, int count){
            KillNpcDefObjective kill = new KillNpcDefObjective(objectiveName, npcDef, count);
            objective(kill);
            return this;
        }

        public QuestBuilder hailWithObjectives(String objectiveName, IFormattableTextComponent description,
                                               QuestNpc talkTo, String withComplete,
                                               String withoutComplete, List<String> objectives,
                                               @Nullable Consumer<TalkToNpcObjective> additionalLogic){
            TalkToNpcObjective talkObj = new TalkToNpcObjective(objectiveName,
                    talkTo.location.structureName, talkTo.location.structureIndex, talkTo.npcDef, description);
            DialogueNode completeNode = new DialogueNode(String.format("%s_complete", objectiveName), withComplete);
            DialogueResponse completeResponse = new DialogueResponse(completeNode.getId());
            completeResponse.addCondition(new ObjectivesCompleteCondition(quest.getQuestName(), objectives.toArray(new String[0])));
            completeNode.addEffect(new ObjectiveCompleteEffect(talkObj.getObjectiveName(), quest.getQuestName()));
            DialogueNode withoutCompleteNode = new DialogueNode(String.format("%s_wo_complete", objectiveName), withoutComplete);
            DialogueResponse withoutResponse = new DialogueResponse(withoutCompleteNode.getId());
            talkObj.withHailResponse(completeNode, completeResponse);
            talkObj.withHailResponse(withoutCompleteNode, withoutResponse);
            if (additionalLogic != null){
                additionalLogic.accept(talkObj);
            }
            objective(talkObj);
            return this;
        }

        public QuestBuilder hailWithCondition(String objectiveName, IFormattableTextComponent description,
                                              QuestNpc talkTo, String withCondition, String withoutCondition,
                                              DialogueCondition withCond,
                                              @Nullable Consumer<TalkToNpcObjective> additionalLogic){
            TalkToNpcObjective talkObj = new TalkToNpcObjective(objectiveName,
                    talkTo.location.structureName, talkTo.location.structureIndex, talkTo.npcDef, description);
            DialogueNode conditionNode = new DialogueNode(String.format("%s_w_cond", objectiveName), withCondition);
            DialogueResponse conditionResponse = new DialogueResponse(conditionNode.getId());
            conditionResponse.addCondition(withCond);
            conditionNode.addEffect(new ObjectiveCompleteEffect(talkObj.getObjectiveName(), quest.getQuestName()));
            DialogueNode withoutConditionNode = new DialogueNode(String.format("%s_wo_cond", objectiveName), withoutCondition);
            DialogueResponse withoutConditionResponse = new DialogueResponse(withoutConditionNode.getId());
            talkObj.withHailResponse(conditionNode, conditionResponse);
            talkObj.withHailResponse(withoutConditionNode, withoutConditionResponse);
            if (additionalLogic != null){
                additionalLogic.accept(talkObj);
            }
            objective(talkObj);
            return this;
        }

        public QuestBuilder simpleHail(String objectiveName, IFormattableTextComponent description,
                                       QuestNpc talkTo, String hailMessage,
                                       boolean immediateComplete, @Nullable Consumer<TalkToNpcObjective> additionalLogic){
            TalkToNpcObjective talkObj = new TalkToNpcObjective(
                    objectiveName,
                    talkTo.location.structureName, talkTo.location.structureIndex, talkTo.npcDef,
                    description);
            DialogueNode hailNode = new DialogueNode(String.format("%s_hail", objectiveName), hailMessage);
            if (immediateComplete){
                hailNode.addEffect(new ObjectiveCompleteEffect(talkObj.getObjectiveName(), quest.getQuestName()));
            }
            talkObj.withHailResponse(hailNode, new DialogueResponse(hailNode.getId()));
            if (additionalLogic != null){
                additionalLogic.accept(talkObj);
            }
            objective(talkObj);
            return this;
        }

        public QuestBuilder lootChest(String objectiveName, IFormattableTextComponent description, QuestLocation location,
                                      String chestTag, ItemStack... items){
            LootChestObjective chestObj = new LootChestObjective(objectiveName, location.structureName,
                    location.structureIndex, chestTag, description);
            for (ItemStack item : items){
                chestObj.addItemStack(item);
            }
            objective(chestObj);
            return this;
        }

        public Quest quest(){
            return quest;
        }

        public static class QuestLocation {
            ResourceLocation structureName;
            int structureIndex;

            public QuestLocation(ResourceLocation structureName, int structureIndex){
                this.structureIndex = structureIndex;
                this.structureName = structureName;
            }
        }

        public static class QuestNpc {
            QuestLocation location;
            ResourceLocation npcDef;

            public QuestNpc(QuestLocation location, ResourceLocation npcDef){
                this.location = location;
                this.npcDef = npcDef;
            }

            public String getDialogueLink(){
                return NpcDialogueUtils.getNotableNpcRaw(location.structureName, location.structureIndex, npcDef);
            }
        }
    }
}
