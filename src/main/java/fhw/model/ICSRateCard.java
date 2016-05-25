
package fhw.model;

import javax.validation.constraints.Max;

public class ICSRateCard
    extends ICXRateCard
{
    
    public void setId(@Max(value=0)Long lid)
    {
        id = lid;
    }
   
}
