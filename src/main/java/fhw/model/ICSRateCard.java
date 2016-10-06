
package fhw.model;

import javax.validation.constraints.NotNull;

public class ICSRateCard
    extends ICXRateCard
{  

    @Override
    public Integer compute(Integer a, Integer b)
    {
        return(a * b); 
    }
    
}
