package fhw.model;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "customer")
public class CustomerContact
{
    private Long customerId;
    private Long contactId;
    private Long type;
    private Date createDate;
    private Date updateDate;
    private String lastUserToModify;
    private Character processedFlag;

    private Customer customer;
}
