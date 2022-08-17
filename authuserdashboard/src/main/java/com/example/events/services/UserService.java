package com.example.events.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.hibernate.internal.build.AllowSysOut;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import com.example.events.models.LoginUser;
import com.example.events.models.User;
import com.example.events.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepo;

	public User register(User newUser, BindingResult result) {
		if (userRepo.findByEmail(newUser.getEmail()).isPresent()) {
			result.rejectValue("email", "Unique", "This email is already in use!");
		}
		if (!newUser.getPassword().equals(newUser.getConfirm())) {
			result.rejectValue("confirm", "Matches", "The Confirm Password must match Password!");
		}
		if (result.hasErrors()) {
			return null;
		} else {
			String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
			newUser.setPassword(hashed);
			User user = userRepo.save(newUser);
			if(user.getId() == 1) {
				user.setRole("superAdmin");
				userRepo.save(user);   
			}else {
				user.setRole("normal");
				userRepo.save(user); 
			}
		return user;
		}
	}

	public User login(LoginUser newLogin, BindingResult result) {
		if (result.hasErrors()) {
			return null;
		}
		// fetch Optional user list by its key => email
		Optional<User> potentialUser = userRepo.findByEmail(newLogin.getEmail());

		if (!potentialUser.isPresent()) {
			result.rejectValue("email", "Unique", "Unknown email!");
			return null;
		}
		// if email is correct ,fetch user instance then check pw
		User user = potentialUser.get();
		if (!BCrypt.checkpw(newLogin.getPassword(), user.getPassword())) {
			result.rejectValue("password", "Matches", "Invalid Password!");
		}
		if (result.hasErrors()) {
			return null;
		} else {
			return user;
		}
	}

	public List<User> allUsers() {
		return userRepo.findAll();
	}
	
	public User findById(Long id) {
		Optional<User> optionalUser = userRepo.findById(id);
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		} else {
			return null;
		}
	}
	
	public User setUserLastSignIn(Long id) {
		User user = findById(id);
		LocalDate dateObj = LocalDate.now();
        String currentDate = dateObj.toString();
        user.setLastSignin(currentDate);
        return userRepo.save(user);      
	}

	public List<User> findAllNormalUsers() {
		List<User> users = userRepo.findAll();
		List<User> normalUsers = new ArrayList<>();
		for(User user :  users){
			if(user.getRole().equals("normal")) {
				normalUsers.add(user);
			}
		}
		return normalUsers; 
	}

	public List<User> findAllAdminUsers() {
		List<User> users = userRepo.findAll();
		List<User> adminUsers = new ArrayList<>();
		for(User user :  users){
			if(user.getRole().equals("admin")) {
				adminUsers.add(user);
			}
		}
		
		return adminUsers; 
	}

	public void makeAdmin(User user,Long id) {
				user.setId(id);
				user.setRole("admin");
				userRepo.save(user);
	}

	public void deleteUser(Long id) {
		userRepo.deleteById(id);
	}

	public List<User> findAllUsers() {
		List<User> users = userRepo.findAll();
		List<User> allUsers = new ArrayList<>();
		for(User user :  users){
			if(!user.getRole().equals("superAdmin")) {
				allUsers.add(user);
			}
		}
		
		return allUsers; 
	}


}
