package com.chaosbuffalo.mkultra.abilities.misc;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.abilities.ai.conditions.SummonPetCondition;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKPlayerData;
import com.chaosbuffalo.mkcore.core.pets.MKPet;
import com.chaosbuffalo.mkcore.core.pets.PetNonCombatBehavior;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkcore.utils.TargetUtil;
import com.chaosbuffalo.mkfaction.capabilities.FactionCapabilities;
import com.chaosbuffalo.mkfaction.faction.MKFaction;
import com.chaosbuffalo.mknpc.MKNpc;
import com.chaosbuffalo.mknpc.capabilities.IEntityNpcData;
import com.chaosbuffalo.mknpc.entity.MKEntity;
import com.chaosbuffalo.mknpc.entity.ai.memory.MKMemoryModuleTypes;
import com.chaosbuffalo.mknpc.npc.NpcDefinition;
import com.chaosbuffalo.mknpc.npc.NpcDefinitionManager;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.targeting_api.Targeting;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.UUID;

public class MKEntitySummonAbility extends MKAbility {
    protected final ResourceLocationAttribute npcDefintion = new ResourceLocationAttribute("npc", NpcDefinitionManager.INVALID_NPC_DEF);
    public static final MKEntitySummonAbility TEST_SUMMON = new MKEntitySummonAbility(
            new ResourceLocation(MKUltra.MODID, "ability.test_summon"),
            new ResourceLocation(MKUltra.MODID, "hyborean_sorcerer_queen"));


    public MKEntitySummonAbility(ResourceLocation abilityId, ResourceLocation npcDef) {
        super(abilityId);
        npcDefintion.setDefaultValue(npcDef);
        addAttribute(npcDefintion);
        setCastTime(5 * GameConstants.TICKS_PER_SECOND);
        setUseCondition(new SummonPetCondition(this));
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ALL;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.POSITION_INCLUDE_ENTITIES;
    }

    @Override
    public Set<MemoryModuleType<?>> getRequiredMemories() {
        return ImmutableSet.of(MKAbilityMemories.ABILITY_POSITION_TARGET);
    }

    @Override
    public int getCastTime(IMKEntityData casterData) {
        return casterData.getPets().isPetActive(getAbilityId()) ? 0 : super.getCastTime(casterData);
    }

    @Override
    public float getManaCost(IMKEntityData casterData) {
        return casterData.getPets().isPetActive(getAbilityId()) ? 0f : super.getManaCost(casterData);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 20.0f;
    }

    @Override
    public void endCast(LivingEntity castingEntity, IMKEntityData casterData, AbilityContext context) {
        super.endCast(castingEntity, casterData, context);
        TargetUtil.LivingOrPosition target = context.getMemory(MKAbilityMemories.ABILITY_POSITION_TARGET).orElse(null);
        if (target == null) {
            return;
        }
        if (!casterData.getPets().isPetActive(getAbilityId())) {
            NpcDefinition def = NpcDefinitionManager.getDefinition(npcDefintion.getValue());
            if (def != null && target.getPosition().isPresent()) {
                UUID id = casterData instanceof MKPlayerData ? ((MKPlayerData) casterData).getPersonaManager().getActivePersona().getPersonaId() :
                        MKNpc.getNpcData(castingEntity).map(IEntityNpcData::getSpawnID).orElse(castingEntity.getUniqueID());
                Entity entity = def.createEntity(castingEntity.getEntityWorld(), target.getPosition().get(), id);
                MKPet<MKEntity> pet = MKPet.makePetFromEntity(MKEntity.class, getAbilityId(), entity);
                if (pet.getEntity() != null) {
                    casterData.getPets().addPet(pet);
                    castingEntity.getEntityWorld().addEntity(pet.getEntity());
                    pet.getEntity().setNoncombatBehavior(new PetNonCombatBehavior(castingEntity));
                    pet.getEntity().setNonCombatMoveType(MKEntity.NonCombatMoveType.STATIONARY);
                    MKNpc.getNpcData(pet.getEntity()).ifPresent(x -> x.setMKSpawned(true));
                    pet.getEntity().getCapability(FactionCapabilities.MOB_FACTION_CAPABILITY).ifPresent(x -> x.setFactionName(MKFaction.INVALID_FACTION));
                    ITextComponent newName = new TranslationTextComponent("mkultra.pet_name_format", castingEntity.getName(), pet.getEntity().getName());
                    pet.getEntity().setCustomName(newName);
                } else {
                    if (entity != null) {
                        entity.remove();
                    }
                    MKUltra.LOGGER.error("Summon Ability {} failed to cast npc: {} to a MKEntity", getAbilityId(), npcDefintion.getValue());
                }
            } else {
                MKUltra.LOGGER.error("Summon Ability {} Failed to summon npc: {}, definition invalid.", getAbilityId(), npcDefintion.getValue());
            }
        } else {
            if (target.getEntity().isPresent()) {
                LivingEntity tar = target.getEntity().get();
                casterData.getPets().getPet(getAbilityId()).ifPresent(x -> {
                    if (tar.equals(x.getEntity())) {
                        if (castingEntity.isSneaking()) {
                            x.getEntity().remove();
                            casterData.getPets().removePet(x);
                        } else {
                            x.getEntity().setNoncombatBehavior(new PetNonCombatBehavior(castingEntity));
                        }
                    } else {
                        if (x.getEntity() != null) {
                            if (Targeting.isValidEnemy(castingEntity, tar)) {
                                float newThreat = x.getEntity().getHighestThreat() + 500.0f;
                                x.getEntity().addThreat(tar, newThreat, true);
                                x.getEntity().getBrain().removeMemory(MKMemoryModuleTypes.SPAWN_POINT);
                                if (x.getEntity() instanceof MobEntity) {
                                    ((MobEntity) x.getEntity()).getNavigator().clearPath();
                                }
                                x.getEntity().enterCombatMovementState(tar);

                            } else if (Targeting.isValidFriendly(castingEntity, tar)) {
                                x.getEntity().setNoncombatBehavior(new PetNonCombatBehavior(tar));
                            }
                        }
                    }
                });
            } else if (target.getPosition().isPresent()) {
                Vector3d pos = target.getPosition().get();
                casterData.getPets().getPet(getAbilityId()).ifPresent(
                        x -> {
                            if (x.getEntity() != null) {
                                x.getEntity().setNoncombatBehavior(new PetNonCombatBehavior(pos));
                                x.getEntity().clearThreat();
                            }
                        });
            }
        }
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class RegisterMe {
        @SubscribeEvent
        public static void register(RegistryEvent.Register<MKAbility> event) {
            event.getRegistry().register(TEST_SUMMON);
        }
    }
}
