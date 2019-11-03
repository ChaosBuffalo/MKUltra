package com.chaosbuffalo.mkultra.core;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

public abstract class PlayerClass extends IForgeRegistryEntry.Impl<PlayerClass> {

    private ResourceLocation classId;

    protected PlayerClass(String modId, String pathName) {
        this(new ResourceLocation(modId, pathName));
    }

    public PlayerClass(ResourceLocation classId) {
        this.classId = classId;
    }

    public ResourceLocation getClassId() {
        return classId;
    }

    @SideOnly(Side.CLIENT)
    public String getClassName() {
        return I18n.format(getTranslationKey());
    }

    public String getTranslationKey() {
        return String.format("%s.%s.name", classId.getNamespace(), classId.getPath());
    }

    public abstract int getBaseHealth();

    public abstract int getHealthPerLevel();

    public abstract float getBaseManaRegen();

    public abstract float getManaRegenPerLevel();

    public abstract int getBaseMana();

    public String hashAbilities() {
        StringBuilder concatIds = new StringBuilder();
        for (PlayerAbility ability : getAbilities()){
            concatIds.append(ability.getAbilityId().toString());
        }
        return DigestUtils.sha1Hex(concatIds.toString());
    }

    public abstract int getManaPerLevel();

    public abstract IClassClientData getClientData();

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
