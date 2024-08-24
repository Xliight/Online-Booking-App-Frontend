package com.xlight.security.services;
import com.xlight.security.Repository.UserRepository;
import com.xlight.security.enums.Role;
import com.xlight.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Extract user details from OAuth2UserRequest
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        System.out.println("email'sff : " +email);

        // Check if the user already exists
        User user = userRepository.findByEmail(email).orElse(null);
        System.out.println("User's : " + user.getEmail());

        if (user == null) {
            System.out.println("User's : " + user.getEmail());

            user = new User();
            user.setEmail(email);
            user.setFirstname(name);
            user.setPassword("gg");  // Set to empty string or a default password if required
            user.setRole(Role.USER);
            System.out.println("User's : " + user.getEmail() + ", " + user.getFirstname() + ", " + user.getPassword() + ", " + user.getRole());
// Set default role or handle as necessary
            userRepository.save(user);
            System.out.println("done   ");

        }

        // Return a custom user object with OAuth2 details

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                oAuth2User.getAttributes(),
                "email"
        );
    }
}
