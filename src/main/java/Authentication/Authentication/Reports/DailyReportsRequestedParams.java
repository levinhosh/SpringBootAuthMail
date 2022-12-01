package Authentication.Authentication.Reports;


import Authentication.Authentication.AppUser.AppUser;
import Authentication.Authentication.Reports.Enums.ClientNameEnum;
import Authentication.Authentication.Reports.Enums.DepartmentEnum;
import Authentication.Authentication.Reports.Enums.ProductNameEnum;
import Authentication.Authentication.Reports.Enums.ReportCategory;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.sql.Date;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DailyReportsRequestedParams {

    private final String firstName;
    DepartmentEnum departmentEnum;
    private Date creationDate;
        ReportCategory reportCategory;
    private final String ticketId;
    private final String timeTaken;
        ClientNameEnum clientNameEnum;
        ProductNameEnum productNameEnum;
    private final String report_description;
        AppUser appUser;


}
