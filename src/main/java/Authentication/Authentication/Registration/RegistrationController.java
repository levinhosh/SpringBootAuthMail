package Authentication.Authentication.Registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

import Authentication.Authentication.AppUser.AppUser;
import Authentication.Authentication.AppUser.AppUserService;
import Authentication.Authentication.Registration.token.ConfirmationToken;
import Authentication.Authentication.Registration.token.ConfirmationTokenService;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final ConfirmationTokenService confirmationTokenService;
    private final RegistrationService registrationService;
    private final AppUserService appUserService;

    @PostMapping(path = "/registration") //Create
    public String register(@RequestBody RegistrationRequest request) {
        registrationService.register(request);
	return "Account registered Successfully, Activation mail has been sent to User's Email Account";
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }


    @GetMapping(path = "employees") //Read all
    public List<?> Employees()
    {
        return registrationService.getEmployees();
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET) //Read by ID
	public @ResponseBody Optional<Optional<?>> getUserById(@PathVariable Long id) {
        Optional<?> appUser = registrationService.findByID(id);
		return Optional.ofNullable(appUser);
        
	}

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.DELETE) //Delete
	public HttpStatus deletePersnonel(@PathVariable Long id) {
		registrationService.deleteEmployee(id);
		return HttpStatus.NO_CONTENT;
	}


    @RequestMapping(value = "/token", method = RequestMethod.DELETE) //Delete
	public HttpStatus deleteTokenUsed(@RequestBody AppUser appUser) {
		//confirmationTokenService.deleteTokenById(confirmationToken);
        appUserService.deleteUserById(appUser);
		return HttpStatus.NO_CONTENT;
	}



    @RequestMapping(value="/employee/{id}", method = RequestMethod.PUT)  // Update by id
    public HttpStatus updateEmployee(@PathVariable Long id, @RequestBody AppUser appUser) {

        return registrationService.updateEmp(appUser) != null ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST;

    }


}