package com.group2.Tiger_Talks.backend.model.User;

import com.group2.Tiger_Talks.backend.model.Socials.FriendshipRequest;
import com.group2.Tiger_Talks.backend.model.Utils.Role;
import com.group2.Tiger_Talks.backend.model.Utils.UserLevel;
import com.group2.Tiger_Talks.backend.model.Utils.UserStatus;
import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "user_template")
public class UserTemplate {
    @Id
    private String email;

    private String password;
    private String userLevel = UserLevel.USER;   // admin / user
    private String status = UserStatus.PENDING;      // blocked / pending / active
    private boolean isValidated = false;
    private String[] securityQuestions;
    private String[] securityQuestionsAnswer;
    private String role = Role.DEFAULT;        // default / student / instructor / employee
    private boolean isOnline = false;


    @OneToOne(mappedBy = "userTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile userProfile = null;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendshipRequest> sentFriendshipRequests = new LinkedList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendshipRequest> receivedFriendshipRequests = new LinkedList<>();

    public UserTemplate(String email,
                        String password,
                        String[] securityQuestionsAnswer,
                        String[] securityQuestions) {
        this.email = email;
        this.password = password;
        this.securityQuestionsAnswer = securityQuestionsAnswer;
        this.securityQuestions = securityQuestions;
    }

    public UserTemplate() {
    }

    private String toString_() {
        return "user_template{" +
                ",\n email='" + email + '\'' +
                ",\n password='" + password + '\'' +
                ",\n userLevel='" + userLevel + '\'' +
                ",\n status='" + status + '\'' +
                "\n}\n";
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

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
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

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public List<FriendshipRequest> getSentFriendshipRequests() {
        return sentFriendshipRequests;
    }

    public void setSentFriendshipRequests(List<FriendshipRequest> sentFriendshipRequests) {
        this.sentFriendshipRequests = sentFriendshipRequests;
    }

    public List<FriendshipRequest> getReceivedFriendshipRequests() {
        return receivedFriendshipRequests;
    }

    public void setReceivedFriendshipRequests(List<FriendshipRequest> receivedFriendshipRequests) {
        this.receivedFriendshipRequests = receivedFriendshipRequests;
    }

    public Optional<String> findAnswerForSecurityQuestion(String securityQuestion) {
        for (int i = 0; i < securityQuestions.length; i++) {
            if (securityQuestions[i].equals(securityQuestion)) {
                return Optional.of(securityQuestionsAnswer[i]);
            }
        }
        return Optional.empty();
    }

}

