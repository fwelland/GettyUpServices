package fhw.model;


import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class ICXRateCard
{
    @NotNull 
    @Min(value = 0, groups = {ICDValidatorGroup.class})
    @Max(value = 0, groups = {ICSValidatorGroup.class})    
    private Long id;     
    private Long bankId; 
    private String name;     
    private Integer type;
    private Long onewasySellIndicator;   
    private List<ICXRateTier> tiers; 
}
