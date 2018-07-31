package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.GameConstants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;

public class PlayerClassInfo {
    public ResourceLocation classId;
    public int level;
    public int unspentPoints;
    private ResourceLocation[] hotbar;
    private Deque<ResourceLocation> spendOrder;

    public PlayerClassInfo(ResourceLocation classId) {
        this.classId = classId;
        this.level = 1;
        this.unspentPoints = 1;
        hotbar = new ResourceLocation[GameConstants.ACTION_BAR_SIZE];
        Arrays.fill(hotbar, MKURegistry.INVALID_ABILITY);
        spendOrder = new ArrayDeque<>(GameConstants.MAX_CLASS_LEVEL);
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

    private void writeNBTAbilityArray(NBTTagCompound tag, String name, Collection<ResourceLocation> array, int size) {
        NBTTagList list = new NBTTagList();
        if (array != null) {
            array.stream().limit(size).forEach(r -> list.appendTag(new NBTTagString(r.toString())));
        }
        tag.setTag(name, list);
    }

    public void serialize(NBTTagCompound tag) {
        tag.setString("id", classId.toString());
        tag.setInteger("level", level);
        tag.setInteger("unspentPoints", unspentPoints);
        writeNBTAbilityArray(tag, "spendOrder", spendOrder, GameConstants.MAX_CLASS_LEVEL);
        writeNBTAbilityArray(tag, "hotbar", Arrays.asList(hotbar), GameConstants.ACTION_BAR_SIZE);
    }

    public void deserialize(NBTTagCompound tag) {
        classId = new ResourceLocation(tag.getString("id"));
        level = tag.getInteger("level");
        unspentPoints = tag.getInteger("unspentPoints");
        spendOrder = new ArrayDeque<>(Arrays.asList(parseNBTAbilityArray(tag, "spendOrder", GameConstants.MAX_CLASS_LEVEL)));
        setActiveAbilities(parseNBTAbilityArray(tag, "hotbar", GameConstants.ACTION_BAR_SIZE));
    }

    public ResourceLocation[] getActiveAbilities() {
        return hotbar;
    }

    void setActiveAbilities(ResourceLocation[] hotbar) {
        this.hotbar = hotbar;
    }

    public void addToSpendOrder(ResourceLocation abilityId) {
        spendOrder.addFirst(abilityId);
    }

    public ResourceLocation getLastLeveledAbility() {
        if (spendOrder.size() != 0) {
            return spendOrder.removeFirst();
        }
        else {
            return MKURegistry.INVALID_ABILITY;
        }
    }
}
