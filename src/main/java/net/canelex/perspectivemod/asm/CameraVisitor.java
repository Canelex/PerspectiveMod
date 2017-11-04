package net.canelex.perspectivemod.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CameraVisitor extends MethodVisitor implements Opcodes
{
	public CameraVisitor(MethodVisitor mv)
	{
		super(ASM4, mv);
	}

	@Override public void visitFieldInsn(int opcode, String owner, String name, String desc)
	{
		if (opcode == GETFIELD && desc.equals("Z") && name.equals("x") || name.equals("inGameHasFocus"))
		{
			visitMethodInsn(INVOKESTATIC, "net/canelex/perspectivemod/PerspectiveMod", "overrideMouse", "()Z", false);
		}
		else
		{
			super.visitFieldInsn(opcode, owner, name, desc);
		}
	}
}
