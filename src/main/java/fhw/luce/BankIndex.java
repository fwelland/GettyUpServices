package fhw.luce;

import fhw.model.Bank;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;


public class BankIndex
{
    private EntityManagerFactory emf;
    private EntityManager em;
    private RAMDirectory directory;
    private IndexSearcher searcher;


    public BankIndex()
    {
        emf = Persistence.createEntityManagerFactory("ProdPU");
        em = emf.createEntityManager();
        directory = new RAMDirectory();
    }


    protected List<Bank> getAllBanks()
    {
        em.getTransaction().begin();
        javax.persistence.Query q = em.createQuery("select b from Bank b");
        List<Bank> l = q.getResultList();
        em.getTransaction().commit();
        return(l);
    }

    protected void createIndex()
    {
        try
        {
            long beg = System.currentTimeMillis(); 
            List<Bank> theBanks = getAllBanks();
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig conf = new IndexWriterConfig(analyzer);
            IndexWriter iWriter = new IndexWriter(directory, conf);

            for(Bank b : theBanks)
            {
                Document doc = new Document();
                doc.add(new TextField("id",b.getId().toString(), Field.Store.YES));
                doc.add(new TextField("name", b.getName(), Field.Store.YES));
                doc.add(new TextField("aba", b.getAbaNum(), Field.Store.YES));
                String v = b.getEin(); 
                if(null != v )
                {
                    doc.add(new StringField("ein", v, Field.Store.YES));
                }
                iWriter.addDocument(doc);
            }
            iWriter.commit();
            iWriter.close();
            IndexReader reader = DirectoryReader.open(directory);
            searcher = new IndexSearcher(reader);
            long end = System.currentTimeMillis(); 
            System.out.println("load and indexing time:  " + (end - beg) + " ms");           
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void abaSearch(String aba)
    {
        try
        {
            long beg = System.currentTimeMillis(); 
            QueryParser parser = new QueryParser("aba", new StandardAnalyzer());                        
            Query q = parser.parse(aba); 
            TopDocs td = searcher.search(q,20);             
            long end = System.currentTimeMillis(); 
            System.out.println("The number of hits is:   " + td.totalHits);             
            for(ScoreDoc sd : td.scoreDocs)
            {
                Document doc = searcher.doc(sd.doc);
                for(IndexableField f : doc.getFields())
                {
                    System.out.println("The field name:  " +  f.name()); 
                }
                System.out.println("And the document is:  bankid: " + doc.get("id")  + "; name:  " + doc.get("name"));  
            }
            System.out.println("search time:  " + (end - beg) + " ms");           
        }
        catch(Exception e)
        {
            e.printStackTrace(); 
        }
    }
        
    public void findBanksContaining(String searchVal)
    {
        try
        {
            long beg = System.currentTimeMillis(); 
            QueryParser parser = new QueryParser("id", new StandardAnalyzer());    
            parser.setAllowLeadingWildcard(true);
            String s = String.format("(*%s*) OR name:(*%s*) OR aba:(*%s*)", searchVal,searchVal, searchVal); 
            Query q = parser.parse(s);            
            TopDocs td = searcher.search(q,10);             
            long end = System.currentTimeMillis();             
            System.out.println("The number of hits is:   " + td.totalHits);
            System.out.println("The first 10:");
            for(ScoreDoc sd : td.scoreDocs)
            {
                Document doc = searcher.doc(sd.doc);
                System.out.println("\tdocument is:  bankid: " + doc.get("id")  + "; name:  " + doc.get("name") + "; aba:  " + doc.get("aba"));  
            }
            System.out.println("search time:  " + (end - beg) + " ms");                       
        }
        catch(Exception e)
        {
            e.printStackTrace(); 
        }
    }    
}
