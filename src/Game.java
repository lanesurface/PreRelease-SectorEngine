import static org.lwjgl.opengl.GL11.*;

import java.io.File;

import org.lwjgl.util.vector.Vector3f;

import com.whitecanyongames.engine.Engine;
import com.whitecanyongames.engine.Utilities;
import com.whitecanyongames.engine.camera.Camera;
import com.whitecanyongames.engine.object.OBJLoader;

public class Game extends Engine {
	private float rotate;
	public static Camera camera;
	
	public Game(int width, int height, int FPS, String title, boolean VSync) {
		super(width, height, FPS, title, VSync);
	}
	@Override
	public void initGL()	{
		System.out.println("OpenGL version: " + glGetString(GL_VERSION));
		
		camera = new Camera(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f));
		
//		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); //Wireframe mode
		
		bDebugMode = true;
		bDisableFullscreen = true;
		bDynamicLighting = true;
		
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glLightModel(GL_LIGHT_MODEL_AMBIENT, Utilities.asFloatBuffer(new float[] {0.05f, 0.05f, 0.05f, 1f}));
		glLight(GL_LIGHT0, GL_DIFFUSE, Utilities.asFloatBuffer(new float[] {1.5f, 1.5f, 1.5f, 1f}));
		
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
		glEnable(GL_TEXTURE_2D);
        
		Utilities.set3DPerspective();
	}
	@Override
	public void update(int delta)	{
		super.update(delta);
		camera.toll(delta);
	}
	@Override
	public void renderGL()	{
		//Render a square
//		InputHandler2D input = new InputHandler2D();
//		Vector2f pos = new Vector2f();
//		pos.x = input.getxPos();
//		pos.y = input.getyPos();
		
//		TextureLoader texture = new TextureLoader();
//		try {
//			texture.loadTexture("texture.png");
//			glBindTexture(GL_TEXTURE_2D, texture.getTexture().getTextureID());
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		glBegin(GL_QUADS);
//			glTexCoord2f(100, 100);
//			glVertex2f(100, 100);
//			glTexCoord2f(300, 100);
//			glVertex2f(300, 100);
//			glTexCoord2f(300, 300);
//			glVertex2f(300, 300);
//			glTexCoord2f(100, 300);
//			glVertex2f(100, 300);
//		glEnd();
//		
//		glBindTexture(GL_TEXTURE_2D, 0);
		
		//Render a cube
//		glLoadIdentity();
//		glTranslatef(0.0f, 0.0f, -7.0f);
//		
//		glBegin(GL_QUADS);
//			glColor3f(0.0f,1.0f,0.0f);          // Set The Color To Green
//			glVertex3f( 1.0f, 1.0f,-1.0f);          // Top Right Of The Quad (Top)
//			glVertex3f(-1.0f, 1.0f,-1.0f);          // Top Left Of The Quad (Top)
//			glVertex3f(-1.0f, 1.0f, 1.0f);          // Bottom Left Of The Quad (Top)
//			glVertex3f( 1.0f, 1.0f, 1.0f);          // Bottom Right Of The Quad (Top)
//		
//			glColor3f(1.0f,0.5f,0.0f);          // Set The Color To Orange
//			glVertex3f( 1.0f,-1.0f, 1.0f);          // Top Right Of The Quad (Bottom)
//			glVertex3f(-1.0f,-1.0f, 1.0f);          // Top Left Of The Quad (Bottom)
//			glVertex3f(-1.0f,-1.0f,-1.0f);          // Bottom Left Of The Quad (Bottom)
//			glVertex3f( 1.0f,-1.0f,-1.0f);          // Bottom Right Of The Quad (Bottom)
//		
//			glColor3f(1.0f,0.0f,0.0f);          // Set The Color To Red
//			glVertex3f( 1.0f, 1.0f, 1.0f);          // Top Right Of The Quad (Front)
//			glVertex3f(-1.0f, 1.0f, 1.0f);          // Top Left Of The Quad (Front)
//			glVertex3f(-1.0f,-1.0f, 1.0f);          // Bottom Left Of The Quad (Front)
//			glVertex3f( 1.0f,-1.0f, 1.0f);          // Bottom Right Of The Quad (Front)
//
//			glColor3f(1.0f,1.0f,0.0f);          // Set The Color To Yellow
//			glVertex3f( 1.0f,-1.0f,-1.0f);          // Bottom Left Of The Quad (Back)
//			glVertex3f(-1.0f,-1.0f,-1.0f);          // Bottom Right Of The Quad (Back)
//			glVertex3f(-1.0f, 1.0f,-1.0f);          // Top Right Of The Quad (Back)
//			glVertex3f( 1.0f, 1.0f,-1.0f);          // Top Left Of The Quad (Back)
//
//			glColor3f(0.0f,0.0f,1.0f);          // Set The Color To Blue
//			glVertex3f(-1.0f, 1.0f, 1.0f);          // Top Right Of The Quad (Left)
//			glVertex3f(-1.0f, 1.0f,-1.0f);          // Top Left Of The Quad (Left)
//			glVertex3f(-1.0f,-1.0f,-1.0f);          // Bottom Left Of The Quad (Left)
//			glVertex3f(-1.0f,-1.0f, 1.0f);          // Bottom Right Of The Quad (Left)
//
//			glColor3f(1.0f,0.0f,1.0f);          // Set The Color To Violet
//      	glVertex3f( 1.0f, 1.0f,-1.0f);          // Top Right Of The Quad (Right)
//      	glVertex3f( 1.0f, 1.0f, 1.0f);          // Top Left Of The Quad (Right)
//      	glVertex3f( 1.0f,-1.0f, 1.0f);          // Bottom Left Of The Quad (Right)
//      	glVertex3f( 1.0f,-1.0f,-1.0f);          // Bottom Right Of The Quad (Right)
//      glEnd();
		
		//Render object files
		Utilities.clearScreen();
		glClearColor(0.025f, 0.28f, 0.36f, 1.0f);
		
		glLoadIdentity();
		glLight(GL_LIGHT0, GL_POSITION, Utilities.asFloatBuffer(new float[] {0.0f, 0.0f, 0.0f, 1f}));
		
		glLoadIdentity();
		
		Vector3f position = camera.getPosition();
		glTranslatef(position.x, 0, position.z);
		
		Vector3f rotation = camera.getRotation();
		glRotatef(rotation.y, 0, 1, 0);
		glRotatef(rotation.x, 1, 0, 0);
		
		glTranslatef(-3.0f, 0.0f, -10.0f);
		glColor3f(0.47f, 0.89f, 0.23f);
		OBJLoader.drawModel(new File("cube.obj"));
		
		glTranslatef(3.0f, 0.0f, 0.0f);
		glRotatef(rotate, 1.0f, 1.0f, 0.0f);
		glColor3f(0.4f, 0.27f, 0.17f);
		OBJLoader.drawModel(new File("icosphere.obj"));
		
		rotate += 0.4f;
	}
	public static void main(String[] args) {
		Game game = new Game(800, 600, 120, "Sector Engine: " + Engine.getBuildNumber(), true);
		game.start();
	}
}
