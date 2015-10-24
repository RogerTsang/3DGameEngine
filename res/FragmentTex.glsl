#version 130

in vec3 N; 
in vec3 v; 
in vec2 texCoordV;
in float jumpHeight;
uniform sampler2D texUnit;

void main (void) {
	vec3 normal, lightDir;
	vec4 ambient, diffuse, specular;
	
	// Calculate the unit vector from current pixel to lightsource[0]
	lightDir = normalize(vec3(gl_LightSource[0].position.xyz - v));
	
	// Calculate normalised normal
	normal = normalize(normal);
	
	// Calculate ambient light
	ambient =  (gl_FrontMaterial.ambient * gl_LightSource[0].ambient);
	
	// Calculate diffuse light
	diffuse =  (max(dot(normal, lightDir), 0.0) * gl_FrontMaterial.diffuse * gl_LightSource[0].diffuse);
	
	// Calculate specular light
	specular = (gl_FrontMaterial.specular * gl_LightSource[0].specular);
	
	// Attach texture to model
	vec4 transparency = vec4(1.0, 1.0, 1.0, 1 - jumpHeight*0.8);
	gl_FragColor = texture(texUnit, texCoordV) * (ambient + diffuse + specular) * transparency; 
}
