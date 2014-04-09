package org.palladiosimulator.recorderspec;

import org.palladiosimulator.recorderspec.launch.IRecorderConfiguration;

import de.uka.ipd.sdq.probespec.framework.measurements.Measurement;

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
