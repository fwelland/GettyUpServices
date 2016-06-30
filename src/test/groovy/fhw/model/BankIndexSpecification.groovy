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
}
