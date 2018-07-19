package com.chaosbuffalo.mkultra.command.subcommands;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EffectCommand extends CommandBase {
    private static List<String> options = Arrays.asList("dump", "clear");

    /**
     * Callback for when the command is executed
     */
    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length == 0) {
            throw new WrongUsageException(getUsage(sender));
        }
        String type = args[0].toLowerCase();

        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        IPlayerData data = MKUPlayerData.get(player);
        if (data == null)
            return;

        if (type.equals("dump")) {
            sender.sendMessage(new TextComponentString("Active Effects"));
            player.getActivePotionMap().forEach((p, e) -> {
                sender.sendMessage(new TextComponentString(e.toString()));
            });
        } else if (type.equals("clear")) {
            player.clearActivePotions();
            sender.sendMessage(new TextComponentString("Cleared all active effects"));
        }
    }

    /**
     * Get a list of options for when the user presses the TAB key
     */
    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return options;
    }

    /**
     * Gets the name of the command
     */
    @Nonnull
    @Override
    public String getName() {
        return "effect";
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     */
    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return String.format("/mk %s <%s>", getName(), options.stream().collect(Collectors.joining("|")));
    }
}
