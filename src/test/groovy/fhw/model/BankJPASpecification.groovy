package fhw.model
import spock.lang.*
import javax.persistence.*
import javax.validation.*
import javax.validation.groups.*
import java.sql.*
import groovy.sql.*
import java.time.*

class BankJPASpecification
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

    def "simple fetch of all banks"()
    {
        when:
            em.getTransaction().begin()
            def q = em.createQuery('select b from Bank b')
            def l = q.getResultList()
            em.getTransaction().commit()

        then:
            l
            l.size() > 0
            println "number 0f banks ${l.size()}"
    }


}
