package com.frovy.cutoffjavaedition;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class Computation extends Thread {
	private static final String baseDir = "cutoff";
	private static final String iniFilename = "CutOff.ini";
	private static final String datFilename = "Cutoff.dat";
	private static final String exeFilename = "Cutoff2050.exe";

	private Instance instance;
	private InstanceRepository instanceRepository;

	public Computation(Instance instance, InstanceRepository instanceRepository)
		throws IOException {
			this.instance = instance;
			this.instanceRepository = instanceRepository;
			new File(baseDir, instance.getId()).mkdirs();
			String content = "\n";
			Date date = new Date();
			date.setTime(instance.getDatetime().getTime());
			String dateStr = new SimpleDateFormat("dd.MM.yyyy").format(date);
			String timeStr = new SimpleDateFormat("HH:mm:ss").format(date);
			content += dateStr + "\n" + timeStr + "\n";
			content += "0\n0\n0\n0\n0\n0\n";
			content += instance.getKp().toString() + "\n";
			content += instance.getModel().toString() + "\n";
			content += instance.getAlt().toString() + "\n";
			content += instance.getLat().toString() + "\n";
			content += instance.getLon().toString() + "\n";
			content += instance.getVertical().toString() + "\n";
			content += instance.getAzimutal().toString() + "\n";
			Float lower = instance.getLower();
			content += (lower != 0 ? lower : instance.getStep()).toString() + "\n";
			content += instance.getUpper().toString() + "\n";
			content += instance.getStep().toString() + "\n";
			content += instance.getFlightTime().toString() + "\n";
			content += "0"; // no traces
			Files.write(Paths.get(baseDir, instance.getId(), iniFilename), content.getBytes());
	}

	public void run()  {
		try {
			File exe = new File(exeFilename);
			ProcessBuilder builder = new ProcessBuilder().command("wine", exe.getAbsolutePath());
			builder.directory(new File(baseDir, instance.getId()));
			Process exec = builder.start();
			int exitCode = exec.waitFor();
			instance.setCompleted(new java.sql.Timestamp(System.currentTimeMillis()));
			instanceRepository.save(instance);
			System.out.println(instance.getId() + " done: " + exitCode);
		} catch (IOException e) {

		} catch (InterruptedException e) {

		}
	}

	public static ObjectNode fetchResults(Instance instance) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		try {
			Path path = Paths.get(baseDir, instance.getId(), datFilename);
			if (!Files.exists(path))
				return objectNode;
			ArrayNode arrayNode = objectNode.putArray("particles");
			BufferedReader reader = Files.newBufferedReader(path);
			String line = reader.readLine();
			while (line != null && !line.startsWith("Cutoff") ) {
				ArrayNode an = mapper.createArrayNode();
				for (String a : line.substring(1).split("\\s+")) {
					an.add(Float.parseFloat(a));
				}
				arrayNode.add(an);
				line = reader.readLine();
			}
			objectNode.put("lower", Float.parseFloat(reader.readLine().substring(1).split("\\s+")[1]));
			objectNode.put("upper", Float.parseFloat(reader.readLine().substring(1).split("\\s+")[1]));
			objectNode.put("effective", Float.parseFloat(reader.readLine().substring(1).split("\\s+")[1]));
		} catch (IOException e) {

		}
		return objectNode;
	}
}
