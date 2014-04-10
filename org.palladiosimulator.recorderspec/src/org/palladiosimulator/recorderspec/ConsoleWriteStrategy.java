package org.palladiosimulator.recorderspec;

import org.palladiosimulator.probespec.framework.measurements.Measurement;
import org.palladiosimulator.recorderspec.launch.IRecorderConfiguration;

public class ConsoleWriteStrategy implements IRawWriteStrategy {

    @Override
    public void flush() {
    }

    @Override
    public void initialize(final IRecorderConfiguration recorderConfiguration) {
    }

    @Override
    public void writeData(final Measurement data) {
        System.out.println(data);
    }

}
