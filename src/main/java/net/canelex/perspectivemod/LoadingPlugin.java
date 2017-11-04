package net.canelex.perspectivemod;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.canelex.perspectivemod.asm.CameraTransformer;

import java.util.Map;

@IFMLLoadingPlugin.Name("Perspective Mod")
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class LoadingPlugin implements IFMLLoadingPlugin
{
	@Override public String[] getASMTransformerClass()
	{
		return new String[]{ CameraTransformer.class.getName() };
	}

	@Override public String getModContainerClass()
	{
		return PerspectiveMod.class.getName();
	}

	@Override public String getSetupClass()
	{
		return null;
	}

	@Override public String getAccessTransformerClass()
	{
		return null;
	}

	@Override public void injectData(Map<String, Object> data)
	{

	}
}
