package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.network.packets.AbilityCooldownPacket;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;
import java.util.Map;

public class AbilityTracker {

    private int ticks;
    private final Map<PlayerAbilityInfo, Cooldown> cooldowns = Maps.newHashMap();

    public boolean hasCooldown(PlayerAbilityInfo info) {
        return getCooldownTicks(info) > 0;
    }

    public float getCooldown(PlayerAbilityInfo itemIn, float partialTicks) {
        Cooldown cd = this.cooldowns.get(itemIn);

        if (cd != null) {
            float totalCooldown = (float) (cd.expireTicks - cd.createTicks);
            float currentCooldown = (float) cd.expireTicks - ((float) this.ticks + partialTicks);
            return MathHelper.clamp(currentCooldown / totalCooldown, 0.0F, 1.0F);
        } else {
            return 0.0F;
        }
    }

    public int getCooldownTicks(PlayerAbilityInfo itemIn) {
        Cooldown cd = this.cooldowns.get(itemIn);

        if (cd != null) {
            return Math.max(0, cd.expireTicks - this.ticks);
        } else {
            return 0;
        }
    }

    public void tick() {
        ticks++;

        if (!this.cooldowns.isEmpty()) {
            Iterator<Map.Entry<PlayerAbilityInfo, Cooldown>> iterator = this.cooldowns.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<PlayerAbilityInfo, Cooldown> entry = iterator.next();

                if (entry.getValue().expireTicks <= this.ticks) {
                    iterator.remove();
                    this.notifyOnRemove(entry.getKey());
                }
            }
        }
    }

    public void setCooldown(PlayerAbilityInfo info, int ticksIn) {
        this.cooldowns.put(info, new Cooldown(this.ticks, this.ticks + ticksIn));
        this.notifyOnSet(info, ticksIn);
    }

    public void removeCooldown(PlayerAbilityInfo info) {
        this.cooldowns.remove(info);
        this.notifyOnRemove(info);
    }

    protected void notifyOnSet(PlayerAbilityInfo info, int ticksIn) {
    }

    protected void notifyOnRemove(PlayerAbilityInfo info) {
    }

    class Cooldown {
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

        protected void notifyOnSet(PlayerAbilityInfo itemIn, int ticksIn) {
            super.notifyOnSet(itemIn, ticksIn);
            MKUltra.packetHandler.sendTo(new AbilityCooldownPacket(itemIn.getId(), ticksIn), player);
        }

        protected void notifyOnRemove(PlayerAbilityInfo itemIn) {
            super.notifyOnRemove(itemIn);
            MKUltra.packetHandler.sendTo(new AbilityCooldownPacket(itemIn.getId(), 0), player);
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
