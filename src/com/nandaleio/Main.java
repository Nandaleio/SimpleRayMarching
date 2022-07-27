package com.nandaleio;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.nandaleio.entity.Camera;
import com.nandaleio.renderer.DisplayManager;
import com.nandaleio.renderer.Loader;
import com.nandaleio.renderer.RawModel;
import com.nandaleio.renderer.Renderer;

public class Main {

	public static void main(String[] args) {
		DisplayManager.createDisplay();

		Loader loader = new Loader();
		float[] vertices = { -1f, 1f, 0f, -1f, -1f, 0f, 1f, -1f, 0f, 1f, -1f, 0f, 1f, 1f, 0f, -1f,1f, 0f };

		RawModel quad = loader.loadToVAO(vertices);
		Renderer renderer = new Renderer();
		
		Camera cam = new Camera();
		cam.setPosition(new Vector3f(0f,10f,0f));

		while (!Display.isCloseRequested()) {
			renderer.prepare();
			// game logic

			cam.move();
			renderer.getShader().loadCam(cam);
			renderer.render(quad);
			
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		DisplayManager.closeDisplay();
	}

}
