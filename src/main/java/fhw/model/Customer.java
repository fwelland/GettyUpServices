package fhw.model;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Customer
{
    private Long customerId;
    private String relationshipBankCustomerId;
    private Integer branchId;
    private Integer bankId;
    private String bankBranchId;
    private Character classId;
    private String mailName;
    private String organizationName;
    private Integer residenceLocationId;
    private Integer citizenshipLocationId;
    private String taxId;
    private Character taxIdType;
    private Date taxCompletionDate;
    private Character taxCompletionStatusType;
    private Character cNoticeType;
    private Date cNoticeDate;
    private String otherId;
    private Date birthDate;
    private Date deathDate;
    private String mothersMaidenName;
    private String referredBy;
    private Character bankEmployeeIndicator;
    private String employeeId;
    private Character employeeStatus;
    private String primaryBankOfficeContact;
    private String secondaryBankOfficeContact;
    private Date lastCustomerContactDate;
    private Date createDate;
    private Date updateDate;
    private String lastUserToModify;
    private Character processedFlag;
    private Character otherIdType;
    private Character customerStatus;
    private Character citizenshipCodeId;
    private String unifiedTaxId;
    private List<Contact> contacts;
    private List<CustomerContact> customerContacts; 
}
