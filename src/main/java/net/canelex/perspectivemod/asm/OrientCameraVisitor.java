package net.canelex.perspectivemod.asm;

import com.google.common.collect.Maps;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;

public class OrientCameraVisitor extends MethodVisitor implements Opcodes
{
	private HashMap<String, String> obfList = Maps.newHashMap();

	public OrientCameraVisitor(MethodVisitor mv)
	{
		super(ASM4, mv);

		obfList.put("y", "getCameraYaw");
		obfList.put("z", "getCameraPitch");
		obfList.put("A", "getCameraYaw");
		obfList.put("B", "getCameraPitch");

		// Unobf
		obfList.put("rotationYaw", "getCameraYaw");
		obfList.put("rotationPitch", "getCameraPitch");
		obfList.put("prevRotationYaw", "getCameraYaw");
		obfList.put("prevRotationPitch", "getCameraPitch");
	}

	@Override public void visitFieldInsn(int opcode, String owner, String name, String desc)
	{
		super.visitFieldInsn(opcode, owner, name, desc);

		if (opcode == GETFIELD && desc.equals("F") && owner.equals("sv") || owner.equals("net/minecraft/entity/EntityLivingBase"))
		{
			if (obfList.containsKey(name))
			{
				System.out.println("Replaced " + name + " with " + obfList.get(name));
				visitInsn(FCONST_0);
				visitInsn(FMUL);
				visitMethodInsn(INVOKESTATIC, "net/canelex/perspectivemod/PerspectiveMod", obfList.get(name), "()F", false);
				visitInsn(FADD);
			}
		}
	}
}
