package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.core.talents.BaseTalent;
import com.chaosbuffalo.mkultra.core.talents.RangedAttributeTalent;
import com.chaosbuffalo.mkultra.core.talents.TalentTree;
import com.chaosbuffalo.mkultra.core.talents.TalentTreeRecord;
import com.google.common.collect.Maps;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

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


    public PlayerClassInfo(ResourceLocation classId) {
        this.classId = classId;
        this.level = 1;
        this.unspentPoints = 1;
        this.totalTalentPoints = 0;
        this.unspentTalentPoints = 0;
        hotbar = new ResourceLocation[GameConstants.ACTION_BAR_SIZE];
        Arrays.fill(hotbar, MKURegistry.INVALID_ABILITY);
        spendOrder = new ArrayDeque<>(GameConstants.MAX_CLASS_LEVEL);
        talentTrees = new HashMap<>();
        for (TalentTree tree : MKURegistry.REGISTRY_TALENT_TREES.getValuesCollection()){
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

    public HashSet<RangedAttributeTalent> getAttributeTalentSet(){
        HashSet<RangedAttributeTalent> attributeTalents = new HashSet<>();
        for (ResourceLocation loc : talentTrees.keySet()){
            TalentTreeRecord rec = talentTrees.get(loc);
            if (rec.hasPointsInTree()){
                attributeTalents.addAll(rec.getAttributeTalentsWithPoints());
            }
        }
        return attributeTalents;
    }

    public Map<IAttribute, AttributeModifier> getAttributeModifiersForRemoval() {
        HashSet<RangedAttributeTalent> presentTalents = getAttributeTalentSet();
        Map<IAttribute, AttributeModifier> attributeModifierMap = Maps.newHashMap();
        for (RangedAttributeTalent talent : presentTalents) {
            AttributeModifier mod = new AttributeModifier(talent.getUUID(),
                    talent.getRegistryName().toString(), 1.0, talent.getOp());
            attributeModifierMap.put(talent.getAttribute(), mod);
        }
        return attributeModifierMap;
    }

    public Map<IAttribute, AttributeModifier> getAttributeModifiers(){
        HashSet<RangedAttributeTalent> presentTalents = getAttributeTalentSet();
        Map<IAttribute, AttributeModifier> attributeModifierMap = Maps.newHashMap();
        for (RangedAttributeTalent talent : presentTalents){
            double value = 0.0;
            for (ResourceLocation loc : talentTrees.keySet()){
                TalentTreeRecord rec = talentTrees.get(loc);
                if (rec.hasPointsInTree()){
                    value += rec.getTotalForAttributeTalent(talent);
                }
            }
            AttributeModifier mod = new AttributeModifier(talent.getUUID(),
                    talent.getRegistryName().toString(), value, talent.getOp());
            attributeModifierMap.put(talent.getAttribute(), mod);
        }
        return attributeModifierMap;
    }

    public void applyAttributesModifiersToPlayer(EntityPlayer player) {
        Iterator modIterator = getAttributeModifiers().entrySet().iterator();
        AbstractAttributeMap abstractAttributeMap = player.getAttributeMap();

        while(modIterator.hasNext()) {
            Map.Entry<IAttribute, AttributeModifier> entry = (Map.Entry)modIterator.next();
            IAttributeInstance iattributeinstance = abstractAttributeMap.getAttributeInstance(entry.getKey());
            if (iattributeinstance != null) {
                AttributeModifier attributemodifier = entry.getValue();
                iattributeinstance.applyModifier(attributemodifier);
            }
        }
    }

    public void removeAttributesModifiersFromPlayer(EntityPlayer player) {
        Iterator modIterator = getAttributeModifiersForRemoval().entrySet().iterator();
        AbstractAttributeMap abstractAttributeMap = player.getAttributeMap();

        while(modIterator.hasNext()) {
            Map.Entry<IAttribute, AttributeModifier> entry = (Map.Entry)modIterator.next();
            IAttributeInstance iattributeinstance = abstractAttributeMap.getAttributeInstance(entry.getKey());
            if (iattributeinstance != null) {
                iattributeinstance.removeModifier(entry.getValue());
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

    public void writeTalentTrees(NBTTagCompound tag){
        NBTTagCompound trees = new NBTTagCompound();
        boolean hadTalents = false;
        for (ResourceLocation loc : talentTrees.keySet()){
            TalentTreeRecord record = talentTrees.get(loc);
            if (record.hasPointsInTree()){
                trees.setTag(loc.toString(), record.toTag());
                hadTalents = true;
            }
        }
        if (hadTalents){
            tag.setTag("trees", trees);
        }
    }

    public void parseTalentTrees(NBTTagCompound tag){
        if (tag.hasKey("trees")){
            NBTTagCompound trees = tag.getCompoundTag("trees");
            for (String key : trees.getKeySet()){
                ResourceLocation loc = new ResourceLocation(key);
                if (talentTrees.containsKey(loc)){
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

    public void serializeTalentInfo(NBTTagCompound tag){
        tag.setInteger("unspentTalentPoints", unspentTalentPoints);
        tag.setInteger("totalTalentPoints", totalTalentPoints);
        writeTalentTrees(tag);
    }

    public void deserializeTalentInfo(NBTTagCompound tag){
        unspentTalentPoints = tag.getInteger("unspentTalentPoints");
        totalTalentPoints = tag.getInteger("totalTalentPoints");
        parseTalentTrees(tag);
    }

    public ResourceLocation[] getActiveAbilities() {
        return hotbar;
    }

    public void addTalentPoints(int pointCount) {
        totalTalentPoints += pointCount;
        unspentTalentPoints += pointCount;
    }

    public boolean canIncrementPointInTree(ResourceLocation tree, String line, int index){
        TalentTreeRecord talentTree = talentTrees.get(tree);
        return talentTree.containsIndex(line, index) && talentTree.canIncrementPoint(line, index);
    }

    public boolean canDecrementPointInTree(ResourceLocation tree, String line, int index){
        TalentTreeRecord talentTree = talentTrees.get(tree);
        return talentTree.containsIndex(line, index) && talentTree.canDecrementPoint(line, index);
    }

    public boolean spendTalentPoint(EntityPlayer player, ResourceLocation tree, String line, int index){
        if (canIncrementPointInTree(tree, line, index)){
            TalentTreeRecord talentTree = talentTrees.get(tree);
            BaseTalent.TalentType type = talentTree.getTypeForPoint(line, index);
            if (type == BaseTalent.TalentType.ATTRIBUTE){
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

    public int getTotalSpentPoints(){
        int tot = 0;
        for (TalentTreeRecord talentTree : talentTrees.values()){
            tot += talentTree.getPointsInTree();
        }
        return tot;
    }

    public boolean refundTalentPoint(EntityPlayer player, ResourceLocation tree, String line, int index){
        if (canDecrementPointInTree(tree, line, index)){
            TalentTreeRecord talentTree = talentTrees.get(tree);
            BaseTalent.TalentType type = talentTree.getTypeForPoint(line, index);
            if (type == BaseTalent.TalentType.ATTRIBUTE){
                removeAttributesModifiersFromPlayer(player);
                talentTree.decrementPoint(line, index);
                applyAttributesModifiersToPlayer(player);
            } else {
                talentTree.decrementPoint(line, index);
            }
            return true;
        } else {
            return false;
        }
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
        }
        else {
            return MKURegistry.INVALID_ABILITY;
        }
    }
}
