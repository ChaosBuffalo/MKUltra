package com.chaosbuffalo.mkultra.command.subcommands;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StatCommand extends CommandBase {
    private static List<String> stats = Arrays.asList("mana", "manaregen", "cdr");

    /**
     * Callback for when the command is executed
     */
    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length == 0) {
            throw new WrongUsageException(getUsage(sender));
        }

        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        IPlayerData data = MKUPlayerData.get(player);
        if (data == null)
            return;

        String type = args[0].toLowerCase();
        String message;

        switch (type) {
            case "mana": {
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
                break;
            }
            case "manaregen": {
                if (args.length == 1) {
                    float rate = data.getManaRegenRate();
                    message = String.format("Mana regen rate: %f", rate);
                } else {
                    float mana = (float) parseDouble(args[1]);
                    data.setManaRegen(mana);
                    message = String.format("Mana regen rate set to %f", mana);
                }
                sender.sendMessage(new TextComponentString(message));
                break;
            }
            case "cdr": {
                float attrVal = (float) player.getEntityAttribute(PlayerAttributes.COOLDOWN).getAttributeValue();
                float baseVal = (float) player.getEntityAttribute(PlayerAttributes.COOLDOWN).getBaseValue();
                message = String.format("Cooldown rate %f base %f", attrVal, baseVal);
                sender.sendMessage(new TextComponentString(message));
                break;
            }
            case "armor": {
                float attrVal = (float) player.getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue();
                float baseVal = (float) player.getEntityAttribute(SharedMonsterAttributes.ARMOR).getBaseValue();
                message = String.format("Armor value %f base %f", attrVal, baseVal);
                sender.sendMessage(new TextComponentString(message));
                break;
            }
        }
    }

    /**
     * Get a list of options for when the user presses the TAB key
     */
    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return stats;
    }

    /**
     * Gets the name of the command
     */
    @Nonnull
    @Override
    public String getName() {
        return "stat";
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
        return String.format("/mk %s <%s>", getName(), stats.stream().collect(Collectors.joining("|")));
    }
}
