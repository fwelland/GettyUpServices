package fhw.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
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
    
    @Override
    public boolean equals(Object o)
    {
        boolean b = false; 
        if(o instanceof ICSRateCardTier)
        {
            ICSRateCardTier icrt = (ICSRateCardTier)o; 
            b = (bankId == icrt.bankId); 
            b = ( b && (programId == icrt.programId) ); 
            b = ( b && (0 == balanceThreshhold.compareTo(icrt.balanceThreshhold)) );             
            b = ( b && (rateType == icrt.rateType) ); 
            b = ( b && (indexCode == icrt.indexCode) ); 
            b = ( b && (spread == icrt.spread) );             
            b = ( b && (fixedRate == icrt.fixedRate) );                         
        }
        return(b); 
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.bankId);
        hash = 89 * hash + Objects.hashCode(this.programId);
        hash = 89 * hash + Objects.hashCode(this.balanceThreshhold);
        return hash;
    }

}
