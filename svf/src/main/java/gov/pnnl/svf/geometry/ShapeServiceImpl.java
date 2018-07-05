package gov.pnnl.svf.geometry;

import gov.pnnl.svf.scene.Scene;
import java.util.HashMap;
import java.util.Map;

/**
 * Standard implementation of the shape service.
 *
 * @author Amelia Bleeker
 */
public class ShapeServiceImpl implements ShapeService {

    private final Map<Class<?>, ShapeRenderer> map = new HashMap<>();

    /**
     * Constructor
     */
    private ShapeServiceImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scene reference to the scene
     *
     * @return a new instance
     */
    public static ShapeServiceImpl newInstance(final Scene scene) {
        final ShapeServiceImpl instance = new ShapeServiceImpl();
        instance.setShapeRenderer(Arc2D.class, new Arc2DRenderer(scene));
        instance.setShapeRenderer(Circle2D.class, new Circle2DRenderer(scene));
        instance.setShapeRenderer(Cuboid3D.class, new Cuboid3DRenderer(scene));
        instance.setShapeRenderer(Path2D.class, new Path2DRenderer(scene));
        instance.setShapeRenderer(Path3D.class, new Path3DRenderer(scene));
        instance.setShapeRenderer(PathVector3D.class, new PathVector3DRenderer(scene));
        instance.setShapeRenderer(Point2D.class, new Point2DRenderer(scene));
        instance.setShapeRenderer(Point3D.class, new Point3DRenderer(scene));
        instance.setShapeRenderer(Polygon2D.class, new Polygon2DRenderer(scene));
        instance.setShapeRenderer(Rectangle2D.class, new Rectangle2DRenderer(scene));
        instance.setShapeRenderer(Rectangle.class, new RectangleRenderer(scene));
        instance.setShapeRenderer(RoundedRectangle2D.class, new RoundedRectangle2DRenderer(scene));
        instance.setShapeRenderer(Sphere3D.class, new Sphere3DRenderer(scene));
        instance.setShapeRenderer(Text2D.class, new Text2DRenderer(scene));
        instance.setShapeRenderer(Text3D.class, new Text3DRenderer(scene));
        instance.setShapeRenderer(Volume3D.class, new Volume3DRenderer(scene));
        instance.setShapeRenderer(Chart2D.class, new Chart2DRenderer(scene));
        scene.add(instance);
        return instance;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Shape> ShapeRenderer getShapeRenderer(final Class<T> type) {
        ShapeRenderer renderer = map.get(type);
        if (renderer == null) {
            // find best case renderer
            final Class<?>[] classes = type.getClasses();
            for (Class<?> classe : classes) {
                renderer = map.get(classe);
                if (renderer != null) {
                    break;
                }
            }
        }
        return renderer;
    }

    @Override
    public <T extends Shape> void setShapeRenderer(final Class<T> type, final ShapeRenderer shapeRenderer) {
        if (shapeRenderer == null) {
            map.remove(type);
        } else {
            map.put(type, shapeRenderer);
        }
    }

}
