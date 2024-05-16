package com.my.springauthentication.service;

import com.my.springauthentication.dto.UserDto;
import com.my.springauthentication.exception.NotFoundException;
import com.my.springauthentication.model.User;
import com.my.springauthentication.repository.UserRepository;
import com.my.springauthentication.utils.ConstantUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                        .orElseThrow(() -> new UsernameNotFoundException(ConstantUtils.USER_NOT_FOUND));
            }
        };
    }

    public Optional<UserDto> getUserDetails(UUID uuid) {
        Optional<User> user = userRepository.findByUuid(uuid);

        UserDto userDto = new UserDto();
        if (user.isPresent()) {
            User userDetails = user.get();
            userDto.setId(userDetails.getUuid());
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

    @Transactional
    public String deleteUser(UUID uuid) {
        if (!userRepository.existsByUuid(uuid)) {
            throw new NotFoundException(ConstantUtils.USER_NOT_FOUND);
        }
        userRepository.deleteByUuid(uuid);
        return ConstantUtils.USER_DELETED;
    }
}
