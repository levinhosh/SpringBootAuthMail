package Authentication.Authentication.Registration;

import Authentication.Authentication.Reports.DailyReports;
import Authentication.Authentication.Reports.DailyReportsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import Authentication.Authentication.AppUser.AppUser;
import Authentication.Authentication.AppUser.AppUserService;
import Authentication.Authentication.Registration.token.ConfirmationTokenService;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class RegistrationController {

    private final ConfirmationTokenService confirmationTokenService;
    private final RegistrationService registrationService;
    private final AppUserService appUserService;


    @Autowired
    private DailyReportsService dailyReportsService;

    //public ResponseEntity

    @PostMapping(path = "/employee/add") //Create
    public String register(@RequestBody RegistrationRequest request) {
        registrationService.register(request);
	return "Account registered Successfully, Activation mail has been sent to User's Email Account";
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }


    @GetMapping(path = "employee/all") //Read all
    public List<?> Employees()
    {
        return registrationService.getEmployees();
    }

    @GetMapping(path = "/report/all") //Read all
    public List<DailyReports> allReports()
    {
        return dailyReportsService.getAllReports();
    }

    @RequestMapping(value = "find/{id}", method = RequestMethod.GET) //Read by ID
	public @ResponseBody Optional<Optional<?>> getUserById(@PathVariable Long id) {
        Optional<?> appUser = registrationService.findByID(id);
		return Optional.ofNullable(appUser);
        
	}

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.DELETE) //Delete
	public String deletePersnonel(@PathVariable Long id) {
		registrationService.deleteEmployee(id);
		return "Deleted Successfully";
	}


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE) //Delete
	public HttpStatus deleteTokenUsed(@PathVariable Long id) {
		//confirmationTokenService.deleteTokenById(confirmationToken);
        appUserService.deleteUserById(id);
		return HttpStatus.NO_CONTENT;
	}



//    @RequestMapping(value="/employee/{id}", method = RequestMethod.PUT)  // Update by id
//    public HttpStatus updateEmployee(@PathVariable Long id, @RequestBody AppUser appUser) {
//
//        return registrationService.updateEmp(appUser) != null ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST;
//
//    }


    @RequestMapping(value="/update/{id}", method = RequestMethod.PUT)  // Update by id
    public String updateEmployeek(@PathVariable("id") Long id, @RequestBody AppUser appUser) {

        registrationService.updateUser(id,appUser);

        return "Updated Successfully";

    }

    @PostMapping(value="/create/report/{appUser}")
    public DailyReports createReport(@RequestBody DailyReports dailyReports, @PathVariable Long appUser) {
        try {
            return dailyReportsService.createReport(dailyReports, appUser);
        }catch (Exception e){
            log.info("Error{} "+e);
            return null;
        }
    }

    @GetMapping(value = "/report/{id}") //Read by single user records
    public @ResponseBody Optional<List<DailyReports>> getUserByReport(@PathVariable Long id) {
        List<DailyReports> dailyReports = dailyReportsService.readReport(id);
        return Optional.ofNullable(dailyReports);

    }

    @RequestMapping(value="/updateReport/{id}", method = RequestMethod.PUT)  // Update by id
    public DailyReports updateDailyReports(@PathVariable("id") Long id, @RequestBody DailyReports dailyReports) {

        return (dailyReportsService.updateUserReport(id,dailyReports));

    }




}