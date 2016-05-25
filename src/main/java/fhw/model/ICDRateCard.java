package fhw.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


public class ICDRateCard
        extends ICXRateCard
{
    
    public void setId(@Min(value=0 )Long lid)
    {
        id = lid;
    }    
}
