package net.canelex.perspectivemod;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.canelex.perspectivemod.command.CommandPerspectiveSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.io.*;
import java.util.Collections;

public class PerspectiveMod extends DummyModContainer
{
	private static File saveFile;
	private static Minecraft mc = Minecraft.getMinecraft();
	private KeyBinding keyPerspective = new KeyBinding("Toggle Perspective", 33, "Perspective Mod");

	public static boolean returnOnRelease = false;
	public static boolean perspectiveToggled = false;
	private static float cameraYaw = 0F;
	private static float cameraPitch = 0F;
	private static int previousPerspective = 0;

	public PerspectiveMod()
	{
		super(new ModMetadata());

		// Set mod metadata since modid file will not be read.
		ModMetadata meta = getMetadata();
		meta.name = "Perspective Mod 3.0";
		meta.modId = "perspectivemod";
		meta.version = "3.0";
		meta.description = "Allows you to view a full 360 degrees of your character.";
		meta.url = "www.youtube.com/canelex";
		meta.authorList = Collections.singletonList("canelex");
	}

	@Override public boolean registerBus(EventBus bus, LoadController controller)
	{
		// Register this class for FMLInitializationEvent
		bus.register(this);
		return true;
	}

	@Subscribe public void init(FMLInitializationEvent event)
	{
		// Register forge stuffs
		ClientRegistry.registerKeyBinding(keyPerspective);
		ClientCommandHandler.instance.registerCommand(new CommandPerspectiveSettings());
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);

		// Load settings from binary file.
		saveFile = new File(mc.mcDataDir, "perspectivemod-3.0.dat");
		loadSettings();
	}

	@SubscribeEvent public void onKeyPress(InputEvent.KeyInputEvent event)
	{
		if (Keyboard.getEventKey() == keyPerspective.getKeyCode())
		{
			if (Keyboard.getEventKeyState())
			{
				perspectiveToggled = !perspectiveToggled;
				cameraYaw = mc.thePlayer.rotationYaw;
				cameraPitch = mc.thePlayer.rotationPitch;

				if (perspectiveToggled)
				{
					previousPerspective = mc.gameSettings.thirdPersonView;
					mc.gameSettings.thirdPersonView = 1;
				}
				else
				{
					mc.gameSettings.thirdPersonView = previousPerspective;
				}
			}
			else if (returnOnRelease)
			{
				perspectiveToggled = false;
				mc.gameSettings.thirdPersonView = previousPerspective;
			}
		}

		if (Keyboard.getEventKey() == mc.gameSettings.keyBindTogglePerspective.getKeyCode())
		{
			perspectiveToggled = false;
		}
	}

	public static float getCameraYaw()
	{
		return perspectiveToggled ? cameraYaw : mc.thePlayer.rotationYaw;
	}

	public static float getCameraPitch()
	{
		return perspectiveToggled ? cameraPitch : mc.thePlayer.rotationPitch;
	}

	public static boolean overrideMouse()
	{
		if (mc.inGameHasFocus && Display.isActive())
		{
			if (!perspectiveToggled)
			{
				return true;
			}

			// CODE
			mc.mouseHelper.mouseXYChange();
			float f1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			float f2 = f1 * f1 * f1 * 8.0F;
			float f3 = (float) mc.mouseHelper.deltaX * f2;
			float f4 = (float) mc.mouseHelper.deltaY * f2;

			cameraYaw += f3 * 0.15F;
			cameraPitch += f4 * 0.15F;

			if (cameraPitch > 90) cameraPitch = 90;
			if (cameraPitch < -90) cameraPitch = -90;
		}

		return false;
	}

	public static void saveSettings()
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
			writer.write(String.valueOf(returnOnRelease));
			writer.close();
		} catch (Exception ex)
		{
			System.out.println("Failed to save settings: " + ex.getMessage());
		}
	}

	public static void loadSettings()
	{
		if (!saveFile.exists())
		{
			return;
		}

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(saveFile));
			returnOnRelease = Boolean.parseBoolean(reader.readLine());
			reader.close();
		} catch (Exception ex)
		{
			System.out.println("Failed to load settings: " + ex.getMessage());
		}
	}
}
