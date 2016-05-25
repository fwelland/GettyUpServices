package fhw.model;


import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class ICXRateCard
{
    @NotNull 
    @Setter(AccessLevel.NONE)
    protected Long id; 
    
    private Long bankId; 
    private String name;     
    private Integer type;
    private Long onewasySellIndicator;   
    private List<ICXRateTier> tiers; 
}
