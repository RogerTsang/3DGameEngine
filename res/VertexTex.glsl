#version 130

/* Acknowledge:
	The code of vertex, normal, animating calculation referenced week6 course package
	SimpleVertex.glsl
	SimpleFragment.glsl
	AnimatedVertex.glsl
	PhongVertex.glsl
	PhongFragment.glsl
*/

out vec2 texCoordV;
out vec3 N; 
out vec3 v;
out vec4 transparency;
uniform float time; /* in milliseconds */

void main(void) {
   // Calculate vertex position in modelview
   v = vec3(gl_ModelViewMatrix * gl_Vertex);
   // Calculate normal
   N = vec3(normalize(gl_NormalMatrix * normalize(gl_Normal)));
   
   // Animation
   vec4 t = gl_Vertex;
   // Setup slime's height
   t.y = 0.05*sin(0.01*(time)) + gl_Vertex.y;
   float jumpHeight = (t.y - gl_Vertex.y) * 10 + 0.5;
   transparency = vec4(1.0, 1.0, 1.0, 1 - jumpHeight*0.8);
   
   // Setup slime's waistline
   t.x = 0.5*cos(0.01*(time)+0.8) * sqrt(t.x*t.x+t.z*t.z) * t.x + gl_Vertex.x;
   t.z = 0.5*cos(0.01*(time)+0.8) * sqrt(t.x*t.x+t.z*t.z) * t.z + gl_Vertex.z;
   
   gl_Position =  gl_ModelViewProjectionMatrix*t;
   texCoordV = vec2(gl_MultiTexCoord0);
}