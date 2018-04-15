package com.chaosbuffalo.mkultra.party;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.network.packets.server.PartyInvitePacket;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nonnull;

public class PartyCommand extends CommandTreeBase {

    public PartyCommand() {
        addSubcommand(new InviteCommand());
        addSubcommand(new LeaveCommand());
        addSubcommand(new InfoCommand());
    }

    @Nonnull
    @Override
    public String getName() {
        return "party";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender) {
        return "/party <invite|leave|info>";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    private static class InviteCommand extends CommandBase
    {
        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
            if (args.length == 0) {
                throw new WrongUsageException(getUsage(sender));
            }

            EntityPlayerMP target = getPlayer(server, sender, args[0]);
            EntityPlayer self = getCommandSenderAsPlayer(sender);
            MKUltra.packetHandler.sendTo(new PartyInvitePacket(self.getUniqueID(), self.getName()), target);
        }

        @Override
        public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
            return true;
        }

        @Override
        public boolean isUsernameIndex(String[] args, int index)
        {
            return index > 0;
        }

        @Nonnull
        @Override
        public String getName()
        {
            return "invite";
        }

        @Nonnull
        @Override
        public String getUsage(@Nonnull ICommandSender sender)
        {
            return "/party invite <player name>";
        }
    }

    private static class LeaveCommand extends CommandBase
    {
        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
            EntityPlayer self = getCommandSenderAsPlayer(sender);
            String message;
            if (self.getTeam() != null) {
                PartyManager.removePlayerFromParty(self);
                message = "You left the party";
            }
            else {
                message = "You are not in a party!";
            }

            self.sendMessage(new TextComponentString(message));
        }

        @Override
        public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
            return true;
        }

        /**
         * Gets the name of the command
         */
        @Nonnull
        @Override
        public String getName()
        {
            return "leave";
        }

        /**
         * Gets the usage string for the command.
         */
        @Nonnull
        @Override
        public String getUsage(@Nonnull ICommandSender sender)
        {
            return "/party leave";
        }
    }

    private static class InfoCommand extends CommandBase
    {
        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
            EntityPlayer self = getCommandSenderAsPlayer(sender);

            Team team = self.getTeam();
            if (team != null) {
                String message = String.format("Party: %s", team.getName());
                self.sendMessage(new TextComponentString(message));

                message = String.format("Members: %s", String.join(", ", team.getMembershipCollection()));
                self.sendMessage(new TextComponentString(message));
            }
            else {
                String message = "You are not in a party!";
                self.sendMessage(new TextComponentString(message));
            }
        }

        @Override
        public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
            return true;
        }

        /**
         * Gets the name of the command
         */
        @Nonnull
        @Override
        public String getName()
        {
            return "info";
        }

        /**
         * Gets the usage string for the command.
         */
        @Nonnull
        @Override
        public String getUsage(@Nonnull ICommandSender sender)
        {
            return "/party info";
        }
    }
}
