package com.chaosbuffalo.mkultra.command;

import com.chaosbuffalo.mkultra.command.subcommands.ClassCommand;
import com.chaosbuffalo.mkultra.command.subcommands.CooldownCommand;
import com.chaosbuffalo.mkultra.command.subcommands.EffectCommand;
import com.chaosbuffalo.mkultra.command.subcommands.StatCommand;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

import javax.annotation.Nonnull;

public class MKCommand extends CommandTreeBase {

    public MKCommand() {
        addSubcommand(new StatCommand());
        addSubcommand(new ClassCommand());
        addSubcommand(new CooldownCommand());
        addSubcommand(new EffectCommand());
        addSubcommand(new CommandTreeHelp(this)); // MUST be last
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
        return 2;
    }
}
