
package fhw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Contact
        extends AuditFields
{
    private Long id;
    private Long customerId;
    private String name;
    private String firstName;
    private String middleName;
    private String lastName;
    private Integer type;

}
