package com.frovy.cutoffjavaedition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.Map;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping(path="/instance")
public class InstanceController {

	@Autowired
	private InstanceRepository instanceRepository;

	@PostMapping(path="")
	public Map<String, String> create(
		@RequestParam String datetime,
		@RequestParam Float kp,
		@RequestParam Float alt,
		@RequestParam Float lat,
		@RequestParam Float lon,
		@RequestParam Float lower,
		@RequestParam Float upper,
		@RequestParam Float step) {
		try {
			Instance n = new Instance();
			n.setOwner(1);
			n.setName("New Instance");
			n.setCreated(new java.sql.Timestamp(System.currentTimeMillis()));
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
			Date date = (Date) df.parse(datetime);
			n.setDatetime(new java.sql.Timestamp(date.getTime()));
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
			Computation computation = new Computation(n);
			computation.start();
			return Collections.singletonMap("id", n.getId());
		} catch(java.text.ParseException e) {
			return Collections.singletonMap("error", "parsing");
		} catch(java.io.IOException e) {
			return Collections.singletonMap("error", "fileio");
		}
	}
}
