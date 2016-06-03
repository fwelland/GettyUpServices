package fhw.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RateCard
{
    private Long bankId;
    private Long programId;
    private String name;
    private List<ICSRateCardTier> tiers;

//    private RateCardPK key;
//    @Getter @Setter
//    public static class RateCardPK
//    {
//        private Long bankId;
//        private Long programId;
//    }

    public void addTier(ICSRateCardTier tier)
    {
        if(null == tiers)
        {
            tiers = new ArrayList<>();
        }
        tiers.add(tier); 
    }
}
