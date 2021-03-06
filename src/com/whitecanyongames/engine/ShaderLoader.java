package com.whitecanyongames.engine;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Allows the user to load a shader pair and compile them
 * 
 * @version 1.0
 * @since 0.0.3
 * @author Lane
 * @category EngineCore
 *
 */
public class ShaderLoader {
	public static int loadShaderPair(String vertexShaderLocation, String fragmentShaderLocation)	{
		int shaderProgram = glCreateProgram();
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		StringBuilder vertexShaderSource = new StringBuilder();
		StringBuilder fragmentShaderSource = new StringBuilder();
		
		BufferedReader vertexShaderFileReader = null;
		try	{
			vertexShaderFileReader = new BufferedReader(new FileReader(vertexShaderLocation));
			String line;
			
			while ((line = vertexShaderFileReader.readLine()) != null)	{
				vertexShaderSource.append(line).append('\n');
			}
		}
		catch (IOException e)	{
			e.printStackTrace();
			return 1;
		}
		finally	{
			if (vertexShaderFileReader != null)	{
				try	{
					vertexShaderFileReader.close();
				}
				catch (IOException e)	{
					e.printStackTrace();
				}
			}
		}
		
		BufferedReader fragmentShaderFileReader = null;
		try	{
			fragmentShaderFileReader = new BufferedReader(new FileReader(fragmentShaderLocation));
			String line;
			
			while ((line = fragmentShaderFileReader.readLine()) != null)	{
				fragmentShaderSource.append(line).append('\n');
			}
		}
		catch (IOException e)	{
			e.printStackTrace();
			return 1;
		}
		finally {
			if (fragmentShaderFileReader != null)	{
				try	{
					fragmentShaderFileReader.close();
				}
				catch(IOException e)	{
					e.printStackTrace();
				}
			}
		}
		
		glShaderSource(vertexShader, vertexShaderSource);
		glCompileShader(vertexShader);
		if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Vertex shader wasn't able to be compiled correctly. Error log: ");
			System.err.println(glGetShaderInfoLog(vertexShader, 1024));
		}
		
		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE)	{
			System.err.println("Frament shader wasn't able to be compiled correctly. Error log: ");
			System.err.println(glGetShaderInfoLog(fragmentShader, 1024));
		}
		
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);
		if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE)	{
			System.err.println("Shader program wasn't linked correctly.");
			System.err.println(glGetProgramInfoLog(shaderProgram, 1024));
			
			return 1;
		}
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		
		return shaderProgram;
	}
}
