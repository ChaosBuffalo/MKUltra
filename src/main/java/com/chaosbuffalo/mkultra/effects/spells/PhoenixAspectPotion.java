package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class PhoenixAspectPotion extends SpellPotionBase {

    public static final UUID MODIFIER_ID = UUID.fromString("721f69b8-c361-4b80-897f-724f84e08ae7");

    public static final PhoenixAspectPotion INSTANCE = (PhoenixAspectPotion) new PhoenixAspectPotion()
            .registerPotionAttributeModifier(PlayerAttributes.COOLDOWN, MODIFIER_ID.toString(),
                    -0.33, PlayerAttributes.OP_INCREMENT)
            .registerPotionAttributeModifier(PlayerAttributes.MANA_REGEN, MODIFIER_ID.toString(),
                    1.0f, PlayerAttributes.OP_INCREMENT)
            ;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private PhoenixAspectPotion() {
        super(false, 4393423);
        setPotionName("effect.phoenix_aspect");
    }

    @Override
    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier)
    {
        return modifier.getAmount() * (double)(amplifier);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/phoenix_aspect.png");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

    }

    @Override
    public void onPotionAdd(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
        if (target instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) target;

            // Store original speed
            cast.setFloat("flySpeed", player.capabilities.getFlySpeed());

            player.capabilities.allowFlying = true;
            SPacketPlayerAbilities packet = new SPacketPlayerAbilities(player.capabilities);
            packet.setFlySpeed(.1f);
            player.connection.sendPacket(packet);
            player.sendPlayerAbilities();
        }
    }

    @Override
    public void onPotionRemove(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
        if (target instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) target;
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;

            SPacketPlayerAbilities packet = new SPacketPlayerAbilities(player.capabilities);
            packet.setFlySpeed(cast.getFloat("flySpeed", 0.05f));
            player.connection.sendPacket(packet);
            player.sendPlayerAbilities();
        }
    }


    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public boolean isReady(int duration, int amplitude) {
        return false;
    }

    @Override
    public boolean isInstant() {
        return false;
    }
}
