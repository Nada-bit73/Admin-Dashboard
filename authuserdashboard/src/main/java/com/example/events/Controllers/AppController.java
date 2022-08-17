package com.example.events.Controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.events.models.LoginUser;
import com.example.events.models.User;
import com.example.events.services.UserService;

import ch.qos.logback.core.net.SyslogOutputStream;

import java.util.*;

@Controller
public class AppController {

	@Autowired
	private UserService userServ;

	// display Registration page
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("newUser", new User());
		model.addAttribute("newLogin", new LoginUser());
		return "user/loginReg.jsp";
	}

	// process the registration data
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("newUser") User newUser, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		// send the instance and the result
		userServ.register(newUser, result);
		if (result.hasErrors()) {
			model.addAttribute("newLogin", new LoginUser());
			return "user/loginReg.jsp";
		}
		redirectAttributes.addFlashAttribute("success", "Your account has been created successfully!");
		return "redirect:/";
	}

	// process login data
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, BindingResult result, Model model,
			HttpSession session) {
		User user = userServ.login(newLogin, result);
		if (result.hasErrors()) {
			model.addAttribute("newUser", new User());
			return "user/loginReg.jsp";
		}
		session.setAttribute("userFirstName", user.getFirstName());
		session.setAttribute("userId", user.getId());
		User user2 = userServ.findById(user.getId());
		if (user2.getRole().equals("normal")) {
			return "redirect:/normaldashboard";
		}
		if (user2.getRole().equals("admin")) {
			return "redirect:/dashboard";
		}
		if (user2.getRole().equals("superAdmin")) {
			return "redirect:/admindashboard";
		}
		return "redirect:/normaldashboard";
	}

	@GetMapping("/admindashboard")
	public String displaySuperAdminDashboard(HttpSession session, Model model) {
		Long idLong = (Long) session.getAttribute("userId");
		User user = userServ.findById(idLong);
		if (user.getRole().equals("superAdmin")) {
			session.getAttribute("userFirstName");
			List<User> users = userServ.findAllUsers();
			model.addAttribute("users", users);
			return "user/adminDashboard.jsp";
		}
		if (idLong == null) {
			return "redirect:/";
		}

		else {
			return "redirect:/";
		}

	}

	@GetMapping("/normaldashboard")
	public String displayNormalDashboard(HttpSession session, Model model) {
		Long idLong = (Long) session.getAttribute("userId");
		User user = userServ.findById(idLong);
		if (user.getRole().equals("normal")) {
			model.addAttribute("user", user);
			return "user/normaldashboard.jsp";
		}
		if (idLong == null) {
			return "redirect:/";
		} else {
			return "redirect:/";
		}
	}

	@GetMapping("/dashboard")
	public String displayDashboard(HttpSession session, Model model) {
		Long idLong = (Long) session.getAttribute("userId");
		User user = userServ.findById(idLong);
		if (user.getRole().equals("admin")) {
			session.getAttribute("userFirstName");
			List<User> users = userServ.findAllNormalUsers();
			model.addAttribute("users", users);

			List<User> usersAdmin = userServ.findAllAdminUsers();
			model.addAttribute("adminUsers", usersAdmin);

			return "user/dashboard.jsp";
		}
		if (idLong == null) {
			return "redirect:/";
		}

		else {
			return "redirect:/";
		}
	}

	@PostMapping("/{id}/makeadmin")
	public String makeAdmin(@PathVariable("id") Long id, Model model, HttpSession session) {
		User user = userServ.findById(id);
		userServ.makeAdmin(user, id);
		return "redirect:/dashboard";
	}
//superAdmin
	@PostMapping("/{id}/makeadmin/superAdmin")
	public String makeAdminBySuperAdmin(@PathVariable("id") Long id, Model model, HttpSession session) {
		User user = userServ.findById(id);
		userServ.makeAdmin(user, id);
		return "redirect:/admindashboard";
	}
	
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
	public String destroy(@PathVariable("id") Long id) {
		userServ.deleteUser(id);
		return "redirect:/dashboard";
	}
	
	@RequestMapping(value = "/{id}/deleteSuperAdmin", method = RequestMethod.DELETE)
	public String destroyadmindashboard(@PathVariable("id") Long id) {
		userServ.deleteUser(id);
		return "redirect:/admindashboard";
	}
	
	

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		Long idLong = (Long) session.getAttribute("userId");
		userServ.setUserLastSignIn(idLong);
		session.invalidate();
		return "redirect:/";
	}
}
