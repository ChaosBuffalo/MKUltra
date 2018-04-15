package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import com.chaosbuffalo.mkultra.network.packets.client.ExecuteActivePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientKeyHandler {

    private static KeyBinding skillSelectBind;
    private static KeyBinding[] abilityBinds;

    private static int currentGCDTicks;
    private static final int totalGCDTicks = 1 * GameConstants.TICKS_PER_SECOND;

    public static void initKeybinds() {
        skillSelectBind = new KeyBinding("key.hud.classpanel", Keyboard.KEY_J, "key.mkultra.category");
        ClientRegistry.registerKeyBinding(skillSelectBind);

        abilityBinds = new KeyBinding[GameConstants.ACTION_BAR_SIZE];
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            String bindName = String.format("key.hud.ability%d", i + 1);
            int key = Keyboard.KEY_1 + i;
            KeyBinding bind = new KeyBinding(bindName, KeyConflictContext.IN_GAME, KeyModifier.ALT, key, "key.mkultra.abilitybar");

            ClientRegistry.registerKeyBinding(bind);
            abilityBinds[i] = bind;
        }
    }

    public static float getGlobalCooldown() {
        return (float) currentGCDTicks / GameConstants.TICKS_PER_SECOND;
    }

    public static float getTotalGlobalCooldown() {
        return (float) totalGCDTicks / GameConstants.TICKS_PER_SECOND;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onKeyEvent(InputEvent.KeyInputEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player == null)
            return;

        IPlayerData pData = MKUPlayerData.get(player);
        if (pData == null || !pData.hasChosenClass()) {
            return;
        }

        if (skillSelectBind.isPressed()) {
            World world = player.getEntityWorld();
            player.openGui(MKUltra.INSTANCE, ModGuiHandler.CLASS_DATA_SCREEN, world,
                    (int) player.posX, (int) player.posY, (int) player.posZ);
        }

        if (currentGCDTicks == 0) {

            for (int i = 0; i < abilityBinds.length; i++) {
                KeyBinding bind = abilityBinds[i];
                if (!bind.isKeyDown()) {
                    continue;
                }

                ResourceLocation abilityId = pData.getAbilityInSlot(i);
                BaseAbility ability = ClassData.getAbility(abilityId);
                if (ability == null)
                    continue;

                if (ability.meetsRequirements(pData)) {
                    MKUltra.packetHandler.sendToServer(new ExecuteActivePacket(i));
                    currentGCDTicks = totalGCDTicks;
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (currentGCDTicks > 0) {
                currentGCDTicks--;
            }
        }
    }
}
