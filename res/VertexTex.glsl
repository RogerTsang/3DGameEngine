#version 130

out vec2 texCoordV;
uniform float time; /* in milliseconds */

void main(void) {
   vec4 t = gl_Vertex;
   // This makes the mine poping up the ground sometimes
   t.y = 0.05*sin(0.001*time) + gl_Vertex.y;
   
   // 
   t.x = 0.5*cos(0.001*time+0.8) * sqrt(t.x*t.x+t.z*t.z) * t.x + gl_Vertex.x;
   t.z = 0.5*cos(0.001*time+0.8) * sqrt(t.x*t.x+t.z*t.z) * t.z + gl_Vertex.z;
   gl_Position =  gl_ModelViewProjectionMatrix*t;
   //gl_FrontColor = gl_Color;
   texCoordV = vec2(gl_MultiTexCoord0);
}