#version 400

#define MAX_STEPS 100
#define MAX_DIST 500.
#define SURF_DIST .001
#define M_PI 3.1415926535897932384626433832795

out vec4 outColor;

uniform vec3 screenResolution;
uniform float time;
uniform vec3 camPosition;
uniform vec3 camRotation;

struct sphere {
	vec3 position;
	float size;
	vec3 color;
};

struct cubox {
	vec3 position;
	vec3 size;
	vec3 color;
};

struct column {
	vec3 position;
	vec3 size;
	vec3 color;
};

sphere s = sphere(vec3(cos(time) * 6, 2, sin(time * 2) * 6), 1,
		vec3(1., 0., 0.));
sphere sd = sphere(vec3(cos(time) * -6, 2, sin(time) * -6), 1, vec3(0, 1, 0));

cubox c = cubox(vec3(0, 2, -6), vec3(1.), vec3(0., 0., 1.));
cubox cb = cubox(vec3(0, 2, 6), vec3(1.), vec3(1, 1, 0));
cubox cl = cubox(vec3(-6, 2, 0), vec3(1.), vec3(0, 1, 1));
cubox cr = cubox(vec3(6, 2, 0), vec3(1.), vec3(1, 1, 0));

//column col = column(vec3(0), vec3())

float sdCylinder(vec3 p, vec3 c) {
	return length(p.xz - c.xy) - c.z;
}

float sdSphere(vec3 p, sphere s) {
	return length(p - s.position) - s.size;
}

vec3 modulus(vec3 p, vec3 c) {
	return mod(p + 0.5 * c, c) - 0.5 * c;
}

//float opDisplace(vec3 p )
//{
//    float d1 = primitive(p);
//    float d2 = displacement(p);
//    return d1+d2;
//}

float cubeSDF(vec3 p, cubox c) {
	vec3 d = abs(p - c.position) - vec3(c.size);
	float insideDistance = min(max(d.x, max(d.y, d.z)), 0.0);
	return insideDistance - length(max(d, 0.0));
}

float columnSDF(vec3 p, vec3 pos) {
	float retour = 0.;

	float rad = 2.;
	rad -= 0.05 * p.y; //
	rad -= 0.1 * pow(0.5 + 0.5 * sin(16.0 * atan(p.x, p.z)), 2.);

	retour += length(p.xz - pos.xz) - rad; //base cinlindre
	retour = max(retour, p.y - 8.); // niveau haut
	retour = max(retour, -p.y - 2.); // niveau bas

	return retour;
}

float temple(vec3 p, vec3 pos) {

	vec3 q = p - pos;
	// columns
	float d = length(q.xz) - 0.9 + 0.05 * p.y;
	d = max(d, p.y - 6.0);
	d = max(d, -p.y - 5.0);
	d -= 0.05 * pow(0.5 + 0.5 * sin(atan(q.x, q.z) * 16.0), 2.0);
	d -= 0.15 * pow(0.5 + 0.5 * sin(q.y * 3.0 + 0.6), 0.12) - 0.15;
	d *= 0.85;

	return d;
}

// ----- OPERATOR -----

float smoothMin(float d1, float d2, float value) {
	float h = max(value - abs(d1 - d2), 0) / value;
	return min(d1, d2) - h * h * h * value * 1 / 6.0;
}

float opSmoothUnion(float d1, float d2, float k) {
	float h = clamp(0.5 + 0.5 * (d2 - d1) / k, 0.0, 1.0);
	return mix(d2, d1, h) - k * h * (1.0 - h);
}

float opSmoothIntersection(float d1, float d2, float k) {
	float h = clamp(0.5 - 0.5 * (d2 - d1) / k, 0.0, 1.0);
	return mix(d2, d1, h) + k * h * (1.0 - h);
}

// ----- MAINS FUNCTIONS -----
float GetDist(vec3 p) {

//	float sphereDist = sdSphere(p, s);
//	vec4 sphereDist2 = sdSphere(p, sd);
	float bc = temple(p, c.position);
//	vec4 bcb = cubeSDF(p, cb);
//	vec4 bcl = cubeSDF(p, cl);
//	vec4 bcr = cubeSDF(p, cr);

//	float d = smoothMin(sphereDist, bc, 5.);
//	d = opSmoothUnion(d, sphereDist2, 4);
//	d = opSmoothUnion(d, bcb, 3);
//	d = opSmoothUnion(d, bcl, 2);
//	d = opSmoothUnion(d, bcr, 1);

	float d = min(bc, p.y + 2);
//	if(d.w)
	return d;
}

float RayMarch(vec3 ro, vec3 rd) {
	float dO = 0.;

//	float minDist = MAX_DIST;

	for (int i = 0; i < MAX_STEPS; i++) {
		vec3 p = ro + rd * dO;
		float dS = GetDist(p);
		dO = dO + dS;

		if (dO > MAX_DIST || dS < SURF_DIST) {
			break;
		}

	}

	return dO;
}

//LIGHTING
vec3 GetNormal(vec3 pos) {
	vec2 e = vec2(.01, 0);
	float d = GetDist(pos);
	vec3 n = vec3(d - GetDist(pos - e.xyy), d - GetDist(pos - e.yxy),
			d - GetDist(pos - e.yyx));

	return normalize(n);
}

float GetLight(vec3 pos) {
	//vec3 lightSource = vec3(2.*cos(iTime), 10., 2.*sin(iTime));
	vec3 lightSource = vec3(0., 15., 0.);

	vec3 lightDir = normalize(lightSource - pos);

	float d = RayMarch(pos + GetNormal(pos) * SURF_DIST * 2., lightDir);

	if (d > length(pos - lightSource)) {
		return dot(normalize(lightSource.xyz - pos), GetNormal(pos));
	} else {
		return dot(normalize(lightSource.xyz - pos), GetNormal(pos)) * 0.3;
	}
}

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
