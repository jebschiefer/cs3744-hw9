package edu.vt.cs.cs3744;

/**
 * The data model.
 *
 * @author Jeb Schiefer
 */
public class Model {

    private String[] args;
    private int selection;

    /**
     * Construct the model.
     *
     * @param input The input String
     */
    public Model(String input) {
        args = input.split(" ");
        selection = -1;
    }

    /**
     * @return An array containing a word from the input string at each index
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * @return The index of the currently selected button
     */
    public int getSelection() {
        return selection;
    }

    /**
     * @param selection The index of the currently selected button
     */
    public void setSelection(int selection) {
        this.selection = selection;
    }
}
