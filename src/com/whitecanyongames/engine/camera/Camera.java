package com.whitecanyongames.engine.camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * A 3D camera class
 * 
 * Note: the camera for nightly 0.0.3 has been replaced with the following class
 * 
 * Pitch represents the rotation around the x axis
 * Yaw represents rotation around the y axis
 * Roll represents rotation around the z axis
 * 
 * dx (oposite side) = hypotenuse * sin(x)
 * dz (adjacent side) = hypotenuse * cos(x)
 * 
 * @version 1.0
 * @since 0.0.4
 * @author Lane
 * @category Camera
 *
 */
public abstract class Camera {
	protected Vector3f position;
	protected Vector3f rotation;
	
	protected boolean keyWDown;
	protected boolean keyADown;
	protected boolean keySDown;
	protected boolean keyDDown;
	
	protected float distance;
	protected float travelSpeed;
	protected float rotSpeed;
	
	public Camera(Vector3f position, Vector3f rotation)	{
		this.position = position;
		this.rotation = rotation;
		
		distance = 0;
		travelSpeed = 90;
		rotSpeed = 120;
	}
	public void toll(float delta)	{
		keyWDown = Keyboard.isKeyDown(Keyboard.KEY_W);
		keyADown = Keyboard.isKeyDown(Keyboard.KEY_A);
		keySDown = Keyboard.isKeyDown(Keyboard.KEY_S);
		keyDDown = Keyboard.isKeyDown(Keyboard.KEY_D);
		
		if (Mouse.isButtonDown(0))	{
			Mouse.setGrabbed(true);
		}
		
		while(Keyboard.next())	{
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.isKeyDown((Keyboard.KEY_ESCAPE)))	{
					Mouse.setGrabbed(false);
					//TODO: menu system
				}
			}
		}
		
		if (keyWDown)	{
			distance += travelSpeed * delta;
		}
		if (keyADown)	{
			rotation.y += rotSpeed * delta;
		}
		if (keySDown)	{
			distance += -travelSpeed * delta;
		}
		if (keyDDown)	{
			rotation.y += -rotSpeed * delta;
		}
		
		float dx = (float)(distance * Math.sin(Math.toRadians(rotation.y)));
		float dz = (float)(distance * Math.cos(Math.toRadians(rotation.y)));
		
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
}
