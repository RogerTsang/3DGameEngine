#version 130

in vec2 texCoordV;
uniform sampler2D texUnit;

void main (void) {
	gl_FragColor = texture(texUnit, texCoordV) * vec4(1,1,1,0.6); 
}
