package tigertalk.service._implementation.Authentication;
import tigertalk.model.User.UserProfile;
import tigertalk.repository.User.UserProfileRepository;
import tigertalk.service.Authentication.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class SignUpServiceImpl implements SignUpService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public Optional<String> signupUserProfile(UserProfile userProfile) {
        Optional<String> validationError = UserProfile.verifyBasics(userProfile, userProfileRepository, true);
        if (validationError.isPresent()) {
            return validationError;
        }
        userProfileRepository.save(userProfile);
        return Optional.empty();
    }

}
