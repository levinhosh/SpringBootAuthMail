package Authentication.Authentication.Reports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DailyReportsRepository extends JpaRepository<DailyReports, Long> {

    @Query(value = "SELECT  * FROM daily_reports WHERE app_user_id=:appUserId", nativeQuery = true)
    List<DailyReports> findByUniqueConstraint(@Param("appUserId") Long appUserId);



    //DailyReports updateReport(Long appUserId,DailyReports dailyReports);


}
