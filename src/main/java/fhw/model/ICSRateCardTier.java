package fhw.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ICSRateCardTier
{    
    private Long id; 
    private Long bankId; 
    private Long programId; 
    private BigDecimal balanceThreshhold; 
    private Long rateType; 
    private Long indexCode; 
    private Long spread; 
    private Long fixedRate; 
    private LocalDateTime createDate; 
    private LocalDateTime updateDate; 
    private String modUserId; 

}
