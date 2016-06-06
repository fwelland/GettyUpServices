package fhw.model
import spock.lang.*
import javax.persistence.*
import javax.validation.*
import javax.validation.groups.*
import java.sql.*
import groovy.sql.*
import java.time.*

class NewRateCardTierSpecificationJPA
    extends Specification
{

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
        sql?.close()
        em?.close()
    }

    def "simple insert of a rate card"()
    {
        def rc = new RateCard();
        rc.bankId = -678
        rc.programId = 57
        rc.name = "Hector"


        when:
            em.getTransaction().begin()
            em.persist(rc)
            em.getTransaction().commit()

        then:
            sql.rows("select * from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 1

        cleanup:
            sql.execute("delete from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId)

    }

    def "simple insert of a rate card with a tier"()
    {
        def rc = new RateCard();
        rc.bankId = -678
        rc.programId = 57
        rc.name = "Hector"

        def tier = new ICSRateCardTier();
        tier.id = -675
        tier.bankId = rc.bankId
        tier.programId = rc.programId
        tier.balanceThreshhold =  1000000
        tier.rateType = 1
        tier.spread = 1
        tier.fixedRate = 1
        tier.createDate = LocalDateTime.now()
        tier.updateDate = LocalDateTime.now()
        tier.modUserId = "frankie"

        rc.addTier(tier)

        when:
            em.getTransaction().begin()
            em.persist(rc)
            em.getTransaction().commit()

        then:
            sql.rows("select * from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 1
            sql.rows("select * from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 1

        cleanup:
            sql.execute("delete from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId)
            sql.execute("delete from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId)

    }


    def "simple insert of a rate card with a few tiers"()
    {
        def theBankId = -678
        def progId =  57
        def startId = -343
        def rc = new RateCard();
        rc.bankId = theBankId
        rc.programId = 57
        rc.name = "Hector"

        4.times{
            def tier = new ICSRateCardTier();
            tier.id = startId
            tier.bankId = -98
            tier.programId = 4
            tier.balanceThreshhold =  (1000000 * (it + 1))
            tier.rateType = 1
            tier.spread = 1
            tier.fixedRate = 1
            tier.createDate = LocalDateTime.now()
            tier.updateDate = LocalDateTime.now()
            tier.modUserId = "frankie ${it}"
            startId += -1
            rc.addTier(tier)
        }


        when:
            em.getTransaction().begin()
            em.persist(rc)
            em.getTransaction().commit()

        then:
            sql.rows("select * from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 1
            sql.rows("select * from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 4

        cleanup:
            sql.execute("delete from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId)
            sql.execute("delete from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId)

    }


    def "simple merge of a rate card w tiers"()
    {
        def theBankId = -678
        def progId =  57
        def startId = -343
        def rc = new RateCard();
        rc.bankId = theBankId
        rc.programId = 57
        rc.name = "Hector"

        4.times{
            def tier = new ICSRateCardTier();
            tier.id = startId
            tier.bankId = theBankId
            tier.programId = progId
            tier.balanceThreshhold =  (1000000 * (it + 1))
            tier.rateType = 1
            tier.spread = 1
            tier.fixedRate = 1
            tier.createDate = LocalDateTime.now()
            tier.updateDate = LocalDateTime.now()
            tier.modUserId = "frankie ${it}"
            startId += -1
            rc.addTier(tier)
        }
        em.getTransaction().begin()
        em.persist(rc)
        em.getTransaction().commit()

        when:
            def rex = rc.tiers.find{ it.balanceThreshhold == 2000000 }

        then:
            rex

        when:
            rc.name = "Fido"
            def tierId = rex.id
            rex.balanceThreshhold = 25000000
            rex.modUserId = 'Homer'
            em.getTransaction().begin()
            em.merge(rc)
            em.getTransaction().commit()

        then:
            sql.rows("select * from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 1
            sql.rows("select * from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 4
            sql.eachRow("select * from ratecard where bankId = ${theBankId} and programId = ${progId}")
            { row -> \
                "Fido" == row.NAME
            }
            sql.eachRow("select * from ics_rt_crd_tier where ICS_RT_CRD_TIER_ID = ${tierId}")
            { row -> \
                "Homer" == row.MOD_USR_ID
                25000000 == row.BALANCE_THRESHHOLD
            }

        cleanup:
            sql.execute("delete from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId)
            sql.execute("delete from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId)
    }

    def "do a merge of a rate card w tiers where there was a tier delete then add"()
    {
        def theBankId = -678
        def progId =  57
        def startId = -343
        def rc = new RateCard();
        rc.bankId = theBankId
        rc.programId = 57
        rc.name = "Hector"

        4.times{
            def tier = new ICSRateCardTier();
            tier.id = startId
            tier.bankId = theBankId
            tier.programId = progId
            tier.balanceThreshhold =  (1000000 * (it + 1))
            tier.rateType = 1
            tier.spread = 1
            tier.fixedRate = 1
            tier.createDate = LocalDateTime.now()
            tier.updateDate = LocalDateTime.now()
            tier.modUserId = "frankie ${it}"
            startId += -1
            rc.addTier(tier)
        }
        em.getTransaction().begin()
        em.persist(rc)
        em.getTransaction().commit()


        when:
            def removedSomething = rc.tiers.removeAll{ it.balanceThreshhold == 2000000 }

        then:
            removedSomething
            rc.tiers.size() == 3

        when:
            startId += -1
            rc.name = "Fido"
            def newTier = new ICSRateCardTier();
            newTier.id = startId
            newTier.bankId = theBankId
            newTier.programId = progId
            newTier.balanceThreshhold =  2000000
            newTier.rateType = 3
            newTier.spread = 3
            newTier.fixedRate = 3
            newTier.createDate = LocalDateTime.now()
            newTier.updateDate = LocalDateTime.now()
            newTier.modUserId = "I am the new one"
            rc.addTier(newTier)

            em.getTransaction().begin()
            em.merge(rc)
            em.getTransaction().commit()

        then:
            sql.rows("select * from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 1
            sql.rows("select * from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 4
            sql.eachRow("select * from ratecard where bankId = ${theBankId} and programId = ${progId}")
            { row -> \
                "Fido" == row.NAME
            }
            sql.eachRow("select * from ics_rt_crd_tier where ICS_RT_CRD_TIER_ID = ${startId}")
            { row -> \
                "I am the new one"  == row.MOD_USR_ID
                2000000 == row.BALANCE_THRESHHOLD
            }

        cleanup:
            sql.execute("delete from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId)
            sql.execute("delete from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId)
    }


    def "do a remove then persist of a rate card w tiers where there was a tier delete then add using em remove and persist"()
    {
        def theBankId = -678
        def progId =  57
        def startId = -343
        def rc = new RateCard()
        rc.bankId = theBankId
        rc.programId = 57
        rc.name = "Hector"

        4.times{
            def tier = new ICSRateCardTier();
            tier.id = startId
            tier.bankId = theBankId
            tier.programId = progId
            tier.balanceThreshhold =  (1000000 * (it + 1))
            tier.rateType = 1
            tier.spread = 1
            tier.fixedRate = 1
            tier.createDate = LocalDateTime.now()
            tier.updateDate = LocalDateTime.now()
            tier.modUserId = "frankie ${it}"
            startId += -1
            rc.addTier(tier)
        }
        em.getTransaction().begin()
        em.persist(rc)
        em.detach(rc)
        em.getTransaction().commit()

        when:
            def removedSomething = rc.tiers.removeAll{ it.balanceThreshhold == 2000000 }

        then:
            removedSomething
            rc.tiers.size() == 3

        when:
            startId += -1
            rc.name = "Fido"
            def newTier = new ICSRateCardTier();
            newTier.id = startId
            newTier.bankId = theBankId
            newTier.programId = progId
            newTier.balanceThreshhold =  2000000
            newTier.rateType = 3
            newTier.spread = 3
            newTier.fixedRate = 3
            newTier.createDate = LocalDateTime.now()
            newTier.updateDate = LocalDateTime.now()
            newTier.modUserId = "I am the new one"
            rc.addTier(newTier)

            em.getTransaction().begin()
            def q = em.createNamedQuery("findByBankIdAndProgramId")
            q.setParameter("bankId", theBankId)
            q.setParameter("programId", progId)
            def oldRc = q.getSingleResult()

        then:
            oldRc
            oldRc.tiers.size() == 4
            rc.tiers.size() == 4

        when:
            em.remove(oldRc)

        then:
            sql.rows("select * from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 0
            sql.rows("select * from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 0

        when:
            em.flush()
            em.persist(rc)
            em.getTransaction().commit()

        then:
            sql.rows("select * from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 1
            sql.rows("select * from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 4
            sql.eachRow("select * from ratecard where bankId = ${theBankId} and programId = ${progId}")
            { row -> \
                "Fido" == row.NAME
            }
            sql.eachRow("select * from ics_rt_crd_tier where ICS_RT_CRD_TIER_ID = ${startId}")
            { row -> \
                "I am the new one"  == row.MOD_USR_ID
                2000000 == row.BALANCE_THRESHHOLD
            }

        cleanup:
            sql.execute("delete from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId)
            sql.execute("delete from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId)
    }

    def "do a remove then merge of a rate card w tiers where there was a tier delete then add; this will clear tiers prior to merge"()
    {
        def theBankId = -678
        def progId =  57
        def startId = -343
        def rc = new RateCard()
        rc.bankId = theBankId
        rc.programId = 57
        rc.name = "Hector"

        4.times{
            def tier = new ICSRateCardTier();
            tier.id = startId
            tier.bankId = theBankId
            tier.programId = progId
            tier.balanceThreshhold =  (1000000 * (it + 1))
            tier.rateType = 1
            tier.spread = 1
            tier.fixedRate = 1
            tier.createDate = LocalDateTime.now()
            tier.updateDate = LocalDateTime.now()
            tier.modUserId = "frankie ${it}"
            startId += -1
            rc.addTier(tier)
        }
        em.getTransaction().begin()
        em.persist(rc)
        em.detach(rc)
        em.getTransaction().commit()

        when:
            def removedSomething = rc.tiers.removeAll{ it.balanceThreshhold == 2000000 }

        then:
            removedSomething
            rc.tiers.size() == 3

        when:
            startId += -1
            rc.name = "Fido"
            def newTier = new ICSRateCardTier();
            newTier.id = startId
            newTier.bankId = theBankId
            newTier.programId = progId
            newTier.balanceThreshhold =  2000000
            newTier.rateType = 3
            newTier.spread = 3
            newTier.fixedRate = 3
            newTier.createDate = LocalDateTime.now()
            newTier.updateDate = LocalDateTime.now()
            newTier.modUserId = "I am the new one"
            rc.addTier(newTier)
            em.getTransaction().begin()
            def q = em.createNamedQuery("removeTiers");
            q.setParameter("bankId", theBankId)
            q.setParameter("programId", progId)
            def updateCount = q.executeUpdate()
            em.flush()
        then:
            4 == updateCount
            sql.rows("select * from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 0

        when:
            em.merge(rc)
            em.getTransaction().commit()

        then:
            sql.rows("select * from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 1
            sql.rows("select * from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 4
            sql.eachRow("select * from ratecard where bankId = ${theBankId} and programId = ${progId}")
            { row ->
                "Fido" == row.NAME
            }
            sql.eachRow("select * from ics_rt_crd_tier where ICS_RT_CRD_TIER_ID = ${startId}")
            { row ->
                "I am the new one"  == row.MOD_USR_ID
                2000000 == row.BALANCE_THRESHHOLD
            }

        cleanup:
            sql.execute("delete from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId)
            sql.execute("delete from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId)
    }
    
    def "use no cascade; do a remove then merge of a rate card w tiers where there was a tier delete then add; this will clear tiers prior to merge"()
    {
        def theBankId = -678
        def progId =  57
        def startId = -343
        sql.execute("insert into ratecard(bankid,programid,name)\n\
                     values (${theBankId},${progId},'Hector')".toString())

        4.times{
            sql.execute("insert into ICS_RT_CRD_TIER \n\
                            (ICS_RT_CRD_TIER_ID,\n\
                             BNK_ID,\n\
                             PRGM_ID, \n\
                             BALANCE_THRESHHOLD,\n\
                             MOD_USR_ID) \n\
            VALUES( ${startId},\n\
                    ${theBankId},\n\
                    ${progId},\n\
                    ${(1000000 * (it + 1))},\n\
                    'frankie ${it}')".toString())
            startId += -1
        }

        when:
            em.getTransaction().begin()
            def q = em.createNamedQuery("findByBankIdAndProgramId")
            q.setParameter("bankId", theBankId)
            q.setParameter("programId", progId)
            def rc = q.getSingleResult()
            em.getTransaction().commit()

        then:
            rc
            "Hector" == rc.name
            rc.tiers.size() == 4

        when:
            def removedSomething = rc.tiers.removeAll{ it.balanceThreshhold == 2000000 }

        then:
            removedSomething
            rc.tiers.size() == 3

        when:
            em.detach(rc)
            startId += -1
            rc.name = "Fido"
            def newTier = new ICSRateCardTier();
            newTier.id = startId
            newTier.bankId = theBankId
            newTier.programId = progId
            newTier.balanceThreshhold =  2000000
            newTier.rateType = 3
            newTier.spread = 3
            newTier.fixedRate = 3
            newTier.createDate = LocalDateTime.now()
            newTier.updateDate = LocalDateTime.now()
            newTier.modUserId = "I am the new one"
            rc.addTier(newTier)

        then:
            "Fido" == rc.name
            4 == rc.tiers.size()

        when:
            em.getTransaction().begin()
            q = em.createNamedQuery("removeTiers")
            q.setParameter("bankId", theBankId)
            q.setParameter("programId", progId)
            def removed = q.executeUpdate()

        then:
            4 == removed
            sql.rows("select * from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:theBankId, pid:progId).size() == 0
            //em.merge(rc)
            em.getTransaction().commit()

        then:
            true

        cleanup:
            sql.execute("delete from ratecard where bankId = ?.bid and programId = ?.pid", bid:theBankId, pid:progId)
            sql.execute("delete from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:theBankId, pid:progId)
    }
    
    
    
    @IgnoreRest
    def "manual relationship -- do a remove then merge of a rate card w tiers where there was a tier delete then add; this will clear tiers prior to merge"()
    {
        def theBankId = -678
        def progId =  57
        def startId = -343
        sql.execute("insert into ratecard(bankid,programid,name)\n\
                     values (${theBankId},${progId},'Hector')".toString())

        4.times{
            sql.execute("insert into ICS_RT_CRD_TIER \n\
                            (ICS_RT_CRD_TIER_ID,\n\
                             BNK_ID,\n\
                             PRGM_ID, \n\
                             BALANCE_THRESHHOLD,\n\
                             MOD_USR_ID) \n\
            VALUES( ${startId},\n\
                    ${theBankId},\n\
                    ${progId},\n\
                    ${(1000000 * (it + 1))},\n\
                    'frankie ${it}')".toString())
            startId += -1
        }

        when:
            em.getTransaction().begin()
            def q = em.createNamedQuery("findByBankIdAndProgramId")
            q.setParameter("bankId", theBankId)
            q.setParameter("programId", progId)
            def rc = q.getSingleResult()
            q = em.createNamedQuery("ICSRateCardTier.findByBankIdAndProgramId")
            q.setParameter("bankId", theBankId)
            q.setParameter("programId", progId)
            rc.tiers = q.getResultList()
            em.getTransaction().commit()

        then:
            rc
            "Hector" == rc.name
            rc.tiers != null
            4 == rc.tiers.size()

        when:
            def removedSomething = rc.tiers.removeAll{ it.balanceThreshhold == 2000000 }

        then:
            removedSomething
            rc.tiers.size() == 3
                    
        when:
            startId += -1
            rc.name = "Fido"
            def newTier = new ICSRateCardTier();
            newTier.id = startId
            newTier.bankId = theBankId
            newTier.programId = progId
            newTier.balanceThreshhold =  2000000
            newTier.rateType = 3
            newTier.spread = 3
            newTier.fixedRate = 3
            newTier.createDate = LocalDateTime.now()
            newTier.updateDate = LocalDateTime.now()
            newTier.modUserId = "I am the new one"
            rc.addTier(newTier)        
        
        then: 
            "Fido" == rc.name
            4 == rc.tiers.size()
            
        when: 
            em.getTransaction().begin()           
            q = em.createNamedQuery("removeTiers")
            q.setParameter("bankId", theBankId)
            q.setParameter("programId", progId)
            def removed = q.executeUpdate()
                
        then: 
            4 == removed
            
        when: 
            em.merge(rc)
            rc.tiers.each{ t-> 
                em.detach(t) 
                em.persist(t)
            }                                
            em.getTransaction().commit()
        
        then: 
            sql.rows("select * from ratecard where bankId = ?.bid and programId = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 1
            sql.rows("select * from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:rc.bankId, pid:rc.programId).size() == 4
            sql.eachRow("select * from ratecard where bankId = ${theBankId} and programId = ${progId}")
            { row -> 
                "Fido" == row.NAME
            }
            sql.eachRow("select * from ics_rt_crd_tier where ICS_RT_CRD_TIER_ID = ${startId}")
            { row -> 
                "I am the new one"  == row.MOD_USR_ID
                2000000 == row.BALANCE_THRESHHOLD
            }        
        
        
        cleanup:
        true
//            sql.execute("delete from ratecard where bankId = ?.bid and programId = ?.pid", bid:theBankId, pid:progId)
//            sql.execute("delete from ics_rt_crd_tier where bnk_Id = ?.bid and prgm_id = ?.pid", bid:theBankId, pid:progId)
    }    
    
    
}
