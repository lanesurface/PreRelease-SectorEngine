package com.whitecanyongames.engine.camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

import static java.lang.Math.*;
import static org.lwjgl.opengl.ARBDepthClamp.GL_DEPTH_CLAMP;
import static org.lwjgl.opengl.GL11.*;

/**
 * A 3d camera class
 * 
 * @version 1.0
 * @since 0.0.3
 * @author Lane
 * @author Oskar Veerhoek
 *
 */
public class EulerCamera implements Camera {
	private float x = 0;
	private float y = 0;
	private float z = 0;
	private float pitch = 0;
	private float yaw = 0;
	private float roll = 0;
	private float fov = 90;
	private float aspectRatio = 1;
	private float zNear;
	private float zFar;
	
	private EulerCamera(Builder builder)	{
		this.x = builder.x;
		this.y = builder.y;
		this.z = builder.z;
		this.pitch = builder.pitch;
		this.yaw   = builder.yaw;
		this.roll  = builder.roll;
		this.aspectRatio = builder.aspectRatio;
		this.zNear = builder.zNear;
		this.zFar  = builder.zFar;
		this.fov   = builder.fov;
	}
	public EulerCamera()	{
		this.zNear = 0.3f;
		this.zFar = 100;
	}
	public EulerCamera(float aspectRatio)	{
		if (aspectRatio <= 0)	{
			throw new IllegalArgumentException("aspectRatio " + aspectRatio + " was 0 or smaller than 0");
		}
		
		this.aspectRatio = aspectRatio;
		this.zNear = 0.3f;
		this.zFar = 100;
	}
	public EulerCamera(float aspectRatio, float x, float y, float z)	{
		this(aspectRatio);
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public EulerCamera(float aspectRatio, float x, float y, float z, float pitch, float yaw, float roll)	{
		this (aspectRatio, x, y, z);
		
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}
	public EulerCamera(float aspectRatio, float x, float y, float z, float pitch, float yaw, float roll, float zNear, float zFar)	{
		if (aspectRatio <= 0)	{
			throw new IllegalArgumentException("aspectRatio " + aspectRatio + " was 0 or smaller than 0");
		}
		if (zNear <= 0)	{
			throw new IllegalArgumentException("zNear " + zNear + " was smaller than 0 or less than 0");
		}
		if (zFar <= zNear)	{
			throw new IllegalArgumentException("zFar " + " was smaller than or the same as zNear");
		}
		
		this.aspectRatio = aspectRatio;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
		this.zNear = zNear;
		this.zFar = zFar;
	}
	@Override
	public void processMouse() {
		final float MAX_LOOK_UP = 90;
		final float MAX_LOOK_DOWN = -90;
		float mouseDX = Mouse.getDX();
		float mouseDY = Mouse.getDY();
		
		if (yaw + mouseDX >= 360)	{
			yaw = yaw + mouseDX - 360;
		}
		else if (yaw + mouseDX < 0)	{
			yaw = 360 - yaw + mouseDX;
		}
		else	{
			yaw += mouseDX;
		}
		
		if (pitch - mouseDY >= MAX_LOOK_DOWN && pitch - mouseDY <= MAX_LOOK_UP)	{
			pitch += -mouseDY;
		}
		else if (pitch - mouseDY < MAX_LOOK_DOWN)	{
			pitch = MAX_LOOK_DOWN;
		}
		else if (pitch - mouseDY > MAX_LOOK_UP)	{
			pitch = MAX_LOOK_UP;
		}
	}
	@Override
	public void processMouse(float mouseSpeed) {
		final float MAX_LOOK_UP = 90;
		final float MAX_LOOK_DOWN = -90;
		float mouseDX = Mouse.getDX() * mouseSpeed * 0.16f;
		float mouseDY = Mouse.getDY() * mouseSpeed * 0.16f;
		
		if (yaw + mouseDX >= 360)	{
			yaw = yaw + mouseDX - 360;
		}
		else if (yaw + mouseDX < 0)	{
			yaw = 360 - yaw + mouseDX;
		}
		else	{
			yaw += mouseDX;
		}
		
		if (pitch - mouseDY >= MAX_LOOK_DOWN && pitch - mouseDY <= MAX_LOOK_UP)	{
			pitch += -mouseDY;
		}
		else if (pitch - mouseDY < MAX_LOOK_DOWN)	{
			pitch = MAX_LOOK_DOWN;
		}
		else if (pitch - mouseDY > MAX_LOOK_UP)	{
			pitch = MAX_LOOK_UP;
		}
	}
	@Override
	public void processMouse(float mouseSpeed, float maxLookUp, float maxLookDown) {
		float mouseDX = Mouse.getDX() * mouseSpeed * 0.16f;
		float mouseDY = Mouse.getDY() * mouseSpeed * 0.16f;
		
		if (yaw + mouseDX >= 360)	{
			yaw = yaw + mouseDX - 360;
		}
		else if (yaw + mouseDX < 0)	{
			yaw = 360 - yaw + mouseDX;
		}
		else	{
			yaw += mouseDX;
		}
		
		if (pitch - mouseDY >= maxLookDown && pitch - mouseDY <= maxLookUp)	{
			pitch += -mouseDY;
		}
		else if (pitch - mouseDY < maxLookUp)	{
			pitch = maxLookDown;
		}
		else if (pitch - mouseDY > maxLookUp)	{
			pitch = maxLookUp;
		}
	}
	@Override
	public void processKeyboard(float delta) {
		if (delta <= 0)	{
			throw new IllegalArgumentException("delta " + delta + " is 0 or is smaller than 0");
		}
		
		boolean keyUp    = Keyboard.isKeyDown(Keyboard.KEY_UP);
		boolean keyDown  = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		boolean keyLeft  = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		boolean flyUp    = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		boolean flyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		
		if (keyUp && keyRight && !keyLeft && !keyDown)	{
			moveFromLook(delta * 0.003f, 0, -delta * 0.003f);
		}
		if (keyUp && keyLeft && !keyRight && !keyDown)	{
			moveFromLook(-delta * 0.003f, 0, -delta * 0.003f);
		}
		if (keyUp && !keyLeft && !keyRight && !keyDown) {
            moveFromLook(0, 0, -delta * 0.003f);
        }
        if (keyDown && keyLeft && !keyRight && !keyUp) {
            moveFromLook(-delta * 0.003f, 0, delta * 0.003f);
        }
        if (keyDown && keyRight && !keyLeft && !keyUp) {
            moveFromLook(delta * 0.003f, 0, delta * 0.003f);
        }
        if (keyDown && !keyUp && !keyLeft && !keyRight) {
            moveFromLook(0, 0, delta * 0.003f);
        }
        if (keyLeft && !keyRight && !keyUp && !keyDown) {
            moveFromLook(-delta * 0.003f, 0, 0);
        }
        if (keyRight && !keyLeft && !keyUp && !keyDown) {
            moveFromLook(delta * 0.003f, 0, 0);
        }
        if (flyUp && !flyDown) {
            y += delta * 0.003f;
        }
        if (flyDown && !flyUp) {
            y -= delta * 0.003f;
        }
	}
	@Override
	public void processKeyboard(float delta, float speed) {
		if (delta <= 0) {
            throw new IllegalArgumentException("delta " + delta + " is 0 or is smaller than 0");
        }

        boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean flyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean flyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

        if (keyUp && keyRight && !keyLeft && !keyDown) {
            moveFromLook(speed * delta * 0.003f, 0, -speed * delta * 0.003f);
        }
        if (keyUp && keyLeft && !keyRight && !keyDown) {
            moveFromLook(-speed * delta * 0.003f, 0, -speed * delta * 0.003f);
        }
        if (keyUp && !keyLeft && !keyRight && !keyDown) {
            moveFromLook(0, 0, -speed * delta * 0.003f);
        }
        if (keyDown && keyLeft && !keyRight && !keyUp) {
            moveFromLook(-speed * delta * 0.003f, 0, speed * delta * 0.003f);
        }
        if (keyDown && keyRight && !keyLeft && !keyUp) {
            moveFromLook(speed * delta * 0.003f, 0, speed * delta * 0.003f);
        }
        if (keyDown && !keyUp && !keyLeft && !keyRight) {
            moveFromLook(0, 0, speed * delta * 0.003f);
        }
        if (keyLeft && !keyRight && !keyUp && !keyDown) {
            moveFromLook(-speed * delta * 0.003f, 0, 0);
        }
        if (keyRight && !keyLeft && !keyUp && !keyDown) {
            moveFromLook(speed * delta * 0.003f, 0, 0);
        }
        if (flyUp && !flyDown) {
            y += speed * delta * 0.003f;
        }
        if (flyDown && !flyUp) {
            y -= speed * delta * 0.003f;
        }
	}
	@Override
	public void processKeyboard(float delta, float speedX, float speedY, float speedZ) {
		if (delta <= 0) {
            throw new IllegalArgumentException("delta " + delta + " is 0 or is smaller than 0");
        }

        boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean flyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean flyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

        if (keyUp && keyRight && !keyLeft && !keyDown) {
            moveFromLook(speedX * delta * 0.003f, 0, -speedZ * delta * 0.003f);
        }
        if (keyUp && keyLeft && !keyRight && !keyDown) {
            moveFromLook(-speedX * delta * 0.003f, 0, -speedZ * delta * 0.003f);
        }
        if (keyUp && !keyLeft && !keyRight && !keyDown) {
            moveFromLook(0, 0, -speedZ * delta * 0.003f);
        }
        if (keyDown && keyLeft && !keyRight && !keyUp) {
            moveFromLook(-speedX * delta * 0.003f, 0, speedZ * delta * 0.003f);
        }
        if (keyDown && keyRight && !keyLeft && !keyUp) {
            moveFromLook(speedX * delta * 0.003f, 0, speedZ * delta * 0.003f);
        }
        if (keyDown && !keyUp && !keyLeft && !keyRight) {
            moveFromLook(0, 0, speedZ * delta * 0.003f);
        }
        if (keyLeft && !keyRight && !keyUp && !keyDown) {
            moveFromLook(-speedX * delta * 0.003f, 0, 0);
        }
        if (keyRight && !keyLeft && !keyUp && !keyDown) {
            moveFromLook(speedX * delta * 0.003f, 0, 0);
        }
        if (flyUp && !flyDown) {
            y += speedY * delta * 0.003f;
        }
        if (flyDown && !flyUp) {
            y -= speedY * delta * 0.003f;
        }
	}
	@Override
	public void moveFromLook(float dx, float dy, float dz) {
		this.z += dx * (float) cos(toRadians(yaw - 90)) + dz * cos(toRadians(yaw));
        this.x -= dx * (float) sin(toRadians(yaw - 90)) + dz * sin(toRadians(yaw));
        this.y += dy * (float) sin(toRadians(pitch - 90)) + dz * sin(toRadians(pitch));
	}
	@Override
	public void setPosition(float x, float y, float z) {
		this.x = x;
        this.y = y;
        this.z = z;
	}
	@Override
	public void applyOrthographicsMatrix() {
		glPushAttrib(GL_TRANSFORM_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-aspectRatio, aspectRatio, -1, 1, 0, zFar);
        glPopAttrib();
	}
	@Override
	public void applyOptimalStates() {
		if (GLContext.getCapabilities().GL_ARB_depth_clamp) {
            glEnable(GL_DEPTH_CLAMP);
        }
	}
	@Override
	public void applyPerspectiveMatrix() {
		glPushAttrib(GL_TRANSFORM_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(fov, aspectRatio, zNear, zFar);
        glPopAttrib();
	}
	@Override
	public void applyTranslations() {
		glPushAttrib(GL_TRANSFORM_BIT);
        glMatrixMode(GL_MODELVIEW);
        glRotatef(pitch, 1, 0, 0);
        glRotatef(yaw, 0, 1, 0);
        glRotatef(roll, 0, 0, 1);
        glTranslatef(-x, -y, -z);
        glPopAttrib();
	}
	@Override
	public void setRotation(float pitch, float yaw, float roll) {
		this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
	}
	@Override
	public float x() {
		return x;
	}
	@Override
	public float y() {
		return y;
	}
	@Override
	public float z() {
		return z;
	}
	@Override
	public float pitch() {
		return pitch;
	}
	@Override
	public float yaw() {
		return yaw;
	}
	@Override
	public float roll() {
		return roll;
	}
	@Override
	public float fieldOfView() {
		return fov;
	}
	@Override
	public void setFieldOfView(float fov) {
		this.fov = fov;
	}
	@Override
	public float aspectRatio() {
		return aspectRatio;
	}
	@Override
	public void setAspectRatio(float aspectRatio) {
		if (aspectRatio <= 0) {
            throw new IllegalArgumentException("aspectRatio " + aspectRatio + " is 0 or less");
        }
		this.aspectRatio = aspectRatio;
	}
	@Override
	public float nearClippingPlane() {
		return zNear;
	}
	@Override
	public float farClippingPlane() {
		return zFar;
	}
	public String toString() {
        return "EulerCamera [x=" + x + ", y=" + y + ", z=" + z + ", pitch=" + pitch + ", yaw=" + yaw + ", " +
                "roll=" + roll + ", fov=" + fov + ", aspectRatio=" + aspectRatio + ", zNear=" + zNear + ", " +
                "zFar=" + zFar + "]";
    }
    /** A builder helper class for the EulerCamera class. */
    public static class Builder {

        private float aspectRatio = 1;
        private float x = 0, y = 0, z = 0, pitch = 0, yaw = 0, roll = 0;
        private float zNear = 0.3f;
        private float zFar = 100;
        private float fov = 90;

        public Builder() {
        	
        }
        /**
         * Sets the aspect ratio of the camera.
         *
         * @param aspectRatio the aspect ratio of the camera (window width / window height)
         *
         * @return this
         */
        public Builder setAspectRatio(float aspectRatio) {
            if (aspectRatio <= 0) {
                throw new IllegalArgumentException("aspectRatio " + aspectRatio + " was 0 or was smaller than 0");
            }
            
            this.aspectRatio = aspectRatio;
            return this;
        }
        /**
         * Sets the distance from the camera to the near clipping pane.
         *
         * @param nearClippingPane the distance from the camera to the near clipping pane
         *
         * @return this
         *
         * @throws IllegalArgumentException if nearClippingPane is 0 or less
         */
        public Builder setNearClippingPane(float nearClippingPane) {
            if (nearClippingPane <= 0) {
                throw new IllegalArgumentException("nearClippingPane " + nearClippingPane + " is 0 or less");
            }
            
            this.zNear = nearClippingPane;
            return this;
        }
        /**
         * Sets the distance from the camera to the far clipping pane.
         *
         * @param farClippingPane the distance from the camera to the far clipping pane
         *
         * @return this
         *
         * @throws IllegalArgumentException if farClippingPane is 0 or less
         */
        public Builder setFarClippingPane(float farClippingPane) {
            if (farClippingPane <= 0) {
                throw new IllegalArgumentException("farClippingPane " + farClippingPane + " is 0 or less");
            }
            
            this.zFar = farClippingPane;
            return this;
        }
        /**
         * Sets the field of view angle in degrees in the y direction.
         *
         * @param fov the field of view angle in degrees in the y direction
         *
         * @return this
         */
        public Builder setFieldOfView(float fov) {
            this.fov = fov;
            
            return this;
        }
        /**
         * Sets the position of the camera.
         *
         * @param x the x-coordinate of the camera
         * @param y the y-coordinate of the camera
         * @param z the z-coordinate of the camera
         *
         * @return this
         */
        public Builder setPosition(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
            
            return this;
        }
        /**
         * Sets the rotation of the camera.
         *
         * @param pitch the rotation around the x-axis in degrees
         * @param yaw the rotation around the y-axis in degrees
         * @param roll the rotation around the z-axis in degrees
         */
        public Builder setRotation(float pitch, float yaw, float roll) {
            this.pitch = pitch;
            this.yaw = yaw;
            this.roll = roll;
            
            return this;
        }
        /**
         * Constructs an instance of EulerCamera from this builder helper class.
         *
         * @return an instance of EulerCamera
         *
         * @throws IllegalArgumentException if farClippingPane is the same or less than nearClippingPane
         */
        public EulerCamera build() {
            if (zFar <= zNear) {
                throw new IllegalArgumentException("farClippingPane " + zFar + " is the same or less than " + "nearClippingPane " + zNear);
            }
            
            return new EulerCamera(this);
        }
    }
}
