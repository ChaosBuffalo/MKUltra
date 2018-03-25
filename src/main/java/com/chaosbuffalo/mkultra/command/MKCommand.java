package com.chaosbuffalo.mkultra.command;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import com.chaosbuffalo.mkultra.network.packets.server.ForceOpenClientGUIPacket;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MKCommand extends CommandTreeBase {

    public MKCommand() {
        addSubcommand(new StatCommand());
        addSubcommand(new ClassCommand());
        addSubcommand(new CooldownCommand());
    }

    @Nonnull
    @Override
    public String getName() {
        return "mk";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender) {
        return "/mk";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }


    private static class StatCommand extends CommandBase
    {
        static List<String> stats = Arrays.asList("mana", "manaregen", "cdr");

        /**
         * Callback for when the command is executed
         */
        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
            if (args.length == 0) {
                throw new WrongUsageException(getUsage(sender));
            }

            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            IPlayerData data = PlayerDataProvider.get(player);
            if (data == null)
                return;

            String type = args[0].toLowerCase();
            String message;

            if (type.equals("mana")) {
                if (args.length == 1) {
                    message = String.format("You have %d/%d mana", data.getMana(), data.getTotalMana());
                } else {
                    int mana = parseInt(args[1]);
                    if (mana > data.getTotalMana()) {
                        data.setTotalMana(mana);
                    }
                    data.setMana(mana);
                    message = String.format("Mana set to %d", mana);
                }
                sender.sendMessage(new TextComponentString(message));
            } else if (type.equals("manaregen")) {
                if (args.length == 1) {
                    float rate = data.getManaRegenRate();
                    message = String.format("Mana regen rate: %f", rate);
                } else {
                    float mana = (float)parseDouble(args[1]);
                    data.setManaRegen(mana);
                    message = String.format("Mana regen rate set to %f", mana);
                }
                sender.sendMessage(new TextComponentString(message));
            } else if (type.equals("cdr")) {
                float attrVal = (float)player.getEntityAttribute(PlayerAttributes.COOLDOWN).getAttributeValue();
                float baseVal = (float)player.getEntityAttribute(PlayerAttributes.COOLDOWN).getBaseValue();
                message = String.format("Cooldown rate %f base %f", attrVal, baseVal);
                sender.sendMessage(new TextComponentString(message));
            }
        }

        /**
         * Get a list of options for when the user presses the TAB key
         */
        @Nonnull
        @Override
        public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
        {
            return stats;
        }

        /**
         * Gets the name of the command
         */
        @Nonnull
        @Override
        public String getName()
        {
            return "stat";
        }

        /**
         * Return the required permission level for this command.
         */
        @Override
        public int getRequiredPermissionLevel()
        {
            return 2;
        }

        /**
         * Gets the usage string for the command.
         */
        @Nonnull
        @Override
        public String getUsage(@Nonnull ICommandSender sender)
        {
            return String.format("/mk %s <%s>", getName(), stats.stream().collect(Collectors.joining("|")));
        }
    }

    private static class ClassCommand extends CommandBase
    {
        static List<String> options = Arrays.asList("reset", "switch", "learn");

        /**
         * Callback for when the command is executed
         */
        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
            if (args.length == 0) {
                throw new WrongUsageException(getUsage(sender));
            }

            EntityPlayerMP playerIn = getCommandSenderAsPlayer(sender);
            IPlayerData data = PlayerDataProvider.get(playerIn);
            if (data == null)
                return;

            String type = args[0].toLowerCase();

            if (type.equals("reset")) {
                data.activateClass(ClassData.INVALID_CLASS);
                sender.sendMessage(new TextComponentString("Class reset"));
            } else if (type.equals("switch")) {
                sender.sendMessage(new TextComponentString("Opening class switch GUI"));
                MKUltra.packetHandler.sendTo(new ForceOpenClientGUIPacket(ModGuiHandler.CHANGE_CLASS_SCREEN), playerIn);
            } else if (type.equals("learn")) {
                sender.sendMessage(new TextComponentString("Opening class learn GUI"));
                MKUltra.packetHandler.sendTo(new ForceOpenClientGUIPacket(ModGuiHandler.LEARN_CLASS_SCREEN), playerIn);
            }

        }

        /**
         * Get a list of options for when the user presses the TAB key
         */
        @Nonnull
        @Override
        public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
        {
            return options;
        }

        /**
         * Gets the name of the command
         */
        @Nonnull
        @Override
        public String getName()
        {
            return "class";
        }

        /**
         * Return the required permission level for this command.
         */
        @Override
        public int getRequiredPermissionLevel()
        {
            return 2;
        }

        /**
         * Gets the usage string for the command.
         */
        @Nonnull
        @Override
        public String getUsage(@Nonnull ICommandSender sender)
        {
            return String.format("/mk %s <%s>", getName(), options.stream().collect(Collectors.joining("|")));
        }
    }


    private static class CooldownCommand extends CommandBase
    {
        static List<String> options = Arrays.asList("dump", "reset");

        /**
         * Callback for when the command is executed
         */
        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
            if (args.length == 0) {
                throw new WrongUsageException(getUsage(sender));
            }

            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            IPlayerData data = PlayerDataProvider.get(player);
            if (data == null)
                return;

            PlayerData playerData = (PlayerData)data;

            String type = args[0].toLowerCase();

            if (type.equals("dump")) {
                playerData.debugDumpAllAbilities(sender);
            } else if (type.equals("reset")) {
                playerData.debugResetAllCooldowns();
            }
        }

        /**
         * Get a list of options for when the user presses the TAB key
         */
        @Nonnull
        @Override
        public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
        {
            return options;
        }

        /**
         * Gets the name of the command
         */
        @Nonnull
        @Override
        public String getName()
        {
            return "cd";
        }

        /**
         * Return the required permission level for this command.
         */
        @Override
        public int getRequiredPermissionLevel()
        {
            return 2;
        }

        /**
         * Gets the usage string for the command.
         */
        @Nonnull
        @Override
        public String getUsage(@Nonnull ICommandSender sender)
        {
            return String.format("/mk %s <%s>", getName(), options.stream().collect(Collectors.joining("|")));
        }
    }
}
