package Authentication.Authentication.Reports;

import Authentication.Authentication.AppUser.AppUser;
import Authentication.Authentication.Reports.Enums.ClientNameEnum;
import Authentication.Authentication.Reports.Enums.DepartmentEnum;
import Authentication.Authentication.Reports.Enums.ProductNameEnum;
import Authentication.Authentication.Reports.Enums.ReportCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;

/*
Example of POST i.e INSERT INTO `daily_reports` (`client_name`, `created_at`,
 `department`, `product_name`, `category`, `description`, `ticket_id`, //
 `time_taken`, `app_user_id`) VALUES ('POST_BANK', '2022-11-29', 'ACCOUNTING',
 'PRODUCT_A', 'AAAAAA', 'Another update.', NULL, '0500hrs', '8');


Example of UPDATE i.e UPDATE `daily_reports` SET
`client_name` = 'NORTHWEST', `created_at` = '2022-11-30',
`department` = 'MANAGEMENT', `product_name` = 'PRODUCT_B',
`category` = 'CCCCCC', `ticket_id` = 'AS5ER',
`time_taken` = '1200hrs' WHERE `daily_reports`.`daily_report_id` = 0
;


Example of READ i.e SELECT `client_name`,`created_at`,`department`,
`product_name`,`category`,`description`,`ticket_id`,`time_taken`
FROM daily_reports RIGHT JOIN
app_user ON daily_reports.app_user_id = app_user.id;


SELECT client_name,created_at,department,
 product_name,category,description,ticket_id,
time_taken FROM daily_reports WHERE daily_reports.app_user_id = 8
 */

@Getter
@Setter
@DynamicUpdate
@Entity
@Table(name="daily_reports")
@NoArgsConstructor
@AllArgsConstructor
public class DailyReports {


    @SequenceGenerator(
            name = "daily_report_sequence",
            sequenceName = "daily_report_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "daily_report_sequence"
    )


    @Column(name = "daily_report_id")
    private Long id;



    @Column(name = "created_at", nullable = false, insertable = false, updatable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date creationDate;

    @Column(name = "department", nullable = false)
    @Enumerated(EnumType.STRING)
    private DepartmentEnum departmentEnum;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportCategory reportCategory;

    @Column(name = "ticketId", nullable = true)
    private String ticketId;

    @Column(name = "timeTaken", nullable = false)
    private String timeTaken;


    @Column(name = "client_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private ClientNameEnum clientNameEnum;

    @Column(name = "product_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductNameEnum productNameEnum;

    @Column(name = "description", updatable=true, nullable = false)
    private String report_description;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

}
