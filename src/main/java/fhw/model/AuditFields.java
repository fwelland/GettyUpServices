package fhw.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditFields
{    
    private Date createDate;
    private Date updateDate;
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
    
    public void setCrDt(Date d) { }
    public void setUpDt(Date d) {}
    public void setModUsr(String s) {}    
    public void setProcFlg(Character c) {}        
}
