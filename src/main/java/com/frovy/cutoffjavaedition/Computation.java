package com.frovy.cutoffjavaedition;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Computation {
	private final String baseDir = "cutoff";
	private final String iniFilename = "CutOff.ini";
	private final String datFilename = "Cutoff.dat";
	private final String exeFilename = "Cutoff2050.exe";

	private String id;

	public Computation(Instance instance)
		throws IOException {
			this.id = instance.getId();
			new File(baseDir, id).mkdirs();
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
			Files.write(Paths.get(baseDir, id, iniFilename), content.getBytes());
	}

	public void run() {

	}
}
