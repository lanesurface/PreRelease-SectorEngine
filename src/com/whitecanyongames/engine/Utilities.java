//Created by White Canyon Games, 2014
package com.whitecanyongames.engine;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

/** 
 * A utility class that provides wrappers for some openGL code
 * 
 * @author Lane
 * @version 1.1
 * @since 0.0.1
 * @category EngineCore
 * 
 */

public class Utilities {
	/**
	 * Clears the screen to it's blank state
	 */
	public static void clearScreen()	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	/**
	 * Set our camera in position for a 2d game
	 */
	public static void set2DPerspective()	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		//Objects rendered will always appear the same size in the viewport
		glOrtho(0, (float)Display.getWidth(), 0, (float)Display.getHeight(), 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}
	/**
	 * Set out camera in position for a 3d game
	 */
	public static void set3DPerspective()	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		//Objects rendered further into the screen will appear smaller in the viewport
		gluPerspective(70.0f, (float)Display.getWidth() / (float)Display.getHeight(), 0.1f, 100.0f);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
	/**
	 * @param values The float array that you want converted to a FloatBuffer 
	 * @return The generated FloatBuffer
	 */
	public static FloatBuffer asFloatBuffer(float[] values)	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		
		return buffer;
	}
	/**
	 * 
	 * @param size The size of the FloatBuffer
	 * @return The FloatBuffer with the specified amount of data (size) reserved
	 */
	public static FloatBuffer reserveData(int size)	{
		FloatBuffer data = BufferUtils.createFloatBuffer(size);
		
		return data;
	}
}