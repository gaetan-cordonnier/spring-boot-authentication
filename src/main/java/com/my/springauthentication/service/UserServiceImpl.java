package com.my.springauthentication.service;

import com.my.springauthentication.dto.UserDto;
import com.my.springauthentication.model.User;
import com.my.springauthentication.repository.UserRepository;
import com.my.springauthentication.utils.ConstantUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) {
                return userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    public Optional<UserDto> getUserDetails(UUID id) {
        Optional<User> user = userRepository.findById(id);

        UserDto userDto = new UserDto();
        if (user.isPresent()) {
            User userDetails = user.get();
            userDto.setId(userDetails.getId());
            userDto.setFirstname(userDetails.getFirstname());
            userDto.setLastname(userDetails.getLastname());
            userDto.setEmail(userDetails.getEmail());
            userDto.setRole(userDetails.getRole());
            userDto.setLanguage(userDetails.getLanguage());
            userDto.setTheme(userDetails.getTheme());
            userDto.setValidated(userDetails.getValidated());
            userDto.setVerification(userDetails.getVerification());
        }
        return Optional.of(userDto);
    }

    public String deleteUser(UUID id) {
        userRepository.deleteById(id);
        return ConstantUtils.USER_DELETED;
    }
}
