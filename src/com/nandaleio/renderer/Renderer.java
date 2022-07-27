package com.nandaleio.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

public class Renderer {
	
	private StaticShader shader;
	private long initTime;
	
	public Renderer() {
		shader = new StaticShader();
		initTime = System.currentTimeMillis();
		
	}
	
	public void prepare() {
		GL11.glClearColor(0, 1, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	public void render(RawModel model) {
		shader.start();
		float nyan = (System.currentTimeMillis() - initTime)/1000f;
		shader.loadTime(nyan);
		shader.loadScreenResolution(new Vector3f(DisplayManager.WIDTH, DisplayManager.HEIGHT, 1f));
		
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		
		shader.start();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

	public StaticShader getShader() {
		return shader;
	}

}
