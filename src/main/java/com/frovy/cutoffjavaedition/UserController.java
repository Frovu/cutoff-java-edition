package com.frovy.cutoffjavaedition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.servlet.http.HttpSession;
import java.util.Collections;

@RestController
@RequestMapping(path="/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@PostMapping(path="")
	public String register(HttpSession session, @RequestParam String email, @RequestParam String password) {
		User n = new User();
		n.setEmail(email);
		n.setPassword(password);
		userRepository.save(n);
		System.out.println(session.getId());
		return "Saved";
	}

	@PostMapping(path="/login")
	public ResponseEntity login(HttpSession session, @RequestParam String email, @RequestParam String password) {
		session.setAttribute("username", email);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	@PostMapping(path="/logout")
	public ResponseEntity login(HttpSession session) {
		session.getAttribute("login", false);
		session.setAttribute("username", null);
		return "logged out";
	}

	@GetMapping(path="")
	public ObjectNode info(HttpSession session) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();

		objectNode.put("login", null!=session.getAttribute("login") ? (boolean)session.getAttribute("login") : false);
		objectNode.put("username", null!=session.getAttribute("username") ? (String)session.getAttribute("username") : "none");
		return objectNode;
	}
}
