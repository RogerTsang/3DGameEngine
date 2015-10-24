#version 130

in vec2 texCoordV;
uniform sampler2D texUnit;
uniform float transparency;

void main (void) {

	vec4 trans = vec4(1.0, 1.0, 1.0, transparency);
	gl_FragColor = texture(texUnit, texCoordV) * (gl_FrontMaterial.ambient * gl_FrontMaterial.specular * gl_FrontMaterial.diffuse) * trans; 
}
