# svf
Sci-Vis Framework

Sci-Vis Framework (SVF) is a full featured OpenGL 3d framework that allows for rapid creation of complex visualizations. The framework handles much of the lifecycle and complex tasks required for a 3d visualization. Unlike a game framework, SVF was designed to use fewer resources, work well in a windowed environment, and only render when necessary. The scene also takes advantage of multiple threads to free up the UI thread as much as possible. Shapes (actors) in the scene are created by adding or removing functionality (through support objects) during runtime. This allows a highly flexible and dynamic means of creating highly complex actors without the code complexity (it also helps overcome the lack of multiple inheritance in Java.) All classes are highly customizable and there are abstract classes which are intended to be subclassed to allow a developer to create more complex and highly performant actors. There are multiple demos included in the framework to help the developer get started and shows off nearly all of the functionality. Some simple shapes (actors) are already created for you such as text, bordered text, radial text, text area, complex paths, NURBS paths, cube, disk, grid, plane, geometric shapes, and volumetric area. It also comes with various camera types for viewing that can be dragged, zoomed, and rotated. Picking or selecting items in the scene can be accomplished in various ways depending on your needs (raycasting or color picking.) The framework currently has functionality for tooltips, animation, actor pools, color gradients, 2d physics, text, 1d/2d/3d textures, children, blending, clipping planes, view frustum culling, custom shaders, and custom actor states.

Pacific Northwest National Laboratory

Arthur Bleeker

arthur.bleeker@pnnl.gov
