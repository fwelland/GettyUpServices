package fhw.model;

import javax.validation.constraints.NotNull;

public class ICDRateCard
    extends ICXRateCard
{    
    
    public Integer compute(Integer a, Integer b)
    {
        return(a + b); 
    }    
}
