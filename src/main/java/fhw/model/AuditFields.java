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
        return(updateDate);
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
        //setUpdateDate(updateDate);
    }

    protected void setModUsr(String s)
    {
        //setModifyUser(modifyUser);
    }

    protected void setProcFlg(Character c)
    {
        //setProcessedFlag(processedFlag);
    }
}
