package fhw.dao;

import fhw.model.CustomerContact;
import fhw.model.Customer;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Frank
{

    public static void main(String[] args)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProdPU");
        EntityManager em = emf.createEntityManager();
//        customerFind(em);
//        customerContact(em);
//        customer(em);
//        contactFind(em);
        //contactSave(em);
        //contactRemove(em);
        contactUpdate(em);
    }


    private static void contactFind(EntityManager em)
    {
        CustomerContact c = em.find(CustomerContact.class, new Long(7905));
        System.out.println(c);
    }

    private static void contact(EntityManager em)
    {
        Query q = em.createNamedQuery("Contact.findById");
        q.setParameter(1, new Long("7905"));
        CustomerContact c = (CustomerContact) q.getSingleResult();
        System.out.println(c);
    }

    private static void contactSave(EntityManager em)
    {
        em.getTransaction().begin();
        CustomerContact c = new CustomerContact();
        Date d = new Date();
        c.setName("Matt Welland");
        c.setFirstName("Fred");
        c.setLastName("Welland");
        c.setId(-8945L);
        c.setCustomerId(5000041027L);
        c.setMiddleName("H");
        c.setProcessedFlag('S');
        c.setCreateDate(d);
        c.setModifyUser("fhw");
        c.setType(1);
        em.persist(c);
        em.getTransaction().commit();
    }

    private static void contactRemove(EntityManager em)
    {
        em.getTransaction().begin();
        CustomerContact c = em.find(CustomerContact.class, -8945L);
        em.remove(c);
        em.getTransaction().commit();
    }

    private static void contactUpdate(EntityManager em)
    {
        em.getTransaction().begin();
        CustomerContact c = em.find(CustomerContact.class, -8945L);
        c.setName("Matt Ratt");
        c.setType(6);
        c.setModifyUser("YYY");
        em.merge(c);
        em.getTransaction().commit();
    }
}
