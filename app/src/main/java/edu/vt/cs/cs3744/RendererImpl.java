package edu.vt.cs.cs3744;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

/**
 * Renders the rectangles using OpenGL ES.
 *
 * @author Jeb Schiefer
 */
public class RendererImpl implements GLSurfaceView.Renderer {

    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 vPosition;" +
            "uniform int selection;" +
            "varying vec4 vColor;" +
            "void main() {" +
            "  gl_Position = vec4(vPosition.x, vPosition.y, 0.0, 1.0);" +
            "  if (int(vPosition.z) == selection) {" +
            "    vColor =  vec4(1.0 , 0.0, 0.0, 1.0);" +
            "  }" +
            "  else {" +
            "    vColor = vec4(0.0, 0.0, 1.0, 1.0);" +
            "  }" +
            "}";

    private static final String FRAGMENT_SHADER_CODE =
            "varying vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private Model model;
    private float vertexData[];
    private FloatBuffer vertexBuffer;
    private int program;

    /**
     * Construct the renderer and create the vertexData array.
     *
     * @param model The data model
     */
    public RendererImpl(Model model) {
        super();

        this.model = model;

        String[] args = model.getArgs();

        int n = args.length;

        vertexData = new float[n * 2 * 3 * 3];

        int max = 0;
        int temp;
        for (int i = 0; i < args.length; i++) {
            temp = args[i].length();
            if (temp > max) {
                max = temp;
            }
        }

        int count = 0;
        for (int i = 0; i < n; i++) {
            // 2 triangles per rectangle
            // triangle 1, vertex 1
            vertexData[count++] = -1.0f + 2.0f / (max + 2);
            vertexData[count++] = 1.0f - (i + 1) * 2.0f / (n + 2);
            vertexData[count++] = i;

            // triangle 1, vertex 2
            vertexData[count++] = -1.0f + 2.0f / (max + 2);
            vertexData[count++] = 1.0f - (i + 2) * 2.0f / (n + 2);
            vertexData[count++] = i;

            // triangle 1, vertex 3
            vertexData[count++] = -1.0f + (args[i].length() + 1) * 2.0f / (max + 2);
            vertexData[count++] = 1.0f - (i + 2) * 2.0f / (n + 2);
            vertexData[count++] = i;

            // triangle 2, vertex 1
            vertexData[count++] = -1.0f + 2.0f / (max + 2);
            vertexData[count++] = 1.0f - (i + 1) * 2.0f / (n + 2);
            vertexData[count++] = i;

            // triangle 2, vertex 2
            vertexData[count++] = -1.0f + (args[i].length() + 1) * 2.0f / (max + 2);
            vertexData[count++] = 1.0f - (i + 2) * 2.0f / (n + 2);
            vertexData[count++] = i;

            // triangle 2, vertex 3
            vertexData[count++] = -1.0f + (args[i].length() + 1) * 2.0f / (max + 2);
            vertexData[count++] = 1.0f - (i + 1) * 2.0f / (n + 2);
            vertexData[count++] = i;
        }
    }

    /**
     * Creates the surface to draw on.
     *
     * @param gl The GL10 object
     * @param config The EGLConfig object
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        // initialize vertex byte buffer for shape coordinates
        // (# of coordinate values * 4 bytes per float)
        ByteBuffer bb = ByteBuffer.allocateDirect(vertexData.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertexData);
        vertexBuffer.position(0);

        int vertexShader = compile(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_CODE);
        int fragmentShader = compile(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE);

        program = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(program, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(program, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(program);                  // create OpenGL program executables
    }

    /**
     * Renders one frame.
     *
     * @param gl The GL10 object
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // Add program to OpenGL environment
        GLES20.glUseProgram(program);

        // get handle to vertex shader's vPosition member
        int positionHandle = GLES20.glGetAttribLocation(program, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // get handle to selected button
        int selectionHandle = GLES20.glGetUniformLocation(program, "selection");
        GLES20.glUniform1i(selectionHandle, model.getSelection());

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexData.length / 3);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    /**
     * Updates the viewport if the surface is changed.
     *
     * @param gl The GL10 object
     * @param width The new width
     * @param height The new height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    /**
     * Compiles the shader.
     *
     * @param type The type
     * @param shaderCode The shader code String to compile
     * @return An int representing the compiled shader
     */
    public static int compile(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

}
