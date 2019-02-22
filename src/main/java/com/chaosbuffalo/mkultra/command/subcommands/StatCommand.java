package com.chaosbuffalo.mkultra.command.subcommands;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class StatCommand extends CommandBase {
    private static List<String> stats = Arrays.asList("mana", "manaregen", "cdr", "mcrit", "scrit", "armor");

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
                handleAttr(sender, player, PlayerAttributes.MANA_REGEN, args);
                break;
            }
            case "mcrit": {
                handleAttr(sender, player, PlayerAttributes.MELEE_CRIT, args);
                break;
            }
            case "scrit": {
                handleAttr(sender, player, PlayerAttributes.SPELL_CRIT, args);
                break;
            }
            case "cdr": {
                handleAttr(sender, player, PlayerAttributes.COOLDOWN, args);
                break;
            }
            case "armor": {
                handleAttr(sender, player, SharedMonsterAttributes.ARMOR, args);
                break;
            }
            case "healbonus": {
                handleAttr(sender, player, PlayerAttributes.HEAL_BONUS, args);
                break;
            }
        }
    }

    private void handleAttr(@Nonnull ICommandSender sender, EntityPlayerMP player, IAttribute attr, String[] args) throws CommandException {
        if (args.length == 1) {
            float attrVal = (float) player.getEntityAttribute(attr).getAttributeValue();
            float baseVal = (float) player.getEntityAttribute(attr).getBaseValue();
            ITextComponent message = new TextComponentTranslation("attribute.name." + attr.getName())
                    .appendText(String.format(": %f base %f", attrVal, baseVal));
            sender.sendMessage(message);
        } else if (args.length > 1) {
            float newBase = (float) parseDouble(args[1]);
            player.getEntityAttribute(attr).setBaseValue(newBase);
            ITextComponent message = new TextComponentTranslation("attribute.name." + attr.getName())
                    .appendText(String.format(": set to %f", newBase));
            sender.sendMessage(message);
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
        return String.format("/mk %s <%s>", getName(), String.join(" | ", stats));
    }
}
