package net.canelex.perspectivemod.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

public class CameraTransformer implements IClassTransformer
{
	@Override public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		if (transformedName.equals("net.minecraft.client.renderer.EntityRenderer"))
		{
			ClassReader reader = new ClassReader(bytes);
			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			ClassVisitor visitor = new ClassVisitor(Opcodes.ASM4, writer)
			{
				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] ex)
				{
					MethodVisitor mv = super.visitMethod(access, name, desc, signature, ex);

					if (desc.equals("(F)V") && name.equals("h") || name.equals("orientCamera"))
					{
						return new OrientCameraVisitor(mv);
					}

					if (desc.equals("(F)V") && name.equals("b") || name.equals("updateCameraAndRender"))
					{
						return new CameraVisitor(mv);
					}

					return mv;
				}
			};

			reader.accept(visitor, 0);
			return writer.toByteArray();
		}

		return bytes;
	}
}
