package com.frovy.cutoffjavaedition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.stream.StreamSupport;
import java.util.stream.Stream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@RestController
@RequestMapping(path="/instance")
public class InstanceController {

	static class InstanceData {
		public String datetime;
		public Float kp;
		public Float alt;
		public Float lat;
		public Float lon;
		public Float lower;
		public Float upper;
		public Float step;
	}

	@Autowired
	private InstanceRepository instanceRepository;

	private HttpStatus authorize(HttpSession session, String id) {
		if (null==session.getAttribute("login") || true != (boolean)session.getAttribute("login"))
			return HttpStatus.UNAUTHORIZED;
		Instance target = instanceRepository.findById(id).orElse(null);
	if (null == target)
		return HttpStatus.NOT_FOUND;
		if (null==session.getAttribute("uid") || (int)session.getAttribute("uid") != target.getOwner())
			return HttpStatus.FORBIDDEN;
		return HttpStatus.OK;
	}

	@PostMapping(path="")
	public Map<String, String> create(HttpSession session, @RequestBody InstanceData req) {
		try {
			if (null==session.getAttribute("login") || true != (boolean)session.getAttribute("login"))
				return Collections.singletonMap("error", "unauthorized");
			Instance n = new Instance();
			n.setOwner((int)session.getAttribute("uid"));
			n.setName("New Instance");
			n.setCreated(new java.sql.Timestamp(System.currentTimeMillis()));
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
			Date date = (Date) df.parse(req.datetime);
			n.setDatetime(new java.sql.Timestamp(date.getTime()));
			n.setModel("00");
			n.setKp(req.kp);
			n.setAlt(req.alt);
			n.setLat(req.lat);
			n.setLon(req.lon);
			n.setVertical(new Float(.0));
			n.setAzimutal(new Float(.0));
			n.setLower(req.lower);
			n.setUpper(req.upper);
			n.setStep(req.step);
			n.setFlightTime(new Float(.1));
			instanceRepository.save(n);
			Computation computation = new Computation(n, instanceRepository);
			computation.start();
			return Collections.singletonMap("id", n.getId());
		} catch(java.text.ParseException e) {
			return Collections.singletonMap("error", "parsing");
		} catch(java.io.IOException e) {
			return Collections.singletonMap("error", "fileio");
		}
	}

	@GetMapping(path="")
	public ResponseEntity getAll(HttpSession session) {
		if (null==session.getAttribute("login") || true != (boolean)session.getAttribute("login"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		int uid = (int)session.getAttribute("uid");
		Stream<Instance> list = StreamSupport.stream(instanceRepository.findAll().spliterator(), false).filter(
			item -> item.getOwner() == uid
		);
		return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("instances", list));
	}

	@GetMapping(path="{id}")
	public ResponseEntity fetch(HttpSession session, @PathVariable String id) {
		HttpStatus auth = authorize(session, id);
		if (auth != HttpStatus.OK)
			return ResponseEntity.status(auth).body(null);
		Instance target = instanceRepository.findById(id).orElse(null);
		if (null == target.getCompleted())
			return ResponseEntity.status(auth).body(Collections.singletonMap("status", "processing"));
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.put("status", "complted");
		objectNode.put("data", Computation.fetchResults(target));
		return ResponseEntity.status(HttpStatus.OK).body(objectNode);
	}
}
