//Created by White Canyon Games, 2014
package com.whitecanyongames.engine.object;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Lane
 * @version 1.0
 * @since 0.0.2
 * @category Object
 * 
 * Loads in the object file (current format not supported)
 * 
 */

public class OBJLoader {
	public static Model loadModel(File file) throws FileNotFoundException, IOException	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Model model = new Model();
		String line;
		while ((line = reader.readLine()) != null)	{
			if (line.startsWith("v "))	{
				float x = Float.valueOf(line.split("\\s+")[1]);
				float y = Float.valueOf(line.split("\\s+")[2]);
				float z = Float.valueOf(line.split("\\s+")[3]);
				model.verticies.add(new Vector3f(x, y, z));
			}
			else if (line.startsWith("vn "))	{
				float x = Float.valueOf(line.split("\\s+")[1]);
				float y = Float.valueOf(line.split("\\s+")[2]);
				float z = Float.valueOf(line.split("\\s+")[3]);
				model.normals.add(new Vector3f(x, y, z));
			}
			else if (line.startsWith("f "))	{
				Vector3f vertexIndices = new Vector3f(Float.valueOf(line.split("\\s+")[1].split("/")[0]), 
													  Float.valueOf(line.split("\\s+")[2].split("/")[0]),
													  Float.valueOf(line.split("\\s+")[3].split("/")[0]));
				Vector3f normalIndices = new Vector3f(Float.valueOf(line.split("\\s+")[1].split("/")[2]), 
						  							  Float.valueOf(line.split("\\s+")[2].split("/")[2]),
						  							  Float.valueOf(line.split("\\s+")[3].split("/")[2]));
				model.faces.add(new Face(vertexIndices, normalIndices));
			}
		}
		reader.close();
		
		return model;
	}
	public static void drawModel(File file) {
		Model model = null;
		try	{
			model = OBJLoader.loadModel(file);
		}
		catch(FileNotFoundException e)	{
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		catch(IOException e)	{
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		
		glBegin(GL_TRIANGLES);
		for (Face face : model.faces)	{
			Vector3f n1 = model.normals.get((int) face.normal.x - 1);
			glNormal3f(n1.x, n1.y, n1.z);
			Vector3f v1 = model.verticies.get((int) face.vertex.x - 1);
			glVertex3f(v1.x, v1.y, v1.z);
			
			Vector3f n2 = model.normals.get((int) face.normal.y - 1);
			glNormal3f(n1.x, n1.y, n1.z);
			Vector3f v2 = model.verticies.get((int) face.vertex.y - 1);
			glVertex3f(v2.x, v2.y, v2.z);
			
			Vector3f n3 = model.normals.get((int) face.normal.z - 1);
			glNormal3f(n1.x, n1.y, n1.z);
			Vector3f v3 = model.verticies.get((int) face.vertex.z - 1);
			glVertex3f(v3.x, v3.y, v3.z);
		}
		glEnd();
	}
}
