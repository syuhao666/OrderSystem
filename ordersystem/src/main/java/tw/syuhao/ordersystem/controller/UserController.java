package tw.syuhao.ordersystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.repository.UserRepository;
import tw.syuhao.ordersystem.service.UserService;

@Controller
public class UserController {

    
    private final UserRepository userRepository;

    @Autowired
    private UserService service;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
	public String displayRegister() {
		return "register";
	}

    @PostMapping("/register")
	public String doRegister(
			Model model,
			@RequestParam String username,
			@RequestParam String password,
			@RequestParam String name,
			@RequestParam String email) {
		try {
			service.register(username, password, name, email);
			return "redirect:/login";
		} catch (Exception e) {
			System.out.println(e);
			model.addAttribute("error", "something error");
			return "register";
		}
	}

    @GetMapping("/login")
	public String showLogin() {
		return "login";
	}
	
    @PostMapping("/login")
	public String doLogin(			
			Model model,
			@RequestParam String username,
			@RequestParam String password,
			HttpSession session) {
		
		Users user = service.login(username, password);
		if (user != null) {
			session.setAttribute("user", user);
			return "redirect:/home";
		}
		
		return "login";
	}

	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
		Users user = (Users)session.getAttribute("user");
		if (user == null) return "redirect:/login";

		return "home";
	}
}
