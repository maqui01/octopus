/**
 * 
 */
package papasoft.octopus.webapp.utils;

import java.io.Serializable;

/**
 * @author maqui
 *
 */
public class ValueHolder implements Serializable
{        private static final long serialVersionUID = 1L;
    public Double value;
    
    public ValueHolder()
    {
    }
    
    public ValueHolder(Double v)
    {
        value=v;
    }
    
    public Double getValue()
    {
        return value;
    }
    
    public void setValue(Double v)
    {
        value=v;
    }
    
}
