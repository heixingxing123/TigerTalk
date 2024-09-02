package tigertalk.service._implementation.UserProfile;

import tigertalk.model.User.UserProfile;
import tigertalk.model.User.UserProfileDTO;
import tigertalk.repository.User.UserProfileRepository;
import tigertalk.service.UserProfile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static tigertalk.model.Utils.RegexCheck.NAME_NORM;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public List<UserProfileDTO> getAllUserProfiles() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        List<UserProfileDTO> userProfileDTOs = new ArrayList<>();
        for (UserProfile userProfile : userProfiles) {
            UserProfileDTO dto = userProfile.toDto();
            userProfileDTOs.add(dto);
        }
        return userProfileDTOs;
    }

    @Override
    public Optional<UserProfileDTO> getUserProfileByEmail(String email) {
        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(email);
        if (userProfileOptional.isPresent()) {
            UserProfile userProfile = userProfileOptional.get();
            UserProfileDTO userProfileDTO = userProfile.toDto();
            return Optional.of(userProfileDTO);
        } else {
            return Optional.empty();
        }
    }

    // for admin / user
    @Override
    public void deleteUserProfileByEmail(String email) {
        Optional<UserProfile> userProfile = userProfileRepository.findById(email);
        if (userProfile.isPresent()) {
            userProfileRepository.deleteById(email);
        } else {
            throw new RuntimeException("User profile with email " + email + " not found.");
        }
    }


    @Override
    public Optional<String> updateUserProfile_setCommonInfo(String email, String firstName, String lastName, String userName, String biography, int age, String gender) {
        if (!NAME_NORM.matcher(firstName).matches()) {
            return Optional.of("First name must contain no symbols");
        }
        if (!NAME_NORM.matcher(lastName).matches()) {
            return Optional.of("Last name must contain no symbols");
        }
        if (age <= 0) {
            return Optional.of("Age must be greater than 0");
        }

        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(email);
        if (userProfileOptional.isEmpty()) {
            return Optional.of("User not found");
        }
        UserProfile userProfile = userProfileOptional.get();

        userProfile.setFirstName(firstName);
        userProfile.setLastName(lastName);
        userProfile.setUserName(userName);
        userProfile.setBiography(biography);
        userProfile.setAge(age);
        userProfile.setGender(gender);

        userProfileRepository.save(userProfile);
        return Optional.empty();
    }

    // all: set profile picture
    @Override
    public Optional<String> updateUserProfile_setProfilePicture(String email, String profilePictureUrl) {
        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(email);
        if (userProfileOptional.isEmpty()) {
            return Optional.of("User not found");
        }
        UserProfile userProfile = userProfileOptional.get();
        userProfile.setProfilePictureUrl(profilePictureUrl);
        userProfileRepository.save(userProfile);
        return Optional.empty();
    }

    // all: set online status: "available" / "busy" / "away" / "offline"
    @Override
    public Optional<String> updateUserProfile_setOnlineStatus(String email, String onlineStatus) {
        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(email);
        if (userProfileOptional.isEmpty()) {
            return Optional.of("User not found");
        }
        UserProfile userProfile = userProfileOptional.get();
        userProfile.setOnlineStatus(onlineStatus);
        userProfileRepository.save(userProfile);
        return Optional.empty();
    }



    // admin: set role: "none" / "student" / "instructor" / "employee"
    @Override
    public Optional<String> updateUserProfile_setRole(String email, String role) {
        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(email);
        if (userProfileOptional.isEmpty()) {
            return Optional.of("User not found");
        }
        UserProfile userProfile = userProfileOptional.get();
        userProfile.setRole(role);
        userProfileRepository.save(userProfile);
        return Optional.empty();
    }

    // admin: set validated: true / false
    @Override
    public Optional<String> updateUserProfile_setValidated(String email, boolean validated) {
        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(email);
        if (userProfileOptional.isEmpty()) {
            return Optional.of("User not found");
        }
        UserProfile userProfile = userProfileOptional.get();
        userProfile.setValidated(validated);
        userProfileRepository.save(userProfile);
        return Optional.empty();
    }

    // admin: set userLevel: user / admin
    @Override
    public Optional<String> updateUserProfile_setUserLevel(String email, String userLevel) {
        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(email);
        if (userProfileOptional.isEmpty()) {
            return Optional.of("User not found");
        }
        UserProfile userProfile = userProfileOptional.get();
        userProfile.setUserLevel(userLevel);
        userProfileRepository.save(userProfile);
        return Optional.empty();
    }
}
