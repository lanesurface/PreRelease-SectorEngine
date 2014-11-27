package com.whitecanyongames.engine.camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.whitecanyongames.engine.Engine;

/**
 * A 3D camera class
 * 
 * Pitch represents the rotation around the x axis
 * Yaw represents rotation around the y axis
 * Roll represents rotation around the z axis
 * 
 * dx (oposite side) = hypotenuse(distance) * sin(theta (x rotation))
 * dz (adjacent side) = hypotenuse(distance) * cos(theta (x rotation))
 * 
 * @version 1.1
 * @since 0.0.4
 * @author Lane
 * @category Camera
 *
 */
public class Camera {
	protected Vector3f position;
	protected Vector3f rotation;
	
	protected boolean keyWDown;
	protected boolean keyADown;
	protected boolean keySDown;
	protected boolean keyDDown;
	protected boolean mouseGrabbed;
	
	protected float distance;
	protected float travelSpeed;
	protected float rotSpeed;
	
	public Camera(Vector3f position, Vector3f rotation)	{
		this.position = position;
		this.rotation = rotation;
		
		distance = 0;
		travelSpeed = 0.02f;
		rotSpeed = 0.2f;
	}
	public void toll(float delta)	{
		keyWDown = Keyboard.isKeyDown(Keyboard.KEY_W);
		keyADown = Keyboard.isKeyDown(Keyboard.KEY_A);
		keySDown = Keyboard.isKeyDown(Keyboard.KEY_S);
		keyDDown = Keyboard.isKeyDown(Keyboard.KEY_D);
		
		if (Mouse.isButtonDown(0))	{
			mouseGrabbed = true;
			Mouse.setGrabbed(true);
		}
		
		while(Keyboard.next())	{
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.isKeyDown((Keyboard.KEY_ESCAPE)))	{
					mouseGrabbed = false;
					Mouse.setGrabbed(false);
					//TODO: menu system
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_F11))	{
					Engine.setDisplayMode(Engine.getWidth(), Engine.getHeight(), !Display.isFullscreen());
				}
			}
		}
		
		if (mouseGrabbed) {
			if (Mouse.isButtonDown(1)) rotation.x += -Mouse.getDY() * rotSpeed;
			else rotation.y += Mouse.getDX() * rotSpeed;
		}
		
		if (keyWDown && mouseGrabbed) distance += travelSpeed;
		if (keySDown && mouseGrabbed) distance += -travelSpeed;
		
		float dx = (float)(distance * Math.sin(Math.toRadians(rotation.x)));
		float dz = (float)(distance * Math.cos(Math.toRadians(rotation.x)));
		
		position.x += dx;
		position.z += dz;
	}
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public Vector3f getRotation() {
		return rotation;
	}
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	//If the game is calling super.update(), the mouse needs to be updated from the main update loop
	public boolean isMouseGrabbed() {
		return mouseGrabbed;
	}
	public void setMouseGrabbed(boolean mouseGrabbed) {
		this.mouseGrabbed = mouseGrabbed;
	}
}
