package com.chaosbuffalo.mkultra.client.audio;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.utils.MathUtils;
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
    private int castTime;

    public MovingSoundCasting(EntityLivingBase caster, SoundEvent event, SoundCategory category, int castTime) {
        super(event, category);
        this.caster = caster;
        this.repeat = true;
        this.repeatDelay = 0;
        this.castTime = castTime;
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
                    } else {
                        int currentCast = data.getCastTicks();
                        int lerpTime = (int) (castTime * .2f);
                        int timeCasting = castTime - currentCast;
                        int fadeOutPoint = castTime - lerpTime;
                        if (timeCasting <= lerpTime){
                            volume = MathUtils.lerp(0.0f, 1.0f,
                                    (float) timeCasting / (float) lerpTime);
                        } else if (timeCasting >= fadeOutPoint) {
                            volume = MathUtils.lerp(1.0f, 0.0f,
                                    (float) (timeCasting - fadeOutPoint) / (float) lerpTime);
                        }
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
