package fhw.model;

public  class DriverOne
        extends Driver
{

    public static void main(String args[])
    {
        new DriverOne().run(args);
    }

    @Override
    public int go()
    {
       System.out.println("gone!");
       return(0);
    }


}
