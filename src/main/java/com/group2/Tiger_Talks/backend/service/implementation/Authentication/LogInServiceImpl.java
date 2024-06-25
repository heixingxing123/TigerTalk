package com.group2.Tiger_Talks.backend.service.implementation.Authentication;

import com.group2.Tiger_Talks.backend.model.User.UserTemplate;
import com.group2.Tiger_Talks.backend.repsitory.User.UserTemplateRepository;
import com.group2.Tiger_Talks.backend.service.Authentication.LogInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LogInServiceImpl implements LogInService {

    @Autowired
    private UserTemplateRepository userTemplateRepository;

    @Override
    public Optional<UserTemplate> logInUserTemplate(String email, String password) {
        Optional<UserTemplate> userTemp = userTemplateRepository.findUserTemplateByEmail(email);
        if (userTemp.isPresent() && userTemp.get().getPassword().equals(password)) {
            return userTemp;
        } else {
            return Optional.empty();
        }
    }

    public Optional<UserTemplate> getUserByEmail(String email) {
        return userTemplateRepository.findUserTemplateByEmail(email);
    }

}
