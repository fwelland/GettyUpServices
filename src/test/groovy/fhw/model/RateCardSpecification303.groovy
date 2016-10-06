package fhw.model

import spock.lang.Specification
import javax.validation.*
import javax.validation.groups.*
import javax.validation.executable.*
import java.lang.reflect.*

class RateCardSpecification303
    extends Specification
{
    def Validator validator
    def ExecutableValidator evlad

    def setup()
    {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
        evlad = validator.forExecutables()
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
    
    
    def "test out the method level 303 non-null stuff "()
    {
        given:
            def rtcrd = new ICSRateCard();
            rtcrd.setId(-9)

        when:
            def Set<ConstraintViolation<ICSRateCard>> violations = validator.validate(rtcrd, Default.class, ICSValidatorGroup.class)
        then:
            0 == violations.size()
        when:             
            Method m = rtcrd.getClass().getMethod( "compute", Integer.class, Integer.class);  
            
        then: 
            m            
            
        when: 
           Object[] params = [ null, null ]        
           Class[] groups = [Default.class]
           violations = evlad.validateParameters(rtcrd,m,params,groups)
            
        then: 
            violations.size() > 0 
            violations.each{ println "${it}"  }
    }    
    
}

