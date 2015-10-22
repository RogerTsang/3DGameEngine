#version 130

in vec2 texCoordV;
uniform sampler2D texUnit;

void main (void) {
	// Attach texture to model
	gl_FragColor = texture(texUnit, texCoordV);
}
