package com.chaosbuffalo.mkultra.spawner;

import com.chaosbuffalo.mkultra.choice.RandomCollection;
import net.minecraft.entity.EntityLivingBase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.BiFunction;

public class ItemOption extends MobOption {

    private final BiFunction<EntityLivingBase, ItemChoice, Boolean> applyFunc;
    public final HashSet<ItemChoice> choices;

    public ItemOption(BiFunction<EntityLivingBase, ItemChoice, Boolean> func, ItemChoice... choices){
        this.applyFunc = func;
        this.choices = new HashSet<ItemChoice>();
        this.choices.addAll(Arrays.asList(choices));
    }

    @Override
    public void apply(EntityLivingBase entity, int level, int maxLevel) {
        this.level = level;
        this.maxLevel = maxLevel;
        RandomCollection<ItemChoice> current_choices = new RandomCollection<>();
        for (ItemChoice choice : choices){
            if (level >= choice.minLevel){
                current_choices.add(choice.weight, choice);
            }
        }
        this.applyFunc.apply(entity, current_choices.next());
    }
}
