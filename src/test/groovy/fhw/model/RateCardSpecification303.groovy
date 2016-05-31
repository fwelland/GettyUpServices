package fhw.model

import spock.lang.Specification
import javax.validation.*
import javax.validation.groups.*


class RateCardSpecification303
    extends Specification
{
    def Validator validator

    def setup()
    {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }

    def "violate non-null for an ICS rate card id"()
    {
        given:
            def icsrtcrd = new ICSRateCard();

        when:
           def Set<ConstraintViolation<ICSRateCard>> violations = validator.validate(icsrtcrd)

        then:
            0 < violations.size()
            violations.each{ println "${it}"  }
    }

    def "violate natural number restriction for rate card that is an ICD card"()
    {
        given:
            def rtcrd = new ICDRateCard();
            rtcrd.setId(-9)

        when:
           def Set<ConstraintViolation<ICSRateCard>> violations = validator.validate(rtcrd, Default.class, ICDValidatorGroup.class)

        then:
            0 < violations.size()
            violations.each{ println "${it}"  }
    }


    def "violate must be negative restriction for rate card that is an ICS card "()
    {
        given:
            def rtcrd = new ICSRateCard();
            rtcrd.setId(9)

        when:
        def Set<ConstraintViolation<ICSRateCard>> violations = validator.validate(rtcrd, Default.class, ICSValidatorGroup.class)

        then:
            0 < violations.size()
            violations.each{ println "${it}"  }
    }
    
}

