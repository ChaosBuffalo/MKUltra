package com.chaosbuffalo.mkultra.command.subcommands;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

import javax.annotation.Nonnull;

public class StatCommand extends CommandTreeBase {

    public StatCommand() {
        addSubcommand(new HealthStat());
        addSubcommand(new AttrStat("maxhealth", SharedMonsterAttributes.MAX_HEALTH));
        addSubcommand(new ManaStat());
        addSubcommand(new AttrStat("maxmana", PlayerAttributes.MAX_MANA));
        addSubcommand(new AttrStat("manaregen", PlayerAttributes.MANA_REGEN));
        addSubcommand(new AttrStat("healthregen", PlayerAttributes.HEALTH_REGEN));
        addSubcommand(new AttrStat("mcrit", PlayerAttributes.MELEE_CRIT));
        addSubcommand(new AttrStat("mdamage", PlayerAttributes.MELEE_CRITICAL_DAMAGE));
        addSubcommand(new AttrStat("scrit", PlayerAttributes.SPELL_CRIT));
        addSubcommand(new AttrStat("sdamage", PlayerAttributes.SPELL_CRITICAL_DAMAGE));
        addSubcommand(new AttrStat("cdr", PlayerAttributes.COOLDOWN));
        addSubcommand(new AttrStat("armor", SharedMonsterAttributes.ARMOR));
        addSubcommand(new AttrStat("healbonus", PlayerAttributes.HEAL_BONUS));
        addSubcommand(new AttrStat("buffduration", PlayerAttributes.BUFF_DURATION));
        addSubcommand(new CommandTreeHelp(this)); // MUST be last
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
     * Gets the usage string for the command.
     */
    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/mk stat";
    }

    static abstract class SingleStat extends CommandTreeBase {
        @Override
        public final void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            IPlayerData data = MKUPlayerData.get(player);
            if (data == null)
                return;
            subExecute(player, data, args);
        }

        @Nonnull
        @Override
        public String getUsage(@Nonnull ICommandSender sender) {
            return String.format("/mk stat %s [value]", getName());
        }

        protected void handleAttr(EntityPlayerMP player, IAttribute attr, String[] args) throws CommandException {
            if (args.length == 0) {
                float attrVal = (float) player.getEntityAttribute(attr).getAttributeValue();
                float baseVal = (float) player.getEntityAttribute(attr).getBaseValue();
                ITextComponent message = new TextComponentTranslation("attribute.name." + attr.getName())
                        .appendText(String.format(": %f base %f", attrVal, baseVal));
                player.sendMessage(message);
            } else {
                float newBase = (float) parseDouble(args[0]);
                player.getEntityAttribute(attr).setBaseValue(newBase);
                ITextComponent message = new TextComponentTranslation("attribute.name." + attr.getName())
                        .appendText(String.format(": set to %f", newBase));
                player.sendMessage(message);
            }
        }

        protected abstract void subExecute(EntityPlayerMP sender, IPlayerData data, String[] args) throws CommandException;
    }

    static class HealthStat extends SingleStat {

        @Override
        protected void subExecute(EntityPlayerMP sender, IPlayerData data, String[] args) throws CommandException {
            String message;
            if (args.length == 0) {
                message = String.format("You have %f / %f health", data.getHealth(), data.getTotalHealth());
            } else {
                float health = (float) parseDouble(args[0], 0, data.getTotalHealth());
                data.setHealth(health);
                message = String.format("Health set to %f", health);
            }
            sender.sendMessage(new TextComponentString(message));
        }

        @Override
        public String getName() {
            return "health";
        }
    }

    static class ManaStat extends SingleStat {

        @Override
        protected void subExecute(EntityPlayerMP sender, IPlayerData data, String[] args) throws CommandException {
            String message;
            if (args.length == 0) {
                message = String.format("You have %f / %f mana", data.getMana(), data.getTotalMana());
            } else {
                float mana = (float) parseDouble(args[0], 0, data.getTotalMana());
                data.setMana(mana);
                message = String.format("Mana set to %f", mana);
            }
            sender.sendMessage(new TextComponentString(message));
        }

        @Override
        public String getName() {
            return "mana";
        }
    }

    static class AttrStat extends SingleStat {
        IAttribute attr;
        String name;

        protected AttrStat(String name, IAttribute attr) {
            this.name = name;
            this.attr = attr;
        }

        @Override
        protected void subExecute(EntityPlayerMP sender, IPlayerData data, String[] args) throws CommandException {
            handleAttr(sender, attr, args);
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
