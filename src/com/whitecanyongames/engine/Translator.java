package com.whitecanyongames.engine;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Translator {
	Matrix4f translationMatrix;
	
	public void translateObject(Vector4f translationVector)	{
		translationMatrix.translate(new Vector3f(10.0f, 0.0f, 0.0f));
	}
}
