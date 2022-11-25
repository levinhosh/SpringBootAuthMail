package Authentication.Authentication.Registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
    public boolean deleteTokenById(ConfirmationToken confirmationToken){
        
        Optional<ConfirmationToken> cToken = confirmationTokenRepository.findById(confirmationToken.getId());
       
        if (cToken.isPresent())
        {

            confirmationTokenRepository.deleteById(cToken.get().getId());
            
            return true;

        }


        return false;
    }
}