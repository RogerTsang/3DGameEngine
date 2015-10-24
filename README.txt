Key bindings for Game

Key			Action
-------------------------------------------
W			Forward
A			Left
S			Backwards
D			Right
F			Toggles first / third person
Up Arrow	Moves camera upwards
Left Arrow	Moves camera left
Down Arrow	Moves camera downwards
Right Arrow	Moves camera right
R			Toggles fly mode
Space		Fly upwards (in fly mode)
Ctrl		Fly downwards (in fly mode)
Z			Decrease iteration of tree
X			Increase iteration of tree
C			Road extrusion enable/disable

Game:	
	File: Game.java
			Initialisation
			Texture initialisation
			Terrain file entry point creation

Terrain:
	File: Terrain.java
			Altitude calculation
			Positioning

	File: TerrainPainter.java
			Painter of all elements in the scene

	File: TerrainSection.java
			Store basic rendering information (vertexes normal)
			draw(gl) method entry

Trees:
	File: Tree.java
			Tree registration
			Altitude calculation
			Positioning

	File: TerrainSection.java
			Store basic rendering information (vertexes normal)
			draw(gl) method entry

Road:
	File: Road.java
			Road registration
			Altitude calculation
			Positioning

	File: RoadSection.java
			Store basic rendering information (vertexes normal)
			draw(gl) method entry

Lighting:
	File: Lights.java
			Sun model drawing
			Sun light positioning
			GL_LIGHT0 ATTENUATION setting
			GL_LIGHT0 ATTRIBUTE (specular, diffuse, amibient) setting

Texturing:
	File: Texture.java
			Texture registration
			Texture setting

	File: TextureMgr.java (global)
			Texture management
			Texture activate/deactivate for other classes

Avatar:
	File: Avatar.java
			Avatar modeling and texturing

The Others:
	File: Slime.java
			Slime registration
			Altitude calculation
			Positioning

	File: SlimeSection.java
			VBO indexed drawing
			VBO texture coordinates mapping
			Shader of animated vertices
			Shader of simple fragment colour interpolation 

Utilities:
	File: MathUtil.java
			Calculation methods

	File: Shader.java
			Shader registration
			Shader compiler
			
Extensions:
Pond:
	File: Pond.java
			Defines pond Object
			
	File: PondSection.java
			Renders pond (gl entry)
	
Tree L System:
	File: LSystem.java
			Implements L System in String form
	File: TreeSection.java
			Uses L System and has definition of L System grammar
			
Road Extrusion Fix:
	File: RoadSection.java
			Can switch to an alternate form to render the road following terrain