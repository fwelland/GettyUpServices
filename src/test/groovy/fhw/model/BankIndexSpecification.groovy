package fhw.model

import spock.lang.*
import javax.persistence.*
import javax.validation.*
import javax.validation.groups.*
import java.sql.*
import fhw.luce.BankIndex
import groovy.sql.*
import java.time.*

class BankIndexSpecification
    extends Specification
{

    def BankIndex bi

    def setup()
    {
        bi = new BankIndex()
    }

    def cleanup()
    {

    }

    def "simple fetch of all banks"()
    {
        when:
            def l = bi.getAllBanks()

        then:
            l
            l.size() > 0
            println "number 0f banks ${l.size()}"
    }


    def "so make an index"()
    {
        when:
            bi.createIndex()

        then:
            true
    }
    
    def "so make an index and search for an aba"()
    {
        given:
            bi.createIndex()
            
        when:
            bi.abaSearch("211173357")

        then:
            true
    }    
    
    
    @IgnoreRest
    def "so make an index and search over several fields"(String searchVal)
    {
        given:
            bi.createIndex()
            
        when:
            bi.findBanksContaining(searchVal)

        then:
            true
            
        where: 
                searchVal   | _
                "Castle"    | _
                "473"       | _
                "301"       | _
                "Bank"      | _
    }        
}
