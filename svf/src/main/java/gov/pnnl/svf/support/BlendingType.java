package gov.pnnl.svf.support;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 *
 * @author Arthur Bleeker
 */
public enum BlendingType {

    /**
     * Not an actual blending type. Used to specify a field that is not using or
     * making changes to blending.
     */
    NONE(-1),
    /**
     * Factor: (0,0,0) <br/> Alpha: 0
     */
    ZERO(GL.GL_ZERO),
    /**
     * Factor: (1,1,1,) <br/> Alpha: 1
     */
    ONE(GL.GL_ONE),
    /**
     * Factor: (Rs,Gs,Bs) <br/> Alpha: As
     */
    SRC_COLOR(GL.GL_SRC_COLOR),
    /**
     * Factor: (1,1,1)-(Rs,Gs,Bs) <br/> Alpha: 1-As
     */
    ONE_MINUS_SRC_COLOR(GL.GL_ONE_MINUS_SRC_COLOR),
    /**
     * Factor: (Rd,Gd,Bd) <br/> Alpha: Ad
     */
    DST_COLOR(GL.GL_DST_COLOR),
    /**
     * Factor: (1,1,1)-(Rd,Gd,Bd) <br/> Alpha: 1-Ad
     */
    ONE_MINUS_DST_COLOR(GL.GL_ONE_MINUS_DST_COLOR),
    /**
     * Factor: (As,As,As) <br/> Alpha: As
     */
    SRC_ALPHA(GL.GL_SRC_ALPHA),
    /**
     * Factor: (1,1,1)-(As,As,As) <br/> Alpha: 1-As
     */
    ONE_MINUS_SRC_ALPHA(GL.GL_ONE_MINUS_SRC_ALPHA),
    /**
     * Factor: (Ad,Ad,Ad) <br/> Alpha: Ad
     */
    DST_ALPHA(GL.GL_DST_ALPHA),
    /**
     * Factor: (1,1,1)-(Ad,Ad,Ad) <br/> Alpha: 1-Ad
     */
    ONE_MINUS_DST_ALPHA(GL.GL_ONE_MINUS_DST_ALPHA),
    /**
     * Factor: (Rc,Gc,Bc) <br/> Alpha: Ac
     */
    CONSTANT_COLOR(GL2.GL_CONSTANT_COLOR),
    /**
     * Factor: (1,1,1)-(Rc,Gc,Bc) <br/> Alpha: 1-Ac
     */
    ONE_MINUS_CONSTANT_COLOR(GL2.GL_ONE_MINUS_CONSTANT_COLOR),
    /**
     * Factor: (Ac,Ac,Ac) <br/> Alpha: Ac
     */
    CONSTANT_ALPHA(GL2.GL_CONSTANT_ALPHA),
    /**
     * Factor: (1,1,1)-(Ac,Ac,Ac) <br/> Alpha: 1-Ac
     */
    ONE_MINUS_CONSTANT_ALPHA(GL2.GL_ONE_MINUS_CONSTANT_ALPHA),
    /**
     * Factor: (f,f,f);f=min(As,1-Ad) <br/> Alpha: 1
     */
    SRC_ALPHA_SATURATE(GL2.GL_SRC_ALPHA_SATURATE);
    private final int glConstant;

    private BlendingType(final int glConstant) {
        this.glConstant = glConstant;
    }

    /**
     * @return the GL constant for this blending type
     */
    public int getGlConstant() {
        return glConstant;
    }
}
