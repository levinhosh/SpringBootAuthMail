package Authentication.Authentication.AppUser;

import Authentication.Authentication.Registration.token.ConfirmationToken;
import Authentication.Authentication.Registration.token.ConfirmationTokenRepository;
import Authentication.Authentication.Registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {


@Autowired
private final ConfirmationTokenRepository confirmationTokenRepository;

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder; //using BCrypt for 
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();

        if (userExists) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.

            throw new IllegalStateException("email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder
                .encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser); //save credentials first then send confirmation token

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

//        TODO: SEND EMAIL

        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public boolean deleteUserById(Long id){
        
        //Optional<AppUser> cToken = appUserRepository.findById(appUser.getId());
        Optional<AppUser> cToken = appUserRepository.findById(id);
        //Optional<ConfirmationToken> cT = confirmationTokenRepository.findById(appUser.getId());
        Optional<ConfirmationToken> cT = confirmationTokenRepository.findById(id);
        if (cToken.isPresent())
        {
                confirmationTokenRepository.deleteById(cT.get().getId());
                appUserRepository.deleteById(cToken.get().getId());
            
            return true;

        }


        return false;
    }
 

}