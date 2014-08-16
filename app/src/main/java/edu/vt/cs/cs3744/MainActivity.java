package edu.vt.cs.cs3744;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Main activity class for Homework 9.
 *
 * @author Jeb Schiefer
 */
public class MainActivity extends ActionBarActivity implements OnClickListener {

    public Model model;
    private MyGLSurfaceView surfaceView;

    /**
     * Programmatically creates buttons for each word in the input string and adds
     * initial model to the GLSurfaceView.
     *
     * @param savedInstanceState The application's saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        model = new Model(getResources().getString(R.string.input));

        String[] args = model.getArgs();

        if (args.length > 0) {
            LinearLayout buttonView = (LinearLayout) findViewById(R.id.button_layout);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) buttonView.getLayoutParams();
            params.bottomMargin = 1;
            params.topMargin = 1;
            params.leftMargin = 1;
            params.rightMargin = 1;

            for (int i = 0; i < args.length; i++) {
                Button button = new Button(this);
                button.setId(i);
                button.setText(args[i]);
                button.setLayoutParams(params);
                button.setBackgroundColor(Color.WHITE);
                button.setTextColor(Color.BLUE);
                button.setOnClickListener(this);
                buttonView.addView(button);
            }
        }

        surfaceView = (MyGLSurfaceView) findViewById(R.id.surface_view);
        surfaceView.setModel(model);
    }

    /**
     * Inflates the options menu. Unused in this application.
     *
     * @param menu The application menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Process the menu item that was selected.
     *
     * @param item The selected menu item
     * @return True if successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Updates the selection in the model every time a button is clicked.
     *
     * @param view The view
     */
    @Override
    public void onClick(View view) {
        if (model.getSelection() == view.getId()) {
            model.setSelection(-1);
        }
        else {
            model.setSelection(view.getId());
        }
    }

    /**
     * Pause the view.
     */
    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.onPause();
    }

    /**
     * Resume the view.
     */
    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.onResume();
    }
}
