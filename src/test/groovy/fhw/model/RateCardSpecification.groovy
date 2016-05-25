package fhw.model

import spock.lang.Specification
import javax.persistence.*
import javax.validation.*
import javax.validation.groups.*
import java.sql.*
import groovy.sql.*

class RateCardSpecification
    extends Specification
{

//    def Validator validator
//
//    def setup()
//    {
//        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
//        validator = vf.getValidator();
//    }
//
//    def "violate non-null for an ICS rate card id"()
//    {
//        given:
//            def icsrtcrd = new ICSRateCard();
//
//        when:
//           def Set<ConstraintViolation<ICSRateCard>> violations = validator.validate(icsrtcrd)
//
//        then:
//            0 < violations.size()
//            violations.each{ println "${it}"  }
//    }
//
//    def "violate natural number restriction for rate card that is an ICD card "()
//    {
//        given:
//            def rtcrd = new ICDRateCard();
//            rtcrd.setId(-9)
//
//        when:
//           def Set<ConstraintViolation<ICSRateCard>> violations = validator.validate(rtcrd, Default.class, ICDValidatorGroup.class)
//
//        then:
//            0 < violations.size()
//            violations.each{ println "${it}"  }
//    }
//
//
//    def "violate must be negative restriction for rate card that is an ICS card "()
//    {
//        given:
//            def rtcrd = new ICSRateCard();
//            rtcrd.setId(9)
//
//        when:
//           def Set<ConstraintViolation<ICSRateCard>> violations = validator.validate(rtcrd, Default.class, ICSValidatorGroup.class)
//
//        then:
//            0 < violations.size()
//            violations.each{ println "${it}"  }
//    }

    
    def EntityManagerFactory emf
    def EntityManager em 
    def Sql sql
    
    def setup() 
    {
        emf = Persistence.createEntityManagerFactory("ProdPU")
        em = emf.createEntityManager()        
        def props = emf.getProperties(); 
        sql = Sql.newInstance( props['javax.persistence.jdbc.url'], props['javax.persistence.jdbc.user'], props['javax.persistence.jdbc.password'], props['javax.persistence.jdbc.driver'])
    }
        
    def cleanup()
    {       
        sql.close()        
        em.close()
    }
    
    def "simple working ICS persist rate card"()
    {
        def rtcrd = new ICSRateCard();
        rtcrd.id = -90
        rtcrd.name = "derp"
        rtcrd.bankId = 24
        rtcrd.type = 78

        when:
            em.getTransaction().begin()          
            em.persist(rtcrd)
            em.getTransaction().commit()
            
        then:
            sql.rows('select * from ICS_RT_CRD where ICS_RT_CRD_ID =?.id ', id:rtcrd.id ).size() == 1

        cleanup: 
            sql.execute( 'delete from ICS_RT_CRD where ICS_RT_CRD_ID =?.id ', id:rtcrd.id )
    }   
    
    def "ICS persist rate card that should violate the id contraint of being gte 0"()
    {
        def rtcrd = new ICSRateCard();
        rtcrd.id = 90
        rtcrd.name = "derp"
        rtcrd.bankId = 24
        rtcrd.type = 78

        when:
            em.getTransaction().begin()          
            em.persist(rtcrd)
            em.getTransaction().commit()
            
        then:
            Exception e = thrown()
            (e instanceof PersistenceException) || (e instanceof ConstraintViolationException )

        cleanup: 
            sql.execute( 'delete from ICS_RT_CRD where ICS_RT_CRD_ID =?.id ', id:rtcrd.id )
    }        
    
    
    def "ICS persist rate card that should violate the id contraint of not null"()
    {
        def rtcrd = new ICSRateCard();
        rtcrd.name = "derp"
        rtcrd.bankId = 24
        rtcrd.type = 78

        when:
            em.getTransaction().begin()          
            em.persist(rtcrd)
            em.getTransaction().commit()
            
        then:
            Exception e = thrown()
            (e instanceof PersistenceException) || (e instanceof ConstraintViolationException )
    }            
    
    def "ICD persist rate card that should violate the id contraint of not null"()
    {
        def rtcrd = new ICDRateCard();
        rtcrd.name = "derp"
        rtcrd.bankId = 24
        rtcrd.type = 78

        when:
            em.getTransaction().begin()          
            em.persist(rtcrd)
            em.getTransaction().commit()
            
        then:
            Exception e = thrown()
            (e instanceof PersistenceException) || (e instanceof ConstraintViolationException )
    }                
    
    
//    def "ICD persist rate card that should violate the id contraint of being lte 0"()
//    {
//        def rtcrd = new ICDRateCard();
//        rtcrd.id = -86
//        rtcrd.name = "derp"
//        rtcrd.bankId = 24
//        rtcrd.type = 78
//
//        when:
//            em.getTransaction().begin()          
//            em.persist(rtcrd)
//            em.getTransaction().commit()
//            
//        then:
//            Exception e = thrown()
//            (e instanceof PersistenceException) || (e instanceof ConstraintViolationException )
//
//        cleanup: 
//            sql.execute( 'delete from ICD_RT_CRD where ICD_RT_CRD_ID =?.id ', id:rtcrd.id )
//    }            
}

