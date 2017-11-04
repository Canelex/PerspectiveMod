package net.canelex.perspectivemod.command;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.canelex.perspectivemod.PerspectiveMod;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class CommandPerspectiveSettings extends CommandBase
{
	@Override public String getCommandName()
	{
		return "perspectivemod";
	}

	@Override public String getCommandUsage(ICommandSender sender)
	{
		return "/perspectivemod [hold/press]";
	}

	@Override public boolean canCommandSenderUseCommand(ICommandSender sender)
	{
		return true;
	}

	@Override public void processCommand(ICommandSender sender, String[] args)
	{
		if (args.length > 0)
		{
			if (args[0].equalsIgnoreCase("hold"))
			{
				PerspectiveMod.returnOnRelease = true;
				sender.addChatMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "[Perspective Mod] Mode is now 'HOLD_KEY'"));
				PerspectiveMod.saveSettings();
			}
			else if (args[0].equalsIgnoreCase("press"))
			{
				PerspectiveMod.returnOnRelease = false;
				sender.addChatMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "[Perspective Mod] Mode is now 'PRESS_KEY'"));
				PerspectiveMod.saveSettings();
			}
			else
			{
				sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + getCommandUsage(sender)));
			}
		}
		else
		{
			sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + getCommandUsage(sender)));
		}
	}
}
