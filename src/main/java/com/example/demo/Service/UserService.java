package com.example.demo.Service;

import com.example.demo.Model.UserModel;
import com.example.demo.Repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> user= userRepo.findByUsername(username);
        if (user.isPresent())
        {
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .build();
        }
        throw new UnsupportedOperationException("Ten hoac mk khong ton tai");
    }


    public UserModel getUserById(Long userId) {
        return userRepo.findById(userId).orElse(null);
    }


    public UserModel getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }

    public List<UserModel> getAllUsers() {
        return userRepo.findAll();
    }

    public UserModel updateUser(Long id, UserModel updatedUser) {
        UserModel user = userRepo.findById(id).orElse(null);
        if (user != null) {
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            user.setEmail(updatedUser.getEmail());
            user.setAvatarUrl(updatedUser.getAvatarUrl());
            userRepo.save(user);
        }
        return user;
    }
}
