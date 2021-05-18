package oop;

import javafx.scene.shape.Rectangle;

public class ISORuut extends Rectangle {
    // Põhimõtteliselt ruut, aga sellele on lisatud koordinaadid mängulaual
    // vajalik on see sellepärast, et siis on võimalik sellele klikates
    // lambda funktsiooni siseselt saada tema koordinaadid tagasi
    private int i;
    private int j;

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public ISORuut(double x, double y, double width, double height, int i, int j) {
        super(x, y, width, height);
        this.i = i;
        this.j = j;
    }
}/**
 ⎛⎝(•ⱅ•)⎠⎞
 */
