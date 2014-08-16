package edu.vt.cs.cs3744;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Custom GLSurfaceView to display rectangles for each button.
 *
 * @author Jeb Schiefer
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private RendererImpl renderer;
    private Model model;

    /**
     * Creates the renderer.
     *
     * @param context The context
     */
    public MyGLSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
    }

    /**
     * Creates the renderer.
     *
     * @param context The context
     * @param attributeSet The attribute set
     */
    public MyGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
    }

    /**
     * Sets the model in the renderer. Requests the view to be re-rendered.
     *
     * @param model The model
     */
    public void setModel(Model model) {
        this.model = model;
        renderer = new RendererImpl(model);
        setRenderer(renderer);
    }

    /**
     * @return The RendererImpl for the view
     */
    public RendererImpl getRenderer() {
        return renderer;
    }

}
