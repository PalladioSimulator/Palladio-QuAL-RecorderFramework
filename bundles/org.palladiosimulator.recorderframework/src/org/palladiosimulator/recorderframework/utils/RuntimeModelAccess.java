package org.palladiosimulator.recorderframework.utils;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

@FunctionalInterface
public interface RuntimeModelAccess {
    
    /**
     * Retrieves model elements from the shared analysis blackboard partition
     *  
     * @param targetType the root element type
     * @return a list of all root elements of the given type
     */
    <T extends EObject> List<T> getElement(final EClass targetType);

}
