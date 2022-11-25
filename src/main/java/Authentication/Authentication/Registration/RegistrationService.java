package Authentication.Authentication.Registration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import Authentication.Authentication.AppUser.AppUser;
import Authentication.Authentication.AppUser.AppUserRepository;
import Authentication.Authentication.AppUser.AppUserRole;
import Authentication.Authentication.AppUser.AppUserService;
import Authentication.Authentication.Registration.token.ConfirmationToken;
import Authentication.Authentication.Registration.token.ConfirmationTokenRepository;
import Authentication.Authentication.Registration.token.ConfirmationTokenService;
import Authentication.Authentication.email.EmailSender;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegistrationService {

      
        @Autowired
    private final AppUserRepository appUserRepository;
        private final BCryptPasswordEncoder bCryptPasswordEncoder; //using BCrypt for 
    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

// Change

       



    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.
                test(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }

        String token = appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
			request.getDesignation(),
                        AppUserRole.USER

                )
        );

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailSender.send(
                request.getEmail(),
                buildEmail(request.getFirstName(), link));

        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

        public Optional<AppUser> findByID(Long empid){
                return appUserRepository.findById(empid);
        }

        public List<AppUser> getEmployees(){
                return (List<AppUser>) appUserRepository.findAll();
        }

        public void deleteEmployee(Long empid) {
		appUserRepository.deleteById(empid);
	}

        public void deleteAllDetails(AppUser savedAppUser){
                // confirmationTokenService.deleteTokenById(confirmationToken);
                // appUserService.deleteUserById(savedAppUser);

                // Optional<AppUser> appUser = appUserRepository.findById(savedAppUser.getId());
                // Optional<ConfirmationToken> cT = confirmationTokenRepository.findById(confirmationToken.getId());

                // if(appUser.isPresent()){
        
                //         AppUser UpdateUser = appUser.get();
                //         ConfirmationToken cToken = cT.get();
                        
                        
                //         confirmationTokenRepository.deleteById(cToken.getId());
                //         appUserRepository.deleteById(UpdateUser.getId());
                
                
                // 
        
        }



        public AppUser updateEmp(AppUser savedAppUser) {

                Optional<AppUser> appUser = appUserRepository.findById(savedAppUser.getId());
                if(appUser.isPresent()){
        
                        AppUser UpdateUser = appUser.get();
                        UpdateUser.setAppUserRole(savedAppUser.getAppUserRole());
                        UpdateUser.setDesignation(savedAppUser.getDesignation());


                        String encodedPassword = bCryptPasswordEncoder
                        .encode(savedAppUser.getPassword());

                        


                        UpdateUser.setPassword(encodedPassword);
                        
                        AppUser newUser = appUserRepository.save(UpdateUser);
                        return newUser;
               }
               else{
                        return null;
               }
        }
}