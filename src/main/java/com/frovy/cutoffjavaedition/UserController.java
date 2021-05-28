package com.frovy.cutoffjavaedition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
	public String greeting(HttpSession session, @RequestParam String email, @RequestParam String password) {
		User n = new User();
		n.setEmail(email);
		n.setPassword(password);
		userRepository.save(n);
		System.out.println(session.getId());
		return "Saved";
	}

	@GetMapping(path="")
	public ObjectNode getAllUsers(HttpSession session) {
		System.out.println(session.getId());
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.put("login", false);
		objectNode.put("username", "something");
		return objectNode;
	}
}
