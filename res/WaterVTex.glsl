#version 130

/* Acknowledge:
	The code of vertex, normal, animating calculation referenced week6 course package
	AnimatedVertex.glsl
   garoudVertex.glsl
*/

out vec2 texCoordV;
uniform float time; /* in milliseconds */

void main(void) {
   vec3 normal, v, lightDir;
   vec4 diffuse, ambient, globalAmbient;
   float NdotL;
   
   // Compute the ambient and globalAmbient terms 
   ambient =  gl_LightSource[0].ambient * gl_FrontMaterial.ambient;
   globalAmbient = gl_LightModel.ambient * gl_FrontMaterial.ambient;
   
   
   // Compute the diffuse term 
   v = vec3(gl_ModelViewMatrix * gl_Vertex);
   normal = normalize(gl_NormalMatrix * gl_Normal);
   lightDir = normalize(gl_LightSource[0].position.xyz - v);
   NdotL = max(dot(normal, lightDir), 0.0);
   diffuse = gl_FrontMaterial.diffuse * gl_LightSource[0].diffuse * NdotL;

   float NdotHV;
   float NdotR;
   vec3 dirToView = normalize(-v);
    
   vec3 R = normalize(reflect(-lightDir,normal)); 
   vec3 H =  normalize(lightDir+dirToView); 
   
   // compute the specular term if NdotL is  larger than zero, otherwise leave it as 0
 
   vec4 specular = vec4(0.0,0.0,0.0,1); 
   if (NdotL > 0.0) {
      //NdotR = max(dot(R,dirToView ),0.0);
      
      /*Can use the halfVector instead of the reflection vector if you wish. That is what the fixed function pipeline uses */
      NdotHV = max(dot(normal,H) ,0.0);
      
      specular = gl_FrontMaterial.specular * gl_LightSource[0].specular * pow(NdotHV,gl_FrontMaterial.shininess);
   }

   // Animation
   vec4 t = gl_Vertex;
   // Setup slime's height
   t.y = 0.05 * sin(0.001 * time + 5.0 * gl_Vertex.x) * cos (0.001 * time + 5.0 * gl_Vertex.z);
   
   gl_FrontColor = gl_FrontMaterial.emission + globalAmbient + ambient + diffuse + specular;
   gl_Position =  gl_ModelViewProjectionMatrix * t;
   texCoordV = vec2(gl_MultiTexCoord0);
}