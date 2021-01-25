//package com.chaosbuffalo.mkultra.init;
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.MKURegistry;
//import com.chaosbuffalo.mkultra.core.PlayerAttributes;
//import com.chaosbuffalo.mkultra.abilities.passives.*;
//import com.chaosbuffalo.mkultra.abilities.ultimates.*;
//import com.chaosbuffalo.mkultra.core.talents.*;
//import com.chaosbuffalo.mkultra.log.Log;
//import com.chaosbuffalo.mkultra.utils.JsonLoader;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import net.minecraft.entity.SharedMonsterAttributes;
//import net.minecraft.entity.ai.attributes.RangedAttribute;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Loader;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.ModContainer;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import net.minecraftforge.fml.common.registry.GameRegistry;
//import net.minecraftforge.registries.IForgeRegistry;
//import net.minecraftforge.registries.IForgeRegistryModifiable;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.UUID;
//
//@Mod.EventBusSubscriber
//@GameRegistry.ObjectHolder(MKUltra.MODID)
//public class ModTalents {
//
//    @GameRegistry.ObjectHolder("talent.magic_armor")
//    public static RangedAttributeTalent magicArmor;
//    @GameRegistry.ObjectHolder("talent.max_mana")
//    public static RangedAttributeTalent maxMana;
//    @GameRegistry.ObjectHolder("talent.max_health")
//    public static RangedAttributeTalent maxHealth;
//    @GameRegistry.ObjectHolder("talent.magic_damage")
//    public static RangedAttributeTalent magicDamage;
//    @GameRegistry.ObjectHolder("talent.attack_damage")
//    public static RangedAttributeTalent attackDamage;
//    @GameRegistry.ObjectHolder("talent.heal_bonus")
//    public static RangedAttributeTalent healBonus;
//    @GameRegistry.ObjectHolder("talent.armor")
//    public static RangedAttributeTalent armor;
//    @GameRegistry.ObjectHolder("talent.mana_regen")
//    public static RangedAttributeTalent manaRegen;
//    @GameRegistry.ObjectHolder("talent.health_regen")
//    public static RangedAttributeTalent healthRegen;
//    @GameRegistry.ObjectHolder("talent.cooldown_reduction")
//    public static RangedAttributeTalent cooldownReduction;
//    @GameRegistry.ObjectHolder("talent.melee_crit_damage")
//    public static RangedAttributeTalent meleeCritDamage;
//    @GameRegistry.ObjectHolder("talent.spell_crit_damage")
//    public static RangedAttributeTalent spellCritDamage;
//    @GameRegistry.ObjectHolder("talent.melee_crit")
//    public static RangedAttributeTalent meleeCrit;
//    @GameRegistry.ObjectHolder("talent.spell_crit")
//    public static RangedAttributeTalent spellCrit;
//    @GameRegistry.ObjectHolder("talent.movement_speed")
//    public static RangedAttributeTalent movementSpeed;
//
//    public static void postInitJsonRegistration() {
//        ModContainer old = Loader.instance().activeModContainer();
//        JsonLoader.loadModsForType("mk_talents" + File.separator + "trees",
//                "mk_overrides", "assets",
//                ModTalents::loadTalentTree, MKURegistry.REGISTRY_TALENT_TREES);
//        Loader.instance().setActiveModContainer(old);
//    }
//
//    public static void loadTalentTree(ResourceLocation name, JsonObject obj,
//                                      IForgeRegistry<TalentTree> registry) {
//        name = new ResourceLocation(name.getNamespace(), "talent_tree." + name.getPath());
//        String[] keys = {"version", "lines"};
//        if (!JsonLoader.checkKeysExist(obj, keys)) {
//            return;
//        }
//        int version = obj.get("version").getAsInt();
//        TalentTree tree = new TalentTree(name, version);
//        JsonArray lineEles = obj.get("lines").getAsJsonArray();
//        for (JsonElement lineEle : lineEles) {
//            String[] lineKeys = {"name", "talents"};
//            JsonObject lineObj = lineEle.getAsJsonObject();
//            if (!JsonLoader.checkKeysExist(lineObj, lineKeys)) {
//                continue;
//            }
//            String lineName = lineObj.get("name").getAsString();
//            ArrayList<TalentNode> talentLine = new ArrayList<>();
//            JsonArray talentEles = lineObj.get("talents").getAsJsonArray();
//            for (JsonElement talentEle : talentEles) {
//                String[] talentKeys = {"name", "max_points"};
//                JsonObject talentObj = talentEle.getAsJsonObject();
//                if (!JsonLoader.checkKeysExist(talentObj, talentKeys)) {
//                    continue;
//                }
//                ResourceLocation talentName = new ResourceLocation(talentObj.get("name").getAsString());
//                int maxPoints = talentObj.get("max_points").getAsInt();
//                BaseTalent talent = MKURegistry.getTalent(talentName);
//                if (talent != null) {
//                    switch (talent.getTalentType()) {
//                        case ATTRIBUTE:
//                            String[] attrKeys = {"value"};
//                            if (!JsonLoader.checkKeysExist(talentObj, attrKeys)) {
//                                Log.info("Skipping attribute missing extra keys.");
//                                break;
//                            }
//                            AttributeTalentNode attrNode = new AttributeTalentNode(
//                                    (RangedAttributeTalent) talent, maxPoints, talentObj.get("value").getAsDouble());
//                            talentLine.add(attrNode);
//                            break;
//                        case PASSIVE:
//                        case ULTIMATE:
//                            TalentNode node = new TalentNode(talent, maxPoints);
//                            talentLine.add(node);
//                            break;
//                        default:
//                            Log.info("Type %s not implemented, skipping talent parsing",
//                                    talent.getTalentType().toString());
//                            break;
//                    }
//                }
//            }
//            Log.debug("Loading Line: %s for Tree: %s with %d talents", lineName, name.toString(), talentLine.size());
//            tree.addLine(lineName, talentLine);
//        }
//        Log.info("Registering Talent Tree: %s", name.toString());
//        if (registry instanceof IForgeRegistryModifiable) {
//            IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) registry;
//            modRegistry.remove(name);
//        }
//        registry.register(tree);
//    }
//
//    @SubscribeEvent
//    public static void registerTalents(RegistryEvent.Register<BaseTalent> event) {
//        RangedAttributeTalent magicArmor = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.magic_armor"),
//                PlayerAttributes.MAGIC_ARMOR,
//                UUID.fromString("5588f6cc-7278-434e-9cf1-a3d2262a9c76"));
//        event.getRegistry().register(magicArmor);
//        RangedAttributeTalent maxMana = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.max_mana"),
//                PlayerAttributes.MAX_MANA,
//                UUID.fromString("50338dba-eaca-4ec8-a71f-13b5924496f4"));
//        event.getRegistry().register(maxMana);
//        RangedAttributeTalent maxHealth = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.max_health"),
//                (RangedAttribute) SharedMonsterAttributes.MAX_HEALTH,
//                UUID.fromString("5d95bcd4-a06e-415a-add0-f1f85e20b18b"));
//        event.getRegistry().register(maxHealth);
//        RangedAttributeTalent magicAttackDamage = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.magic_damage"),
//                PlayerAttributes.MAGIC_ATTACK_DAMAGE,
//                UUID.fromString("23a0f1c5-37c6-4d88-ac3b-51df84165afe"));
//        event.getRegistry().register(magicAttackDamage);
//        RangedAttributeTalent attackDamage = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.attack_damage"),
//                (RangedAttribute) SharedMonsterAttributes.ATTACK_DAMAGE,
//                UUID.fromString("ec2d2cfe-ba12-4955-a94e-2c389c696004"));
//        event.getRegistry().register(attackDamage);
//        RangedAttributeTalent healBonus = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.heal_bonus"),
//                PlayerAttributes.HEAL_BONUS,
//                UUID.fromString("711e57c3-cf2a-4fb5-a503-3dff0a1e007d"));
//        event.getRegistry().register(healBonus);
//        RangedAttributeTalent armor = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.armor"),
//                (RangedAttribute) SharedMonsterAttributes.ARMOR,
//                UUID.fromString("1f917d51-efa1-43ee-8af0-b49175c97c0b"));
//        event.getRegistry().register(armor);
//        RangedAttributeTalent manaRegen = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.mana_regen"),
//                PlayerAttributes.MANA_REGEN,
//                UUID.fromString("87cd1a11-682f-4635-97db-4fedf6a7496b"));
//        event.getRegistry().register(manaRegen);
//        RangedAttributeTalent healthRegen = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.health_regen"),
//                PlayerAttributes.HEALTH_REGEN,
//                UUID.fromString("cee8e51d-e216-4ddd-897b-6618d1ef6868"));
//        event.getRegistry().register(healthRegen);
//
//        RangedAttributeTalent cooldownRate = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.cooldown_reduction"),
//                PlayerAttributes.COOLDOWN,
//                UUID.fromString("5378ff4c-0606-4781-abc0-c7d3e945b378"))
//                .setOp(PlayerAttributes.OP_SCALE_MULTIPLICATIVE)
//                .setDisplayAsPercentage(true);
//        event.getRegistry().register(cooldownRate);
//        RangedAttributeTalent meleeCritDamage = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.melee_crit_damage"),
//                PlayerAttributes.MELEE_CRITICAL_DAMAGE,
//                UUID.fromString("0032d49a-ed71-4dfb-a9f5-f0d3dd183e96"))
//                .setDisplayAsPercentage(true);
//        event.getRegistry().register(meleeCritDamage);
//        RangedAttributeTalent spellCritDamage = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.spell_crit_damage"),
//                PlayerAttributes.SPELL_CRITICAL_DAMAGE,
//                UUID.fromString("a9d6069c-98b9-454d-b59f-c5a6e81966d5"))
//                .setDisplayAsPercentage(true);
//        event.getRegistry().register(spellCritDamage);
//        RangedAttributeTalent meleeCrit = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.melee_crit"),
//                PlayerAttributes.MELEE_CRIT,
//                UUID.fromString("3b9ea27d-61ca-47b4-9bba-e82679b74ddd"))
//                .setDisplayAsPercentage(true);
//        event.getRegistry().register(meleeCrit);
//        RangedAttributeTalent spellCrit = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.spell_crit"),
//                PlayerAttributes.SPELL_CRIT,
//                UUID.fromString("9fbc7b94-4836-45ca-933a-4edaabcf2c6a"))
//                .setDisplayAsPercentage(true);
//        event.getRegistry().register(spellCrit);
//        RangedAttributeTalent movementSpeed = new RangedAttributeTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.movement_speed"),
//                (RangedAttribute) SharedMonsterAttributes.MOVEMENT_SPEED,
//                UUID.fromString("95fcf4d0-aaa9-413f-8362-7706e29412f7"))
//                .setDisplayAsPercentage(true);
//        event.getRegistry().register(movementSpeed);
//
//        PassiveAbilityTalent burningSoul = new PassiveAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.burning_soul"),
//                BurningSoul.INSTANCE);
//        event.getRegistry().register(burningSoul);
//        PassiveAbilityTalent extendedDuration = new PassiveAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.extended_duration"),
//                ExtendedDuration.INSTANCE);
//        event.getRegistry().register(extendedDuration);
//        PassiveAbilityTalent twoHandedStyle = new PassiveAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.two_handed_style"),
//                TwoHandedStyle.INSTANCE);
//        event.getRegistry().register(twoHandedStyle);
//        PassiveAbilityTalent dualWield = new PassiveAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.dual_wield"),
//                DualWield.INSTANCE);
//        event.getRegistry().register(dualWield);
//        PassiveAbilityTalent blademaster = new PassiveAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.blademaster"),
//                Blademaster.INSTANCE);
//        event.getRegistry().register(blademaster);
//        PassiveAbilityTalent holyAura = new PassiveAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.holy_aura"),
//                HolyAura.INSTANCE);
//        event.getRegistry().register(holyAura);
//        PassiveAbilityTalent armorTraining = new PassiveAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.armor_training"),
//                ArmorTraining.INSTANCE);
//        event.getRegistry().register(armorTraining);
//        PassiveAbilityTalent soulDrain = new PassiveAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.soul_drain"),
//                SoulDrain.INSTANCE);
//        event.getRegistry().register(soulDrain);
//        PassiveAbilityTalent lifeSiphon = new PassiveAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.life_siphon"),
//                LifeSiphon.INSTANCE);
//        event.getRegistry().register(lifeSiphon);
//
//        PassiveAbilityTalent guardianAngel = new PassiveAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.guardian_angel"),
//                GuardianAngel.INSTANCE);
//        event.getRegistry().register(guardianAngel);
//
//        UltimateAbilityTalent meteor = new UltimateAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.meteor"),
//                Meteor.INSTANCE);
//        event.getRegistry().register(meteor);
//
//        UltimateAbilityTalent backstab = new UltimateAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.backstab"),
//                Backstab.INSTANCE);
//        event.getRegistry().register(backstab);
//
//        UltimateAbilityTalent healingRain = new UltimateAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.healing_rain"),
//                HealingRain.INSTANCE);
//        event.getRegistry().register(healingRain);
//
//        UltimateAbilityTalent bolsteringRoar = new UltimateAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.bolstering_roar"),
//                BolsteringRoar.INSTANCE);
//        event.getRegistry().register(bolsteringRoar);
//
//        UltimateAbilityTalent righteousJudgement = new UltimateAbilityTalent(
//                new ResourceLocation(MKUltra.MODID, "talent.righteous_judgement"),
//                RighteousJudgement.INSTANCE);
//        event.getRegistry().register(righteousJudgement);
//    }
//}
