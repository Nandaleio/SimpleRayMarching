package com.nandaleio.renderer;

import org.lwjgl.util.vector.Vector3f;

import com.nandaleio.entity.Camera;

public class StaticShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src/com/nandaleio/renderer/vertex.glsl";
	private static final String FRAGMENT_FILE = "src/com/nandaleio/renderer/fragment.glsl";

	//General
	private int location_screenResolution;
	private int location_time;
	
	//Camera :
	private int location_camPosition;
	private int location_camRotation;
	
	//Scene :
	

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocation() {
		location_screenResolution = super.getUniformLocation("screenResolution");
		location_time = super.getUniformLocation("time");
		
		location_camPosition = super.getUniformLocation("camPosition");
		location_camRotation = super.getUniformLocation("camRotation");
	}
	
	public void loadScreenResolution(Vector3f res) {
		super.loadVector(location_screenResolution, res);
	}
	
	public void loadTime(float time) {
		super.loadFloat(location_time, time);
	}

	public void loadCam(Camera cam) {
		super.loadVector(location_camPosition, cam.getPosition());
		super.loadVector(location_camRotation, cam.getRotation());
	}
}
