#version 130

/* Acknowledge:
	The code of vertex, normal, animating calculation referenced week6 course package
	AnimatedVertex.glsl
*/

out vec2 texCoordV;
uniform float time; /* in milliseconds */
uniform float height;

void main(void) {

   // Animation
   vec4 t = gl_Vertex;
   // Setup Wave's height
   t.y = 0.05 * sin(0.001 * time + 5.0 * gl_Vertex.x) * cos (0.001 * time + 5.0 * gl_Vertex.z) * height;
   
   gl_Position =  gl_ModelViewProjectionMatrix * t;
   texCoordV = vec2(gl_MultiTexCoord0);
}