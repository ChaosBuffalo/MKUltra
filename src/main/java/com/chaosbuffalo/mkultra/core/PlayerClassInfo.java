package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.core.talents.*;
import com.chaosbuffalo.mkultra.log.Log;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.*;

public class PlayerClassInfo {
    public ResourceLocation classId;
    public int level;
    public int unspentPoints;
    public int totalTalentPoints;
    public int unspentTalentPoints;
    public HashMap<ResourceLocation, TalentTreeRecord> talentTrees;
    private ResourceLocation[] hotbar;
    private Deque<ResourceLocation> spendOrder;
    private ResourceLocation[] loadedPassives;

    public PlayerClassInfo(ResourceLocation classId) {
        this.classId = classId;
        this.level = 1;
        this.unspentPoints = 1;
        this.totalTalentPoints = 0;
        this.unspentTalentPoints = 0;
        loadedPassives = new ResourceLocation[GameConstants.MAX_PASSIVES];
        Arrays.fill(loadedPassives, MKURegistry.INVALID_ABILITY);
        hotbar = new ResourceLocation[GameConstants.ACTION_BAR_SIZE];
        Arrays.fill(hotbar, MKURegistry.INVALID_ABILITY);
        spendOrder = new ArrayDeque<>(GameConstants.MAX_CLASS_LEVEL);
        talentTrees = new HashMap<>();
        for (TalentTree tree : MKURegistry.REGISTRY_TALENT_TREES.getValuesCollection()) {
            talentTrees.put(tree.getRegistryName(), new TalentTreeRecord(tree));
        }
    }

    private ResourceLocation[] parseNBTAbilityArray(NBTTagCompound tag, String name, int size) {
        NBTTagList list = tag.getTagList(name, Constants.NBT.TAG_STRING);
        ResourceLocation[] arr = new ResourceLocation[size];
        Arrays.fill(arr, MKURegistry.INVALID_ABILITY);
        for (int i = 0; i < size && i < list.tagCount(); i++) {
            arr[i] = new ResourceLocation(list.getStringTagAt(i));
        }
        return arr;
    }

    public void applyPassives(EntityPlayer player, IPlayerData data, World world) {
        Log.debug("applyPassives - loadedPassives %s %s", loadedPassives[0], loadedPassives[1]);
        for (ResourceLocation loc : loadedPassives) {
            if (!loc.equals(MKURegistry.INVALID_ABILITY)) {
                PlayerAbility ability = MKURegistry.getAbility(loc);
                if (ability != null) {
                    ability.execute(player, data, world);
                }
            }
        }
    }

    public boolean addPassiveToSlot(ResourceLocation loc, int slotIndex) {
        if (canAddPassiveToSlot(loc, slotIndex)) {
            loadedPassives[slotIndex] = loc;
            for (int i = 0; i < GameConstants.MAX_PASSIVES; i++){
                if (i != slotIndex && loc.equals(loadedPassives[i])){
                    loadedPassives[i] = MKURegistry.INVALID_ABILITY;
                }
            }
            return true;
        }
        return false;
    }

    public void clearPassiveSlot(int slotIndex) {
        loadedPassives[slotIndex] = MKURegistry.INVALID_ABILITY;
    }

    public ResourceLocation getPassiveForSlot(int slotIndex) {
        if (slotIndex >= GameConstants.MAX_PASSIVES) {
            return MKURegistry.INVALID_ABILITY;
        }
        return loadedPassives[slotIndex];
    }

    public boolean canAddPassiveToSlot(ResourceLocation loc, int slotIndex) {
        return slotIndex < GameConstants.MAX_PASSIVES && hasTrainedPassive(loc);
    }

    public HashSet<PlayerPassiveAbility> getPassiveAbilitiesFromTalents() {
        HashSet<PlayerPassiveAbility> abilities = new HashSet<>();
        for (TalentTreeRecord rec : talentTrees.values()) {
            if (rec.hasPointsInTree()) {
                rec.getPassivesWithPoints().forEach(talent -> abilities.add(talent.getAbility()));
            }
        }
        return abilities;
    }

    public boolean hasTrainedPassive(ResourceLocation loc) {
        HashSet<PlayerPassiveAbility> abilities = getPassiveAbilitiesFromTalents();
        PlayerAbility ability = MKURegistry.getAbility(loc);
        return ability instanceof PlayerPassiveAbility && abilities.contains(ability);
    }

    public HashSet<RangedAttributeTalent> getAttributeTalentSet() {
        HashSet<RangedAttributeTalent> attributeTalents = new HashSet<>();
        for (TalentTreeRecord rec : talentTrees.values()) {
            if (rec.hasPointsInTree()) {
                attributeTalents.addAll(rec.getAttributeTalentsWithPoints());
            }
        }
        return attributeTalents;
    }

    public Map<IAttribute, AttributeModifier> getAttributeModifiers() {
        HashSet<RangedAttributeTalent> presentTalents = getAttributeTalentSet();
        Map<IAttribute, AttributeModifier> attributeModifierMap = Maps.newHashMap();
        for (RangedAttributeTalent talent : presentTalents) {
            double value = 0.0;
            for (TalentTreeRecord rec : talentTrees.values()) {
                if (rec.hasPointsInTree()) {
                    value += rec.getTotalForAttributeTalent(talent);
                }
            }
//            Log.info("Total for attribute talent: %s, %f", talent.getRegistryName().toString(), value);
            attributeModifierMap.put(talent.getAttribute(), talent.createModifier(value));
        }
        return attributeModifierMap;
    }

    public void applyAttributesModifiersToPlayer(EntityPlayer player) {
        AbstractAttributeMap attributeMap = player.getAttributeMap();

        for (Map.Entry<IAttribute, AttributeModifier> entry : getAttributeModifiers().entrySet()) {
            IAttributeInstance instance = attributeMap.getAttributeInstance(entry.getKey());
            if (instance != null) {
                AttributeModifier attributemodifier = entry.getValue();
                instance.removeModifier(attributemodifier);
                instance.applyModifier(attributemodifier);
            }
        }
    }

    public void removeAttributesModifiersFromPlayer(EntityPlayer player) {
        AbstractAttributeMap attributeMap = player.getAttributeMap();

        for (RangedAttributeTalent entry : getAttributeTalentSet()) {
            IAttributeInstance instance = attributeMap.getAttributeInstance(entry.getAttribute());
            if (instance != null) {
                instance.removeModifier(entry.getUUID());
            }
        }
    }

    private void writeNBTAbilityArray(NBTTagCompound tag, String name, Collection<ResourceLocation> array, int size) {
        NBTTagList list = new NBTTagList();
        if (array != null) {
            array.stream().limit(size).forEach(r -> list.appendTag(new NBTTagString(r.toString())));
        }
        tag.setTag(name, list);
    }

    public void writeTalentTrees(NBTTagCompound tag) {
        NBTTagCompound trees = new NBTTagCompound();
        boolean hadTalents = false;
        for (ResourceLocation loc : talentTrees.keySet()) {
            TalentTreeRecord record = talentTrees.get(loc);
            if (record.hasPointsInTree()) {
                trees.setTag(loc.toString(), record.toTag());
                hadTalents = true;
            }
        }
        if (hadTalents) {
            tag.setTag("trees", trees);
        }
    }

    public void parseTalentTrees(NBTTagCompound tag) {
        if (tag.hasKey("trees")) {
            NBTTagCompound trees = tag.getCompoundTag("trees");
            for (String key : trees.getKeySet()) {
                ResourceLocation loc = new ResourceLocation(key);
                if (talentTrees.containsKey(loc)) {
                    talentTrees.get(loc).fromTag(trees.getCompoundTag(key));
                }
            }
        }
    }

    public void serialize(NBTTagCompound tag) {
        tag.setString("id", classId.toString());
        tag.setInteger("level", level);
        tag.setInteger("unspentPoints", unspentPoints);
        writeNBTAbilityArray(tag, "spendOrder", spendOrder, GameConstants.MAX_CLASS_LEVEL);
        writeNBTAbilityArray(tag, "hotbar", Arrays.asList(hotbar), GameConstants.ACTION_BAR_SIZE);
        serializeTalentInfo(tag);
    }

    public void deserialize(NBTTagCompound tag) {
        classId = new ResourceLocation(tag.getString("id"));
        level = tag.getInteger("level");
        unspentPoints = tag.getInteger("unspentPoints");
        spendOrder = new ArrayDeque<>(Arrays.asList(parseNBTAbilityArray(tag, "spendOrder", GameConstants.MAX_CLASS_LEVEL)));
        setActiveAbilities(parseNBTAbilityArray(tag, "hotbar", GameConstants.ACTION_BAR_SIZE));
        deserializeTalentInfo(tag);
    }

    public void serializeTalentInfo(NBTTagCompound tag) {
        tag.setInteger("unspentTalentPoints", unspentTalentPoints);
        tag.setInteger("totalTalentPoints", totalTalentPoints);
        writeNBTAbilityArray(tag, "loadedPassives", Arrays.asList(loadedPassives), GameConstants.MAX_PASSIVES);
        writeTalentTrees(tag);
    }

    public void deserializeTalentInfo(NBTTagCompound tag) {
        unspentTalentPoints = tag.getInteger("unspentTalentPoints");
        totalTalentPoints = tag.getInteger("totalTalentPoints");
        if (tag.hasKey("loadedPassives")) {
            setLoadedPassives(parseNBTAbilityArray(tag, "loadedPassives", GameConstants.MAX_PASSIVES));
        }
        parseTalentTrees(tag);
    }

    public ResourceLocation[] getActivePassives() {
        return loadedPassives;
    }

    public ResourceLocation[] getActiveAbilities() {
        return hotbar;
    }

    public void addTalentPoints(int pointCount) {
        totalTalentPoints += pointCount;
        unspentTalentPoints += pointCount;
    }

    public boolean canIncrementPointInTree(ResourceLocation tree, String line, int index) {
        TalentTreeRecord talentTree = talentTrees.get(tree);
        return talentTree.containsIndex(line, index) && talentTree.canIncrementPoint(line, index);
    }

    public boolean canDecrementPointInTree(ResourceLocation tree, String line, int index) {
        TalentTreeRecord talentTree = talentTrees.get(tree);
        return talentTree.containsIndex(line, index) && talentTree.canDecrementPoint(line, index);
    }

    public boolean spendTalentPoint(EntityPlayer player, ResourceLocation tree, String line, int index) {
        if (canIncrementPointInTree(tree, line, index)) {
            TalentTreeRecord talentTree = talentTrees.get(tree);
            BaseTalent.TalentType type = talentTree.getTypeForPoint(line, index);
            if (type == BaseTalent.TalentType.ATTRIBUTE) {
                removeAttributesModifiersFromPlayer(player);
                talentTree.incrementPoint(line, index);
                applyAttributesModifiersToPlayer(player);
            } else {
                talentTree.incrementPoint(line, index);
            }
            return true;
        } else {
            return false;
        }
    }

    public int getTotalSpentPoints() {
        int tot = 0;
        for (TalentTreeRecord talentTree : talentTrees.values()) {
            tot += talentTree.getPointsInTree();
        }
        return tot;
    }

    public boolean refundTalentPoint(EntityPlayer player, ResourceLocation tree, String line, int index) {
        if (canDecrementPointInTree(tree, line, index)) {
            TalentTreeRecord talentTree = talentTrees.get(tree);
            BaseTalent talentDef = talentTree.getTalentDefinition(line, index);
            BaseTalent.TalentType type = talentDef.getTalentType();
            if (type == BaseTalent.TalentType.ATTRIBUTE) {
                removeAttributesModifiersFromPlayer(player);
                talentTree.decrementPoint(line, index);
                applyAttributesModifiersToPlayer(player);
            } else if (type == BaseTalent.TalentType.PASSIVE) {
                PassiveAbilityTalent passive = (PassiveAbilityTalent) talentDef;
                for (int i = 0; i < GameConstants.MAX_PASSIVES; i++) {
                    ResourceLocation current = getPassiveForSlot(i);
                    if (current.compareTo(passive.getAbility().getAbilityId()) == 0) {
                        clearPassiveSlot(i);
                        break;
                    }
                }
                talentTree.decrementPoint(line, index);
            } else {
                talentTree.decrementPoint(line, index);
            }
            return true;
        } else {
            return false;
        }
    }

    void setLoadedPassives(ResourceLocation[] passives) {
        this.loadedPassives = passives;
    }

    void setActiveAbilities(ResourceLocation[] hotbar) {
        this.hotbar = hotbar;
    }

    public void addToSpendOrder(ResourceLocation abilityId) {
        spendOrder.addFirst(abilityId);
    }

    public ResourceLocation getLastUpgradedAbility() {
        if (spendOrder.size() != 0) {
            return spendOrder.removeFirst();
        } else {
            return MKURegistry.INVALID_ABILITY;
        }
    }

    public TalentTreeRecord getTalentTree(ResourceLocation loc) {
        return talentTrees.get(loc);
    }
}
