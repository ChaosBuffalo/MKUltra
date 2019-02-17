package com.chaosbuffalo.mkultra.core;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

public abstract class BaseClass extends IForgeRegistryEntry.Impl<BaseClass> {

    private String className;
    private ResourceLocation classId;

    protected BaseClass(String modId, String pathName, String className) {
        this(new ResourceLocation(modId, pathName), className);
    }

    public BaseClass(ResourceLocation classId, String className) {
        this.classId = classId;
        this.className = className;
    }

    public ResourceLocation getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public abstract int getBaseHealth();

    public abstract int getHealthPerLevel();

    public abstract float getBaseManaRegen();

    public abstract float getManaRegenPerLevel();

    public abstract int getBaseMana();

    public abstract int getManaPerLevel();

    public abstract Item getUnlockItem();

    public abstract ArmorClass getArmorClass();

    public PlayerAbility getOfferedAbilityBySlot(int slotIndex) {
        List<PlayerAbility> abilities = getAbilities();
        if (slotIndex < abilities.size()) {
            return abilities.get(slotIndex);
        }
        return null;
    }

    protected abstract List<PlayerAbility> getAbilities();
}
