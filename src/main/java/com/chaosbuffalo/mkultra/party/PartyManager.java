package com.chaosbuffalo.mkultra.party;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class PartyManager {

    private static MinecraftServer server;

    public PartyManager() {
    }

    private static MinecraftServer getServer() {
        if (server == null)
            server = FMLCommonHandler.instance().getMinecraftServerInstance();
        return server;
    }

    private static Scoreboard getScoreboard() {
        return getServer().getWorld(0).getScoreboard();
    }

    private static ScorePlayerTeam getTeam(Scoreboard scoreboard, String name) {
        ScorePlayerTeam team = scoreboard.getTeam(name);
        if (team == null) {
            team = scoreboard.createTeam(name);
        }
        team.setAllowFriendlyFire(false);
        team.setDisplayName(name);
        return team;
    }

    private static ScorePlayerTeam getTeamForPlayer(EntityPlayer player) {
        String name = "party" + DigestUtils.sha1Hex(player.getName());
        name = name.substring(0, 15);
        return getTeam(getScoreboard(), name);
    }

    public static void handleInviteAccept(EntityPlayer invitingPlayer, EntityPlayer acceptingPlayer) {
        Scoreboard board = getScoreboard();
        Team invitingTeam = invitingPlayer.getTeam();
        if (invitingTeam == null) {
            invitingTeam = getTeamForPlayer(invitingPlayer);
            board.addPlayerToTeam(invitingPlayer.getName(), invitingTeam.getName());
        }

        board.addPlayerToTeam(acceptingPlayer.getName(), invitingTeam.getName());
    }

    public static void removePlayerFromParty(EntityPlayer player) {
        Scoreboard board = getScoreboard();
        board.removePlayerFromTeams(player.getName());
        ScorePlayerTeam leavingTeam = (ScorePlayerTeam) player.getTeam();
        // If the team is now empty destroy it
        if (leavingTeam != null && leavingTeam.getMembershipCollection().size() == 0) {
            board.removeTeam(leavingTeam);
        }
    }

    public static ArrayList<EntityPlayer> getPlayersOnTeam(EntityPlayer player) {
        ArrayList<EntityPlayer> members = new ArrayList<>();
        Team team = player.getTeam();
        if (team != null) {
            for (String member : team.getMembershipCollection()) {
                EntityPlayer p = player.getEntityWorld().getPlayerEntityByName(member);
                if (p != null && !p.isEntityEqual(player)) {
                    members.add(p);
                }
            }
        }
        return members;
    }

    private static void gainBrouzoufs(EntityPlayer player) {
        if (player instanceof EntityPlayerMP && player.getEntityWorld().rand.nextInt(100) <= 5) {
            player.sendMessage(new TextComponentString("You gain brouzoufs"));
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onPickUpExp(PlayerPickupXpEvent e) {

        EntityPlayer original = e.getEntityPlayer();
        Team team = original.getTeam();

        if (team != null) {
            World world = original.getEntityWorld();

            EntityXPOrb orb = e.getOrb();
            world.playSound(null, orb.posX, orb.posY, orb.posZ,
                    SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                    SoundCategory.PLAYERS,
                    0.1F, 0.5F * ((original.getRNG().nextFloat() - original.getRNG().nextFloat()) * 0.7F + 1.8F));

            int exp = orb.getXpValue();
            ArrayList<EntityPlayer> members = new ArrayList<>();
            for (String member : team.getMembershipCollection()) {
                EntityPlayer p = world.getPlayerEntityByName(member);
                if (p != null) {
                    members.add(p);
                }
            }
            int pCount = members.size();
            int playerExp = Math.max(exp / pCount, 1);
            for (EntityPlayer p : members) {
                if (!p.equals(original)){
                    p.addExperience(playerExp);
                }
            }
            e.getOrb().xpValue = playerExp;
        }
    }
}
