package org.palladiosimulator.recorderframework.edp2;

import org.palladiosimulator.edp2.models.ExperimentData.MeasuringType;
import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPoint;
import org.palladiosimulator.metricspec.MetricDescription;

/**
 * A Helper class to check whether two MeasuringTypes are equivalent, that is they describe the same type of measure, that is the same metric and the same place in the system to measure. 
 * This check cannot just be done with MeasuringType.equals(..) as two MeasuringTypes might have a different ids even if they are equivalent, because the ids in SimuCom are created randomly during independent simulation runs. 
 *  
 * @author Anne Koziolek
 *
 */
public class MeasuringTypeEquivalenceHelper {
	
	/**
	 * Check whether two {@link MeasuringPoint}s are equivalent. Again, we cannot use the id as it may be different. 
	 * Thus, we check whether the {@link MeasuringPoint}s refer to the same model element by checking whether both the 
	 * ResourceURISpecification and the StringRepresentation are equal.  
	 * @param measuringPoint1 One of the {@link MeasuringPoint} to compare
	 * @param measuringPoint2 The other {@link MeasuringPoint} to compare
	 * @return true if equivalent. 
	 */
	private static boolean isEquivalent(MeasuringPoint measuringPoint1, MeasuringPoint measuringPoint2) {
		return measuringPoint1.getResourceURIRepresentation().equals(measuringPoint2.getResourceURIRepresentation())
                && measuringPoint1.getStringRepresentation().equals(measuringPoint2.getStringRepresentation());
	}
			
		
	/**
	 * Helper method to check equivalence of two {@link MeasuringType}. See class documentation. 
	 * @param measuringType1 One of the {@link MeasuringType} to compare
	 * @param measuringType2 The other {@link MeasuringType} to compare
	 * @return true if equivalent. 
	 */
	public static boolean isEquivalent(MeasuringType measuringType1, MeasuringType measuringType2) {
		return isEquivalent(measuringType1, measuringType2.getMetric(), measuringType2.getMeasuringPoint());
	}
	
	/**
	 * Helper method to check equivalence of two measuring types (i.e. place in system and metric) if the second measuring type is not yet represented by a {@link MeasuringType} object.  
	 * @param measuringType {@link MeasuringType} 
	 * @param metricDescription {@link MetricDescription} of the second {@link MeasuringType} to compare to
	 * @param measuringPoint {@link MeasuringPoint} of the second {@link MeasuringType} to compare to
	 * @return true if equivalent. 
	 */
	public static boolean isEquivalent(MeasuringType measuringType, MetricDescription metricDescription, MeasuringPoint measuringPoint) {
		return measuringType.getMetric().equals(metricDescription)
                && isEquivalent(measuringType.getMeasuringPoint(), measuringPoint);
	}

}
