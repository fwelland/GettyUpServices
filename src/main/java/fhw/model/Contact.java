
package fhw.model;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Contact
{
    private Long id;
    private String name;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date createDate;
    private Date updateDate;
    private String lastUserToModify;
    private Character processedFlag;
    private Integer type;
}
