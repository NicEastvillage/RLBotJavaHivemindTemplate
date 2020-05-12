package rlbotexample;

import rlbotexample.output.ControlsOutput;

public class Drone {

    public final int index;
    public ControlsOutput controls = new ControlsOutput();

    public Drone(int index) {
        this.index = index;
    }
}
