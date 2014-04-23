package org.palladiosimulator.recorderframework;

import org.palladiosimulator.measurementspec.Measurement;
import org.palladiosimulator.recorderframework.launch.IRecorderConfiguration;

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
