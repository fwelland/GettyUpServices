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
}

