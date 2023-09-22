package org.example.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

public class UserDatabase implements Writable<User> {
    private static UserDatabase userDatabase;
    private final ArrayList<User> users;
    private User loggedInUser;
    
    private UserDatabase() {
        users = new ArrayList<>(readFromFile());
    }
    
    public static UserDatabase getInstance() {
        return userDatabase == null ? userDatabase = new UserDatabase() : userDatabase;
    }
    
    public ArrayList<User> getUsers() {
        return users;
    }
    
    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public User getUserByUsername(String username) {
        for (User user : users)
            if (user.getUsername().equals(username)) return user;
        return null;
    }

    public boolean haveEqualScore(User user) {
        int index = users.indexOf(user);
        return (0 < index && users.get(index - 1).getScore().getHighscore() == user.getScore().getHighscore())
            || (index + 1 < users.size() && users.get(index + 1).getScore().getHighscore() == user.getScore().getHighscore());
    }

    
    @Override
    public List<User> readFromFile() {
        File file = getFile();
        try {
            Scanner scanner = new Scanner(file);
            String usersDataString = scanner.nextLine();
            scanner.close();
            Gson gson = new Gson();
            return List.of(gson.fromJson(usersDataString, User[].class));
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }
        return null;
    }
    
    @Override
    public void writeDataInFile() {
        try {
            FileWriter fileWriter = new FileWriter(getFile());
            Gson gson = new Gson();
            String usersDataString = gson.toJson(users);
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
        File users = new File(json, "users.json");
        if (users.exists()) return users;
        try {
            users.createNewFile();
        } catch (IOException e) {
            System.err.println("File cannot be created!");
        }
        try {
            FileWriter fileWriter = new FileWriter(users);
            fileWriter.write("[]");
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Can't write in file!");
        }
        return users;
    }
    
}
