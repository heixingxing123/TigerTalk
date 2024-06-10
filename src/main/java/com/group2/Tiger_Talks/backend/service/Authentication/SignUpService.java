package com.group2.Tiger_Talks.backend.service.Authentication;

import com.group2.Tiger_Talks.backend.model.User.UserTemplate;

import java.util.List;
import java.util.Optional;

public interface SignUpService {
    /**
     * Tries to save a user to a database
     * @param userTemplate The user template to save
     * @return Returns an error as a string of any else returns an empty optional
     */
    public Optional<String> signUpUserTemplate(UserTemplate userTemplate);

    public List<UserTemplate> getAllUserTemplates();
}
