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
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;


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
            List<Bank> theBanks = getAllBanks();
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig conf = new IndexWriterConfig(analyzer);
            IndexWriter iWriter = new IndexWriter(directory, conf);

            for(Bank b : theBanks)
            {
                Document doc = new Document();
                doc.add(new IntPoint("id",b.getId()));
                doc.add(new StringField("name", b.getName(), Field.Store.YES));
                doc.add(new StringField("aba", b.getAbaNum(), Field.Store.YES));
                doc.add(new StringField("ein", b.getEin(), Field.Store.YES));
                iWriter.addDocument(doc);
            }
            iWriter.commit();
            iWriter.close();
            IndexReader reader = DirectoryReader.open(directory);
            searcher = new IndexSearcher(reader);
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
            Query query = QueryParser.parse(aba, "aba", new StandardAnalyzer());
            Hits hits = searcher.search(query);

        int hitCount = hits.length();
        if (hitCount == 0) {
            System.out.println(
                "No matches were found for \"" + queryString + "\"");
        }
        else {
            System.out.println("Hits for \"" +
                queryString + "\" were found in quotes by:");

            // Iterate over the Documents in the Hits object
            for (int i = 0; i < hitCount; i++) {
                Document doc = hits.doc(i);

                // Print the value that we stored in the "title" field. Note
                // that this Field was not indexed, but (unlike the
                // "contents" field) was stored verbatim and can be
                // retrieved.
                System.out.println("  " + (i + 1) + ". " + doc.get("title"));
            }
        }
        System.out.println();
    }
    }

}
