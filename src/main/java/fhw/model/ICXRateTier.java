package fhw.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ICXRateTier
{
    private Long id; 
    private Long rateCardId;
    private Long balanceThreshold; 
    private Integer type;
    private Integer indexCd; 
    private BigDecimal spread; 
    private BigDecimal fixedRate; 
    private Boolean  isFixed; 
    private Boolean isDefault;         
    private List<ICXRateTier> tiers; 
}
