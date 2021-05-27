package com.frovy.cutoffjavaedition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(path="/instance")
public class InstanceController {

	@Autowired
	private InstanceRepository instanceRepository;

	@PostMapping(path="")
	public Map<String, String> create(
		@RequestParam java.sql.Timestamp datetime,
		@RequestParam Float kp,
		@RequestParam Float alt,
		@RequestParam Float lat,
		@RequestParam Float lon,
		@RequestParam Float lower,
		@RequestParam Float upper,
		@RequestParam Float step) {
		Instance n = new Instance();
		n.setOwner(1);
		n.setName("New Instance");
		n.setCreated(new java.sql.Timestamp(System.currentTimeMillis()));
		n.setDatetime(datetime);
		n.setModel("00");
		n.setKp(kp);
		n.setAlt(alt);
		n.setLat(lat);
		n.setLon(lon);
		n.setVertical(new Float(.0));
		n.setAzimutal(new Float(.0));
		n.setLower(lower);
		n.setUpper(upper);
		n.setStep(step);
		n.setFlightTime(new Float(.1));
    instanceRepository.save(n);
		return Collections.singletonMap("id", n.getId());
	}
}
