package Authentication.Authentication.Reports;

import Authentication.Authentication.AppUser.AppUser;
import Authentication.Authentication.AppUser.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DailyReportsService  {
    @Autowired
    private DailyReportsRepository dailyReportsRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    private Optional<DailyReports> dailyReports;




    //create report
    public DailyReports createReport(DailyReports dailyReports, Long appUser){
        try{
            Optional<AppUser> appUser1= appUserRepository.findById(appUser);
            if(appUser1.isPresent()){
                AppUser existingAppUser= appUser1.get();
                dailyReports.setAppUser(existingAppUser);
                DailyReports savedDailyReports=dailyReportsRepository.save(dailyReports);
                return savedDailyReports;
            }else {
                return null;
            }

        }catch (Exception e){
            log.info("Error{} "+e);
            return null;
        }



    }



    public List<DailyReports> getAllReports(){
        return (List<DailyReports>) dailyReportsRepository.findAll();
    }


    public List<DailyReports> readReport(Long userId){

        try{

            Optional<AppUser> appUser1= appUserRepository.findById(userId);

           List<DailyReports> dailyReports1 = null;

            if(appUser1.isPresent()){

                dailyReports1 = dailyReportsRepository.findByUniqueConstraint(userId);

                //return (dailyReportsRepository.findByUniqueConstraint(userId));


            }
            return dailyReports1;



        }catch (Exception e){
            log.info("Error{} "+e);
            return null;
        }



    }



    public DailyReports updateUserReport(Long id, DailyReports dr) {
         DailyReports dailyReports = dailyReportsRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("No such record in your Report Diary"));


            dailyReports.setReport_description(dr.getReport_description());
            dailyReports.setReportCategory(dr.getReportCategory());
            dailyReports.setProductNameEnum(dr.getProductNameEnum());
            dailyReports.setTicketId(dr.getTicketId());
            dailyReports.setClientNameEnum(dr.getClientNameEnum());
            dailyReports.setTimeTaken(dr.getTimeTaken());
            dailyReports.setDepartmentEnum(dr.getDepartmentEnum());


            return dailyReportsRepository.save(dailyReports);




    }


}
