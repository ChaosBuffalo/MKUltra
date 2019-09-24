package com.chaosbuffalo.mkultra.client.audio;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class MovingSoundCasting extends MovingSound {
    private final EntityLivingBase caster;

    public MovingSoundCasting(EntityLivingBase caster, SoundEvent event, SoundCategory category) {
        super(event, category);
        this.caster = caster;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    public void update() {
        if (caster.isEntityAlive()) {
            if (caster instanceof EntityPlayer){
                IPlayerData data = MKUPlayerData.get((EntityPlayer) caster);
                if (data == null){
                    donePlaying = true;
                    return;
                } else {
                    if (!data.isCasting()){
                        donePlaying = true;
                        return;
                    }
                }
            }
            xPosF = (float)caster.posX;
            yPosF = (float)caster.posY;
            zPosF = (float)caster.posZ;
        } else {
            donePlaying = true;
        }
    }
}
