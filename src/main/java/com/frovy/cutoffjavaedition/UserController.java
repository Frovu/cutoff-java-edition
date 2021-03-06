package com.frovy.cutoffjavaedition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import org.springframework.security.crypto.password.PasswordEncoder;


@RestController
@RequestMapping(path="/user")
public class UserController {
	static class UserData {
		public String email;
		public String password;
	}
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping(path="")
	public ResponseEntity register(HttpSession session, @RequestBody UserData req) {
		if (userRepository.existsByEmail(req.email))
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		User n = new User();
		n.setEmail(req.email);
		n.setPassword(passwordEncoder.encode(req.password));
		session.setAttribute("login", true);
		session.setAttribute("uid", n.getId());
		session.setAttribute("username", req.email);
		userRepository.save(n);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}

	@PostMapping(path="/login")
	public ResponseEntity login(HttpSession session, @RequestBody UserData req) {
		User target = userRepository.findOneByEmail(req.email);
		if (null == target)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		if (!passwordEncoder.matches(req.password, target.getPassword()))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		session.setAttribute("login", true);
		session.setAttribute("uid", target.getId());
		session.setAttribute("username", target.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PostMapping(path="/logout")
	public String login(HttpSession session) {
		session.setAttribute("login", false);
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
