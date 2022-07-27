# SimpleRayMarching

Simple Ray marcher made in Java using LWJGL

the OpenGL code is basically a quad set at :
```java
float[] vertices = { -1f, 1f, 0f, -1f, -1f, 0f, 1f, -1f, 0f, 1f, -1f, 0f, 1f, 1f, 0f, -1f,1f, 0f };
```

loaded as a VAO
```java
RawModel quad = loader.loadToVAO(vertices);
```

Then using a basic Camera entity to handle the control (AZERTY keyboard layout !)

```java
Camera cam = new Camera();
while (!Display.isCloseRequested()) {
	renderer.prepare();

	cam.move();
	renderer.getShader().loadCam(cam);
	renderer.render(quad);
			
	DisplayManager.updateDisplay();
}
```

All the rendering is made in the fragment shader as so

```glsl
void main(void) {
	vec2 uv = (gl_FragCoord.xy - .5 * screenResolution.xy) / screenResolution.y;

	vec3 ro = camPosition;

	vec3 lookat = camPosition - normalize(camRotation);

	float zoom = 1.;

	vec3 forward = normalize(lookat - ro);
	vec3 right = cross(vec3(0., 1., 0.), forward);
	vec3 up = cross(forward, right);

	vec3 c = ro + forward * zoom;
	vec3 i = c + uv.x * right + uv.y * up;
	vec3 rd = i - ro;

	float d = RayMarch(ro, rd);

	vec3 p = ro + rd * d;

	float dif = GetLight(p);
	vec3 col = vec3(dif);

	col = pow(col, vec3(.4545));	// gamma correction

	outColor = vec4(col, 1.0);
}
```
