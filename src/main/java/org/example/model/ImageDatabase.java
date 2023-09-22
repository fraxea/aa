package org.example.model;

import com.google.gson.Gson;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ImageDatabase implements Writable<String> {
    private static ImageDatabase imageDatabase;
    private final ArrayList<String> externalsImagePaths;
    
    private ImageDatabase() {
        externalsImagePaths = new ArrayList<>(readFromFile());
    }
    
    public static ImageDatabase getInstance() {
        return imageDatabase == null ? imageDatabase = new ImageDatabase() : imageDatabase;
    }
    
    public String addImage(File file) throws MalformedURLException {
        String url = file.toURI().toURL().toExternalForm();
        if (!externalsImagePaths.contains(url)) externalsImagePaths.add(url);
        return url;
    }
    
    public Image getImage(String path) {
        return new Image(path);
    }
    
    @Override
    public void writeDataInFile() {
        try {
            FileWriter fileWriter = new FileWriter(getFile());
            Gson gson = new Gson();
            String usersDataString = gson.toJson(externalsImagePaths);
            fileWriter.write(usersDataString);
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Cannot write in file!");;
        }
    }
    
    @Override
    public File getFile() {
        File main = new File("src", "main");
        File resources = new File(main, "resources");
        File json = new File(resources, "database");
        File images = new File(json, "images.json");
        if (images.exists()) return images;
        try {
            images.createNewFile();
        } catch (IOException e) {
            System.err.println("File cannot be created!");
        }
        try {
            FileWriter fileWriter = new FileWriter(images);
            fileWriter.write("[]");
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Can't write in file!");
        }
        return images;
    }
    
    @Override
    public List<String> readFromFile() {
        File file = getFile();
        try {
            Scanner scanner = new Scanner(file);
            String imagesDataString = scanner.nextLine();
            scanner.close();
            Gson gson = new Gson();
            return List.of(gson.fromJson(imagesDataString, String[].class));
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }
        return null;
    }
}
