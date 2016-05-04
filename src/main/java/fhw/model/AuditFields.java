package fhw.model;

import java.sql.Timestamp;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditFields
{
    private Date createDate;
    private Timestamp updateDate;
    private String modifyUser;
    private Character processedFlag;

    public Date getCrDt()
    {
        return(createDate);
    }

    public Date getUpDt()
    {
        Date d = null; 
        if(null != updateDate)
        {
            d = new Date(updateDate.getTime()); 
        }
        return(d);
    }

    public String getModUsr()
    {
        return(modifyUser);
    }

    public Character getProcFlg()
    {
        return(processedFlag);
    }

    protected void setCrDt(Date d)
    {
        //setCreateDate(createDate);
    }

    protected void setUpDt(Date d)
    {
        
    }

    protected void setModUsr(String s)
    {
        
    }

    protected void setProcFlg(Character c)
    {
        
    }
}
