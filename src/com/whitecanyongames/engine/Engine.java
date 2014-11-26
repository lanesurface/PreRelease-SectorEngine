//Created by White Canyon Games, 2014
package com.whitecanyongames.engine;

import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.glShadeModel;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * The base class for the engine
 * 
 * The main game class extends from this class, any defaults put
 * into this class in here will be included in the main game class
 * if the method is used
 * 
 * @author Lane
 * @version 1.3
 * @since 0.0.1
 * @category EngineCore
 * 
 */

public abstract class Engine {
	protected String title;
	protected int width;
	protected int height;
	
	protected static long lastFrame;
	protected static int FPS;
	protected static long lastFPS;
	
	protected boolean bDebugMode;
	protected boolean bDynamicLighting;
	
	/**
	 * The main engine constructor, all game classes extend Engine and inherit its
	 * rendering methods
	 * 
	 * @param width The width of the window
	 * @param height The height of the window
	 * @param FPS The cap frames being rendered per second
	 * @param title The title given to the window
	 * @param VSync If true, this will enable vsync
	 */
	public Engine(int width, int height, int FPS, String title, boolean VSync)	{
		this.FPS = FPS;
		this.title = title;
		this.width = width;
		this.height = height;
		
		SplashScreen splashScreen = new SplashScreen();
		
		try	{
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle(title);
			Display.create(new PixelFormat(8, 8, 0, 8));
		}
		catch(LWJGLException e)	{
			e.printStackTrace();
			postGLFatal(); //Clean up operations in the event of a fatal display initialization
			System.exit(1);
		}
		
		initGL(); //Before rendering, do the specified operations
		getDelta(); //Get the time since the last update
		lastFPS = getTime();
		
		//For operations that constantly need to be updated
		while (!Display.isCloseRequested())	{
			int delta = getDelta();
			
			update(delta); //Used to update FPS and input handlers
			renderGL(); //Update the graphics
			
			Display.update();
			Display.sync(FPS);
			if (VSync) Display.setVSyncEnabled(true); //Allows the monitor to handle update operations, prevents tearing
		}
		Display.destroy();
		postGL(); //Cleanup operations
	}
	/**
	 * Used for operations that need to be in the update loop
	 * @param delta is the time since the last update
	 */
	protected void update(int delta)	{
		while(Keyboard.next())	{
			if (Keyboard.getEventKeyState())	{
				if (Keyboard.getEventKey() == Keyboard.KEY_F11)	{
					setDisplayMode(width, height, !Display.isFullscreen());
				}
			}
		}
		updateFPS();
	}
	/**
	 * @return delta (the time since the last update)
	 */
	public static int getDelta()	{
		long time = getTime();
		int delta = (int)(time - lastFrame);
		lastFrame = time;
		
//		if (bDebugMode) System.out.println("Delta: " + delta); //Commented out so that we can actually see the FPS printing
		
		return delta;
	}
	public static long getTime()	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	/**
	 * Updates the frames every time that it's called (found in the update loop)
	 */
	public void updateFPS()	{
		if (getTime() - lastFPS > 1000)	{ //For every one second
			if (bDebugMode) System.out.println("FPS: " + FPS);
			FPS = 0; //Reset the FPS so that the frames for this second are zero
			lastFPS += 1000; //Add one second the the lastFPS
		}
		FPS += 1;
	}
	/**
	 * Everything in here will be processed before the graphics are drawn onto the screen
	 */
	protected void initGL()	{
		//Dynamic lighting turns off smooth shading whenever performance is necessary
		//0.0333 is the delta time for 30 FPS
		if (bDynamicLighting)	{
			if (FPS < 30 || getDelta() < 0.0333) glShadeModel(GL_FLAT);
			else glShadeModel(GL_SMOOTH);
		}
	}
	/**
	 * All of the graphics are drawn to the screen here
	 */
	protected void renderGL()	{}
	/**
	 * All cleanup events should go here, called after the display is closed
	 */
	protected void postGL()	{}
	/**
	 * Called whenever the application needs to exit on a fatal error
	 * Cleanup processes for fatal events go here
	 */
	protected void postGLFatal() {}
	/**
	 * The default start method 
	 */
	protected void start()	{}
	/**
	 * Display class used to set fullscreen windows whenever boolean fullscreen is true
	 * 
	 * @param width is the width of the screen when not in fullscreen mode
	 * @param height is the height of the screen when not in fullscreen mode
	 * @param fullscreen boolean that determines fullscreen mode is implemented
	 */
	protected void setDisplayMode(int width, int height, boolean fullscreen)	{
		//If nothing has changed, return
		if ((Display.getDisplayMode().getWidth() == width) && 
		   (Display.getDisplayMode().getHeight() == height)&&
		   (Display.isFullscreen() == fullscreen)) return;
		try	{
			DisplayMode targetDisplayMode = null;
			
			if (fullscreen)	{
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;
				
				for (int i = 0; i < modes.length; i++)	{
					DisplayMode current = modes[i];
					
					if ((current.getWidth() == width) && (current.getHeight() == height))	{
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq))	{
							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel()))	{
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}
						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
						   (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency()))	{
							targetDisplayMode = current;
							break;
						}
					}
				}
			}
			else	{
				targetDisplayMode = new DisplayMode(width, height);
			}
			if (targetDisplayMode == null)	{
				System.out.println("Failed to find value mode: " + width + "x" + height + "fs=" + fullscreen);
				return;
			}
			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);
		}
		catch (LWJGLException e)	{
			System.out.println("Unable to setup mode " + width  + "x" + height + " fs=" + fullscreen);
		}
	}
}
