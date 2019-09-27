package com.chaosbuffalo.mkultra.spawn;

import com.chaosbuffalo.mkultra.utils.RandomCollection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.BiConsumer;

public class ItemOption extends IForgeRegistryEntry.Impl<ItemOption> {

    public int level;
    public int maxLevel;
    private final BiConsumer<EntityLivingBase, ItemChoice> applyFunc;
    public final HashSet<ItemChoice> choices;

    public ItemOption(ResourceLocation name,
                      BiConsumer<EntityLivingBase, ItemChoice> func,
                      Collection<ItemChoice> choices) {
        setRegistryName(name);
        this.applyFunc = func;
        this.choices = new HashSet<>();
        this.choices.addAll(choices);
    }

    public void apply(EntityLivingBase entity, int level, int maxLevel) {
        this.level = level;
        this.maxLevel = maxLevel;
        RandomCollection<ItemChoice> current_choices = new RandomCollection<>();
        for (ItemChoice choice : choices) {
            if (level >= choice.minLevel) {
                current_choices.add(choice.weight, choice);
            }
        }
        if (current_choices.size() > 0) {
            this.applyFunc.accept(entity, current_choices.next());
        }
    }
}
