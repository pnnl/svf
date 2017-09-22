package gov.pnnl.svf.vbo;

import gov.pnnl.svf.geometry.Arc2D;
import gov.pnnl.svf.geometry.Chart2D;
import gov.pnnl.svf.geometry.Circle2D;
import gov.pnnl.svf.geometry.Cuboid3D;
import gov.pnnl.svf.geometry.Path2D;
import gov.pnnl.svf.geometry.Path3D;
import gov.pnnl.svf.geometry.PathVector3D;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Point3D;
import gov.pnnl.svf.geometry.Polygon2D;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.geometry.RoundedRectangle2D;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Sphere3D;
import gov.pnnl.svf.geometry.Text2D;
import gov.pnnl.svf.geometry.Text3D;
import gov.pnnl.svf.geometry.Volume3D;
import gov.pnnl.svf.scene.Scene;
import java.util.HashMap;
import java.util.Map;

/**
 * Standard implementation of the VBO shape service.
 *
 * @author Arthur Bleeker
 */
public class VboShapeServiceImpl implements VboShapeService {

    private final Map<Class<?>, VboShapeFactory> map = new HashMap<>();

    /**
     * Constructor
     */
    private VboShapeServiceImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scene reference to the scene
     *
     * @return a new instance
     */
    public static VboShapeServiceImpl newInstance(final Scene scene) {
        final VboShapeServiceImpl instance = new VboShapeServiceImpl();
        instance.setVboShapeFactory(Arc2D.class, new Arc2DVboFactory(scene));
        instance.setVboShapeFactory(Circle2D.class, new Circle2DVboFactory(scene));
        instance.setVboShapeFactory(Cuboid3D.class, new Cuboid3DVboFactory(scene));
        instance.setVboShapeFactory(Path2D.class, new Path2DVboFactory(scene));
        instance.setVboShapeFactory(Path3D.class, new Path3DVboFactory(scene));
        instance.setVboShapeFactory(PathVector3D.class, new PathVector3DVboFactory(scene));
        instance.setVboShapeFactory(Point2D.class, new Point2DVboFactory(scene));
        instance.setVboShapeFactory(Point3D.class, new Point3DVboFactory(scene));
        instance.setVboShapeFactory(Polygon2D.class, new Polygon2DVboFactory(scene));
        instance.setVboShapeFactory(Rectangle2D.class, new Rectangle2DVboFactory(scene));
        instance.setVboShapeFactory(Rectangle.class, new RectangleVboFactory(scene));
        instance.setVboShapeFactory(RoundedRectangle2D.class, new RoundedRectangle2DVboFactory(scene));
        instance.setVboShapeFactory(Sphere3D.class, new Sphere3DVboFactory(scene));
        instance.setVboShapeFactory(Text2D.class, new Text2DVboFactory(scene));
        instance.setVboShapeFactory(Text3D.class, new Text3DVboFactory(scene));
        instance.setVboShapeFactory(Volume3D.class, new Volume3DVboFactory(scene));
        instance.setVboShapeFactory(Chart2D.class, new Chart2DVboFactory(scene));
        scene.add(instance);
        return instance;
    }

    @Override
    public <T extends Shape> VboShapeFactory getVboShapeFactory(final Class<T> type) {
        VboShapeFactory factory = map.get(type);
        if (factory == null) {
            // find best case factory
            final Class<?>[] classes = type.getClasses();
            for (Class<?> classe : classes) {
                factory = map.get(classe);
                if (factory != null) {
                    break;
                }
            }
        }
        return factory;
    }

    @Override
    public <T extends Shape> void setVboShapeFactory(final Class<T> type, final VboShapeFactory vboShapeFactory) {
        if (vboShapeFactory == null) {
            map.remove(type);
        } else {
            map.put(type, vboShapeFactory);
        }
    }

}
