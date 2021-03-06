package gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import utilities.ErrorPopup;
import video.DriverSessionData;

import com.google.gson.GsonBuilder;

import control.Controller;
import control.ProfileInfo;

public class GSONFileWriter {

	private static final GsonBuilder GSON_BUILDER = new GsonBuilder();

	public void writeToFile(String fileName, String json) {
		try {
			File file = new File(fileName);
			file.setWritable(true);
			FileWriter writer = new FileWriter(file);
			writer.write(json);
			writer.close();
			file.setReadOnly();
		} catch (IOException e) {
			new ErrorPopup("Could not write to " + fileName);
		}
	}

	public void updateDriverSessionData(File videoJsonFile, DriverSessionData dataToWrite) {
		writeToFile(videoJsonFile.getPath(), 
				GSON_BUILDER.create().toJson(dataToWrite, DriverSessionData.class));	
	}

	public void initializeDriverProfile(String initials) {
		ProfileInfo info = new ProfileInfo(initials);
		info.initialize();
		writeToFile(GSONFileReader.PROFILE_FILE,
				GSON_BUILDER.create().toJson(info, ProfileInfo.class));
	}

	/**
	 * @param driverSessionFile
	 * @param data
	 * @param finalFileName - the name of the file that the driver session
	 * will be saved to. This file will appear on the driver's desktop after
	 * they finish driving.
	 */
	public void terminateDeliverable(File driverSessionFile, DriverSessionData data, String finalFileName) {
		data.terminate();
		data.setFileName(finalFileName);
		writeToFile(driverSessionFile.getPath(), 
				GSON_BUILDER.create().toJson(data, DriverSessionData.class));
	}

	public void writeStartTimeToFile(File driverSessionFile) {
		DriverSessionData data = Controller.GSON_READER.readDriverSessionData(driverSessionFile);
		data.setStartTime();
		writeToFile(driverSessionFile.getPath(), 
				GSON_BUILDER.create().toJson(data, DriverSessionData.class));		
	}
}