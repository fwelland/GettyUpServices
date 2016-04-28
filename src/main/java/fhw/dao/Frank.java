package fhw.dao;

import fhw.model.Contact;
import fhw.model.Customer;
import fhw.model.CustomerContact;
import fhw.model.CustomerContactPk;
import java.util.Date;
import java.util.List;
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

    private static void customerContact(EntityManager em)
    {
        CustomerContactPk ccpk = new CustomerContactPk();
        ccpk.setCustomerId(new Long("5000124445"));
        ccpk.setContactId(new Long("16134"));
        CustomerContact cc = em.find(CustomerContact.class, ccpk);
        System.out.println(cc);
    }

//    private static void customer(EntityManager em)
//    {
//        Query q = em.createNamedQuery("Customer.findById");
//        q.setParameter(1, new Long("5000035841"));
//        Customer c = (Customer) q.getSingleResult();
//        System.out.println(c);
//    }

    private static void customerFind(EntityManager em)
    {
        Customer c = em.find(Customer.class, new Long("5000035841"));
        System.out.println(c);
        for(CustomerContact cc: c.getCustomerContacts())
        {
            System.out.println(cc);
        }
    }

    private static void contactFind(EntityManager em)
    {
        Contact c = em.find(Contact.class, new Long(7905));
        System.out.println(c);
    }

    private static void contact(EntityManager em)
    {
        Query q = em.createNamedQuery("Contact.findById");
        q.setParameter(1, new Long("7905"));
        Contact c = (Contact) q.getSingleResult();
        System.out.println(c);
    }
    
    private static void contactSave(EntityManager em)
    {
        em.getTransaction().begin();
        Contact c = new Contact(); 
        Date d = new Date(); 
        c.setName("Matt Welland");
        c.setFirstName("Fred");
        c.setLastName("Welland");
        c.setId(-8945L);        
        c.setCustomerId(5000041027L);                
        c.setMiddleName("H");
        c.setProcessedFlag('S');        
        c.setCreateDate(d);       
        c.setUpdateDate(d);
        c.setModifyUser("fhw");        
        c.setType(1);
        em.persist(c);        
        em.getTransaction().commit();
    }
    
    private static void contactRemove(EntityManager em)
    {
        em.getTransaction().begin();
        Contact c = em.find(Contact.class, -8945L); 
        em.remove(c);
        em.getTransaction().commit();
    }    
    
    private static void contactUpdate(EntityManager em)
    {
        em.getTransaction().begin();
        Contact c = em.find(Contact.class, -8945L); 
        c.setName("Matt Ratt");
        c.setType(6);
        c.setUpdateDate(new Date());
        c.setModifyUser("AMF");
        em.merge(c); 
        em.getTransaction().commit();        
    }
}
