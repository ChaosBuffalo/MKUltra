package com.chaosbuffalo.mkultra.command.subcommands;


import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.BaseClass;
import com.chaosbuffalo.mkultra.core.ClassData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import com.chaosbuffalo.mkultra.network.packets.server.ForceOpenClientGUIPacket;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClassCommand extends CommandBase {
    private static List<String> options = Arrays.asList("reset", "switch", "learn", "unlearn");

    /**
     * Callback for when the command is executed
     */
    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length == 0) {
            throw new WrongUsageException(getUsage(sender));
        }

        EntityPlayerMP playerIn = getCommandSenderAsPlayer(sender);
        PlayerData data = (PlayerData) MKUPlayerData.get(playerIn);
        if (data == null)
            return;

        String type = args[0].toLowerCase();

        switch (type) {
            case "reset":
                data.activateClass(ClassData.INVALID_CLASS);
                sender.sendMessage(new TextComponentString("Class reset"));
                break;
            case "switch":
                sender.sendMessage(new TextComponentString("Opening class switch GUI"));
                MKUltra.packetHandler.sendTo(new ForceOpenClientGUIPacket(ModGuiHandler.CHANGE_CLASS_SCREEN), playerIn);
                break;
            case "learn":
                sender.sendMessage(new TextComponentString("Opening class learn GUI"));
                MKUltra.packetHandler.sendTo(new ForceOpenClientGUIPacket(ModGuiHandler.LEARN_CLASS_SCREEN), playerIn);
                break;
            case "unlearn":
                if (data.hasChosenClass()) {
                    ResourceLocation currentClass = data.getClassId();
                    data.unlearnClass(currentClass);
                    BaseClass bc = ClassData.getClass(currentClass);
                    sender.sendMessage(new TextComponentString(String.format("Class '%s' unlearned", bc.getClassName())));
                }
                else {
                    sender.sendMessage(new TextComponentString("You do not have a class. Switch to the class you want to unlearn"));
                }
                break;
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
        return "class";
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

