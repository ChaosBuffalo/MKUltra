package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.core.IClassClientData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerClass;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class JsonConfiguredClass extends PlayerClass {

    private int baseHealth;
    private int healthPerLevel;
    private float baseManaRegen;
    private float manaRegenPerLevel;
    private int baseMana;
    private int manaPerLevel;
    private ArmorClass armorClass;
    private List<PlayerAbility> abilities;
    private IClassClientData clientData;


    public JsonConfiguredClass(ResourceLocation name){
        super(name);
        this.abilities = new ArrayList<>();
    }

    public void setBaseHealth(int value){
        this.baseHealth = value;
    }

    public void setHealthPerLevel(int value){
        this.healthPerLevel = value;
    }

    public void setBaseManaRegen(float value){
        this.baseManaRegen = value;
    }

    public void setManaRegenPerLevel(float value){
        this.manaRegenPerLevel = value;
    }

    public void setBaseMana(int value){
        this.baseMana = value;
    }

    public void setManaPerLevel(int value){
        this.manaPerLevel = value;
    }

    public void setArmorClass(ArmorClass value){
        this.armorClass = value;
    }

    public void setClientData(IClassClientData data){
        this.clientData = data;
    }

    public void addAbility(PlayerAbility ability){
        this.abilities.add(ability);
    }

    @Override
    public int getBaseHealth() {
        return baseHealth;
    }

    @Override
    public int getHealthPerLevel() {
        return healthPerLevel;
    }

    @Override
    public float getBaseManaRegen() {
        return baseManaRegen;
    }

    @Override
    public float getManaRegenPerLevel() {
        return manaRegenPerLevel;
    }

    @Override
    public int getBaseMana() {
        return baseMana;
    }

    @Override
    public int getManaPerLevel() {
        return manaPerLevel;
    }


    @Override
    public IClassClientData getClientData() {
        return clientData;
    }

    @Override
    public ArmorClass getArmorClass() {
        return armorClass;
    }

    @Override
    protected List<PlayerAbility> getAbilities() {
        return abilities;
    }
}
