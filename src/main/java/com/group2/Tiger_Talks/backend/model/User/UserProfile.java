package com.group2.Tiger_Talks.backend.model.User;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.group2.Tiger_Talks.backend.model.Friend.Friendship;
import com.group2.Tiger_Talks.backend.model.Friend.FriendshipRequest;
import com.group2.Tiger_Talks.backend.model.Group.GroupMembership;
import com.group2.Tiger_Talks.backend.model.Notification;
import com.group2.Tiger_Talks.backend.model.Post.Post;
import com.group2.Tiger_Talks.backend.repository.User.UserProfileRepository;
import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.group2.Tiger_Talks.backend.model.Utils.*;
import static com.group2.Tiger_Talks.backend.model.Utils.RegexCheck.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserProfile implements UserProfileInterface {
    @Id
    private String email;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "sender-friendship")
    private List<Friendship> senderFriendshipList = new LinkedList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "receiver-friendship")
    private List<Friendship> receiverFriendshipList = new LinkedList<>();

    // User Sending req to others
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "sender-friendship-request")
    private List<FriendshipRequest> senderFriendshipRequestList = new LinkedList<>();

    // User Receiving req from others
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "receiver-friendship-request")
    private List<FriendshipRequest> receiverFriendshipRequestList = new LinkedList<>();

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notificationList = new LinkedList<>();

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Post> postList = new LinkedList<>();

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMembership> groupMembershipList = new LinkedList<>();

    private String password;
    private String userLevel = UserLevel.USER;   // admin / user
    private String status = UserStatus.PENDING;      // blocked / pending / active
    private boolean isValidated = false;
    private String[] securityQuestions;
    private String[] securityQuestionsAnswer;
    private String role = Role.DEFAULT;        // default / student / instructor / employee
    private String onlineStatus = OnlineStatus.OFFLINE;

    @Column(unique = true)
    private String userName;

    private String personalInterest;
    private String location;
    private String postalCode;
    private String biography;
    private String profileAccessLevel = ProfileAccessLevel.PRIVATE;  // public / protected / private
    private String phoneNumber;
    private int age;
    private String gender;
    private String firstName;
    private String lastName;

    private String profilePictureUrl = DEFAULT_PROFILE_PICTURE;

    public UserProfile(String firstName,
                       String lastName,
                       int age,
                       String gender,
                       String userName,
                       String email,
                       String password,
                       String[] securityQuestionsAnswer,
                       String[] securityQuestions) {
        this.email = email;
        this.password = password;
        this.securityQuestionsAnswer = securityQuestionsAnswer;
        this.securityQuestions = securityQuestions;
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserProfile() {
    }

    // Getters and setters

    public static Optional<String> verifyBasics(UserProfileInterface userProfile, UserProfileRepository userProfileRepository, boolean isNewUser) {
        if (!NAME_NORM.matcher(userProfile.getFirstName()).matches()) {
            return Optional.of("First name must contain no symbols");
        }
        if (!NAME_NORM.matcher(userProfile.getLastName()).matches()) {
            return Optional.of("Last name must contain no symbols");
        }
        if (userProfile.getAge() <= 0) {
            return Optional.of("Age must be grater than 0");
        }
        if (!EMAIL_NORM.matcher(userProfile.getEmail()).matches()) {
            return Optional.of("Invalid email address. Please use dal email address!");
        }
        if (isNewUser && userProfileRepository.findUserProfileByUserName(userProfile.getUserName()).isPresent()) {
            return Optional.of("Username has already existed!");
        }
        if (isNewUser && userProfileRepository.existsById(userProfile.getEmail())) {
            return Optional.of("Email has already existed!");
        }
        if (!PASSWORD_NORM_LENGTH.matcher(userProfile.getPassword()).matches()) {
            return Optional.of("Password must have a minimum length of 8 characters.");
        }
        if (!PASSWORD_NORM_UPPERCASE.matcher(userProfile.getPassword()).matches()) {
            return Optional.of("Password must have at least 1 uppercase character.");
        }
        if (!PASSWORD_NORM_LOWERCASE.matcher(userProfile.getPassword()).matches()) {
            return Optional.of("Password must have at least 1 lowercase character.");
        }
        if (!PASSWORD_NORM_NUMBER.matcher(userProfile.getPassword()).matches()) {
            return Optional.of("Password must have at least 1 number.");
        }
        if (!PASSWORD_NORM_SPECIAL_CHARACTER.matcher(userProfile.getPassword()).matches()) {
            return Optional.of("Password must have at least 1 special character.");
        }
        return Optional.empty();
    }

    public void updateProfile(UserProfileDTO userProfileDTO) {
        this.age = userProfileDTO.age();
        this.email = userProfileDTO.email();
        this.status = userProfileDTO.status();
        this.isValidated = userProfileDTO.validated();
        this.role = userProfileDTO.role();
        this.onlineStatus = userProfileDTO.onlineStatus();
        this.userName = userProfileDTO.userName();
        this.biography = userProfileDTO.biography();
        this.profileAccessLevel = userProfileDTO.profileAccessLevel();
        this.gender = userProfileDTO.gender();
        this.firstName = userProfileDTO.firstName();
        this.lastName = userProfileDTO.lastName();
        this.userLevel = userProfileDTO.userLevel();
        this.profilePictureUrl = userProfileDTO.profilePictureUrl();
    }

    public UserProfileDTO toDTO() {
        return new UserProfileDTO(this);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    public String[] getSecurityQuestionsAnswer() {
        return securityQuestionsAnswer;
    }

    public void setSecurityQuestionsAnswer(String[] securityQuestionsAnswer) {
        this.securityQuestionsAnswer = securityQuestionsAnswer;
    }

    public String[] getSecurityQuestions() {
        return securityQuestions;
    }

    public void setSecurityQuestions(String[] securityQuestions) {
        this.securityQuestions = securityQuestions;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonalInterest() {
        return personalInterest;
    }

    public void setPersonalInterest(String personalInterest) {
        this.personalInterest = personalInterest;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getProfileAccessLevel() {
        return profileAccessLevel;
    }

    public void setProfileAccessLevel(String profileAccessLevel) {
        this.profileAccessLevel = profileAccessLevel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Friendship> getSenderFriendshipList() {
        return senderFriendshipList;
    }

    public void setSenderFriendshipList(List<Friendship> senderFriendshipList) {
        this.senderFriendshipList = senderFriendshipList;
    }

    public List<Friendship> getReceiverFriendshipList() {
        return receiverFriendshipList;
    }

    public void setReceiverFriendshipList(List<Friendship> receiverFriendshipList) {
        this.receiverFriendshipList = receiverFriendshipList;
    }

    public List<FriendshipRequest> getSenderFriendshipRequestList() {
        return senderFriendshipRequestList;
    }

    public void setSenderFriendshipRequestList(List<FriendshipRequest> senderFriendshipRequestList) {
        this.senderFriendshipRequestList = senderFriendshipRequestList;
    }

    public List<FriendshipRequest> getReceiverFriendshipRequestList() {
        return receiverFriendshipRequestList;
    }

    public void setReceiverFriendshipRequestList(List<FriendshipRequest> receiverFriendshipRequestList) {
        this.receiverFriendshipRequestList = receiverFriendshipRequestList;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public Optional<String> findAnswerForSecurityQuestion(String securityQuestion) {
        for (int i = 0; i < securityQuestions.length; i++) {
            if (securityQuestions[i].equals(securityQuestion)) {
                return Optional.of(securityQuestionsAnswer[i]);
            }
        }
        return Optional.empty();
    }

    public List<UserProfile> getAllFriends() {
        return Stream.concat(
                senderFriendshipList.stream().map(Friendship::getReceiver),
                receiverFriendshipList.stream().map(Friendship::getSender)
        ).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile other = (UserProfile) o;
        return this.email.equals(other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
