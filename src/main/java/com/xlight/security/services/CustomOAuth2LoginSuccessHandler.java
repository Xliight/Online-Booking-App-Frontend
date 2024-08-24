package com.xlight.security.services;
import com.xlight.security.Repository.UserRepository;
import com.xlight.security.enums.Role;
import com.xlight.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
@RequiredArgsConstructor
@Component
public class CustomOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public final UserRepository  userRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Extract the OAuth2User from the Authentication object
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();


        // Get the user's information
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {

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
        // Log or display the user's information
        System.out.println("User's Namegg: " + name);
        System.out.println("User's Email: " + email);

        // Optionally, you can store these details in the session or send them as a response
        request.getSession().setAttribute("userName", name);
        request.getSession().setAttribute("userEmail", email);

        // Redirect to a default page
        setDefaultTargetUrl("/api/v1/demo-controller/testgo");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
