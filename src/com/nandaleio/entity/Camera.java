package com.nandaleio.entity;

import java.awt.event.MouseEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private float vitesse = 0.2f;
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;
	private float yaw;
	private float roll;

	public Camera() {
	}

	public void move() {
		float newAngle = (float) (yaw * Math.PI / 180);
		float verticalAngle = (float) (pitch * Math.PI / 180);

		if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			position.x -= vitesse * Math.cos(newAngle);
			position.z += vitesse * Math.sin(newAngle);
			position.y -= vitesse * Math.sin(verticalAngle);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.x += vitesse * Math.cos(newAngle);
			position.z -= vitesse * Math.sin(newAngle);
			position.y += vitesse * Math.sin(verticalAngle);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x -= vitesse * Math.cos(newAngle + Math.PI / 2f);
			position.z += vitesse * Math.sin(newAngle + Math.PI / 2f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			position.x += vitesse * Math.cos(newAngle + Math.PI / 2f);
			position.z -= vitesse * Math.sin(newAngle + Math.PI / 2f);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += vitesse;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			position.y -= vitesse;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			vitesse -= 0.2f;
			if (vitesse < 0.2f)
				vitesse = 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			vitesse += 0.2f;
			if (vitesse > 5f)
				vitesse = 5f;
		}

		if (Mouse.isButtonDown(MouseEvent.BUTTON1)) {
			yaw += Mouse.getDX() / 5;
			pitch -= Mouse.getDY() / 5;
			if (pitch < -90)
				pitch = -90;
			if (pitch > 90)
				pitch = 90;
		}
		
//		position.y = (position.y > 0) ? position.y -1 : 0; 
		
	}

	public Vector3f getRotation() {
		float newYaw = (float) ((float) (yaw * Math.PI / 180));
		float newPitch = (float) (pitch * Math.PI / 180);

		double xzLen = Math.cos(newPitch);
		double x = (xzLen * Math.cos(newYaw));
		double y = Math.sin(newPitch);
		double z = (xzLen * Math.sin(-newYaw));

		return new Vector3f((float) x, (float) y, (float) z);

//		return new Vector3f((float) (yaw * Math.PI / 180), (float) (pitch * Math.PI / 180), roll);
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setPosition(Vector3f v) {
		this.position = v;
	}

}
