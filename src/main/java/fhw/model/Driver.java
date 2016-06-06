package fhw.model;

import java.util.Map;

public abstract class Driver
{
    public static void main(String args[])
    {
        throw new IllegalStateException("attempted to start abstract driver");
    }

    public final void run(String args[])
    {
        int rc = 0;
        try
        {
            rc = go();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            rc = 1;
        }
        System.exit(rc);
    }

    public abstract int go();
}
