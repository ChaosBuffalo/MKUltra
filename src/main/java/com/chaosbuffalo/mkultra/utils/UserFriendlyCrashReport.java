package com.chaosbuffalo.mkultra.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.minecraft.crash.CrashReport;

public class UserFriendlyCrashReport extends CrashReport {

	private String userInformation = "Unfortunately, no information on this error is provided.\nIf you know the creator of the mod that caused the error, you can tell him that its\nnot a nice thing not to tell the users what went wrong.";

	public UserFriendlyCrashReport(String descriptionIn, Throwable causeThrowable) {
		super(descriptionIn, causeThrowable);
	}

	@Override
	public String getCompleteReport()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("---- Minecraft Crash Report ----\n");
        stringbuilder.append("// ");
        stringbuilder.append(getWittyComment());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time: ");
        stringbuilder.append((new SimpleDateFormat()).format(new Date()));
        stringbuilder.append("\n");
        stringbuilder.append("Description: ");
        stringbuilder.append(this.getDescription());
        stringbuilder.append("\n\n");
        for (int i = 0; i < 87; ++i)
            stringbuilder.append("=");
        stringbuilder.append("\n\n~~~ WHAT TO DO NOW? ~~~\n\n");
        stringbuilder.append(this.userInformation);
        stringbuilder.append("\n\n");
        for (int i = 0; i < 87; ++i)
            stringbuilder.append("=");
        stringbuilder.append("\n\nException that caused this crash:\n");
        stringbuilder.append(this.getCauseStackTraceOrString());
        stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
        for (int i = 0; i < 87; ++i)
            stringbuilder.append("-");
        stringbuilder.append("\n\n");
        net.minecraftforge.fml.common.asm.transformers.BlamingTransformer.onCrash(stringbuilder);
        net.minecraftforge.fml.relauncher.CoreModManager.onCrash(stringbuilder);
        this.getSectionsInStringBuilder(stringbuilder);
        stringbuilder.append("\n\n");
        for (int i = 0; i < 87; ++i)
            stringbuilder.append("=");
        stringbuilder.append("\n\nMinecraft crashed.\n~~~ WHAT TO DO NOW? ~~~\nGo up to the top of this crash report and read what you should do to prevent this from happening again.\n\n");
        for (int i = 0; i < 87; ++i)
            stringbuilder.append("=");
        stringbuilder.append("\n\n");
        return stringbuilder.toString();
    }

	public String getUserInformation() {
		return this.userInformation;
	}

	public UserFriendlyCrashReport setUserInformation(String userInformation) {
		this.userInformation = userInformation;
		return this;
	}

	private static String getWittyComment()
    {
        String[] astring = new String[] {"Who set us up the TNT?", "Everything\'s going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I\'m sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don\'t be sad. I\'ll do better next time, I promise!", "Don\'t be sad, have a hug! <3", "I just don\'t know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn\'t worry myself about that.", "I bet Cylons wouldn\'t have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I\'m Minecraft, and I\'m a crashaholic.", "Ooh. Shiny.", "This doesn\'t make any sense!", "Why is it breaking :(", "Don\'t do that.", "Ouch. That hurt :(", "You\'re mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine."};

        try
        {
            return astring[(int)(System.nanoTime() % astring.length)];
        }
        catch (Throwable throwable)
        {
            return "Witty comment unavailable :(";
        }
    }
}
