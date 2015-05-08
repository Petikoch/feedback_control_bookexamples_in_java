package ch.petikoch.examples.feedbackControlInJava.simulationFramework.setpoints;

/**
 * A java port of the a constant setpoint python function from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/feedback.py
 */
public class ConstantSetpointFunction implements SetpointFunction {

    private final double value;

    public ConstantSetpointFunction(double value) {
        this.value = value;
    }

    @Override
    public Double at(long time) {
        return value;
    }
}
