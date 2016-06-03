package fhw.model

import spock.lang.*
import javax.persistence.*
import javax.validation.*
import javax.validation.groups.*
import java.sql.*
import groovy.sql.*
import java.time.*

class ICSRateCardTierSpecificationJPA
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

    @Ignore
    def "simple insert of a rate card tier"()
    {
        def tier = new ICSRateCardTier();
        tier.bankId = -98
        tier.id = -67
        tier.programId = 4
        tier.balanceThreshhold = 1000000
        tier.rateType = 1
        tier.spread = 1
        tier.fixedRate = 1
        tier.createDate = LocalDateTime.now()
        tier.updateDate = LocalDateTime.now()
        tier.modUserId = 'frankie'


        when:
            em.getTransaction().begin()
            em.persist(tier)
            em.getTransaction().commit()

        then:
            sql.rows('select * from ICS_RT_CRD_TIER where ICS_RT_CRD_TIER_ID =?.id ', id:tier.id).size() == 1

        cleanup:
            sql.execute( 'delete from ICS_RT_CRD_TIER where ICS_RT_CRD_TIER_ID =?.id ', id:tier.id )
    }

    @Ignore
    def "Add a Few and Remove a Few Rate Card Tiers"()
    {
        def Integer startId = -72
        def tierList = []
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
            tierList.add(tier)
        }

        when:
            em.getTransaction().begin()
            tierList.each{ t-> em.persist(t) }
            em.getTransaction().commit()

        then:
            def ids = tierList.collect {  "(${it.id})" }.join(',')
            sql.rows("select ICS_RT_CRD_TIER_ID from ICS_RT_CRD_TIER where ICS_RT_CRD_TIER_ID in ( ${ids} )".toString()).size() == 4

        cleanup:
            sql.execute( "delete from ICS_RT_CRD_TIER where ICS_RT_CRD_TIER_ID in ( ${ids} )".toString() )
    }

    def "Add a some rate tiers to DB, load them, change one in memory then merge that individual one"()
    {
        def Integer startId = -72
        def bId = -98
        def tierList = []
        4.times{
            def tier = new ICSRateCardTier();
            tier.id = startId
            tier.bankId = bId
            tier.programId = 4
            tier.balanceThreshhold =  (1000000 * (it + 1))
            tier.rateType = 1
            tier.spread = 1
            tier.fixedRate = 1
            tier.createDate = LocalDateTime.now()
            tier.updateDate = LocalDateTime.now()
            tier.modUserId = "frankie ${it}"
            startId += -1
            tierList.add(tier)
        }
        def ids = tierList.collect {  "(${it.id})" }.join(',')
        ids = ids + ',-80'
        em.getTransaction().begin()
        tierList.each{ t-> em.persist(t) }
        em.getTransaction().commit()

        when:
            def query = em.createQuery("SELECT t FROM ICSRateCardTier t where t.bankId = ?1", ICSRateCardTier.class);
            query.setParameter(1, bId);
            def List tiers = query.getResultList();

        then:
            tiers.size() == 4

        when:
            def rex = tiers.find{ it.balanceThreshhold == 2000000 }


        then:
            rex

        when:
            rex.rateType = 1
            rex.spread = 1
            rex.fixedRate = 1
            rex.createDate = LocalDateTime.now()
            rex.updateDate = LocalDateTime.now()
            rex.modUserId = "Oh No Mr. Bill"

        then:
            em.getTransaction().begin()
            em.merge(rex)
            em.getTransaction().commit()

        then:
            sql.eachRow("select * from ICS_RT_CRD_TIER WHERE ICS_RT_CRD_TIER_ID = ${rex.id}")
            {
                row -> "Oh No Mr. Bill" == row.MOD_USR_ID
            }

        cleanup:
            sql.execute( "delete from ICS_RT_CRD_TIER where ICS_RT_CRD_TIER_ID in ( ${ids} )".toString() )
    }
}

