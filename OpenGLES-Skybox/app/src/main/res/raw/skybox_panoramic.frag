#version 330

varying vec4 fragColor;
varying vec3 vVertex;

uniform sampler2D panorama;

vec3 equirectangular(vec2 xy) {
	vec2 tc = xy / vec2(2.0) - 0.5;
	vec2 thetaphi =
		((tc * 2.0) - vec2(1.0)) * vec2(3.1415926535897932384626433832795, 1.5707963267948966192313216916398);
	vec3 rayDirection = vec3(cos(thetaphi.y) * cos(thetaphi.x), sin(thetaphi.y), cos(thetaphi.y) * sin(thetaphi.x));
	return rayDirection;
}

vec2 inverse_equirectangular(vec3 direction) {
	const vec2 invAtan = vec2(0.1591, 0.3183);
	vec2 uv = vec2(atan(direction.z, direction.x), asin(direction.y));
	uv *= invAtan;
	uv += 0.5;
	return uv;
}

void main() {
	vec2 uv = inverse_equirectangular(normalize(vVertex));
	fragColor = texture(panorama, uv);
}
