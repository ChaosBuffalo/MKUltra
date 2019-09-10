package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.network.packets.AbilityCooldownPacket;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;
import java.util.Map;

public class AbilityTracker {

    private int ticks;
    private final Map<ResourceLocation, Cooldown> cooldowns = Maps.newHashMap();

    public boolean hasCooldown(ResourceLocation id) {
        return getCooldownTicks(id) > 0;
    }

    public float getCooldown(ResourceLocation id, float partialTicks) {
        Cooldown cd = this.cooldowns.get(id);

        if (cd != null) {
            float totalCooldown = (float) (cd.expireTicks - cd.createTicks);
            float currentCooldown = (float) cd.expireTicks - ((float) this.ticks + partialTicks);
            return MathHelper.clamp(currentCooldown / totalCooldown, 0.0F, 1.0F);
        } else {
            return 0.0F;
        }
    }

    public int getCooldownTicks(ResourceLocation id) {
        Cooldown cd = this.cooldowns.get(id);

        if (cd != null) {
            return Math.max(0, cd.expireTicks - this.ticks);
        } else {
            return 0;
        }
    }

    public void tick() {
        ticks++;

        if (!this.cooldowns.isEmpty()) {
            Iterator<Map.Entry<ResourceLocation, Cooldown>> iterator = this.cooldowns.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<ResourceLocation, Cooldown> entry = iterator.next();

                if (entry.getValue().expireTicks <= this.ticks) {
                    iterator.remove();
                    this.notifyOnRemove(entry.getKey());
                }
            }
        }
    }

    public void setCooldown(ResourceLocation id, int ticksIn) {
        this.cooldowns.put(id, new Cooldown(this.ticks, this.ticks + ticksIn));
        this.notifyOnSet(id, ticksIn);
    }

    public void removeCooldown(ResourceLocation id) {
        this.cooldowns.remove(id);
        this.notifyOnRemove(id);
    }

    protected void notifyOnSet(ResourceLocation id, int ticksIn) {
    }

    protected void notifyOnRemove(ResourceLocation id) {
    }

    static class Cooldown {
        final int createTicks;
        final int expireTicks;

        private Cooldown(int createTicksIn, int expireTicksIn) {
            this.createTicks = createTicksIn;
            this.expireTicks = expireTicksIn;
        }
    }

    static class AbilityTrackerServer extends AbilityTracker {

        private EntityPlayerMP player;

        public AbilityTrackerServer(EntityPlayerMP player) {
            this.player = player;
        }

        @Override
        protected void notifyOnSet(ResourceLocation id, int ticksIn) {
            super.notifyOnSet(id, ticksIn);
            MKUltra.packetHandler.sendTo(new AbilityCooldownPacket(id, ticksIn), player);
        }

        @Override
        protected void notifyOnRemove(ResourceLocation id) {
            super.notifyOnRemove(id);
            MKUltra.packetHandler.sendTo(new AbilityCooldownPacket(id, 0), player);
        }
    }

    public static AbilityTracker getTracker(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            return new AbilityTrackerServer((EntityPlayerMP) player);
        } else {
            return new AbilityTracker();
        }
    }
}
