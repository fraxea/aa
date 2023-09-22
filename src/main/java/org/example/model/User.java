package org.example.model;

import javafx.scene.image.Image;
import org.example.view.images.Avatar;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class User {
    private String username;
    private String password;
    private String imageName;
    private final Score score;
    private final GameSettings gameSettings;
    
    
    public User(String username, String password, String imageName) {
        this.username = username;
        setPassword(password);
        if (imageName.equals("imagePreview"))
            this.imageName = Avatar.getRandomAvatar().getName();
        else this.imageName = imageName;
        score = new Score();
        gameSettings = new GameSettings();
    }
    
    public String getUsername() {
        return username;
    }
    
    public Image getImage() {
        Avatar avatar = Avatar.getAvatarByName(imageName);
        if (avatar != null) return avatar.getImage();
        return ImageDatabase.getInstance().getImage(imageName);
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public Score getScore() {
        return score;
    }
        
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        if (password != null && !password.isEmpty())
            this.password = hashWith256(password);
    }
    
    public void setImageName(String imageName) {
        if (!imageName.matches("imagePreview")) this.imageName = imageName;
    }

    public boolean isPasswordCorrect(String password) {
        if (this.password == null) return false;
        return this.password.equals(hashWith256(password));
    }
    
    public boolean isGuest() {
        return password == null;
    }
    
    private String hashWith256(String textToHash) {
        if (textToHash == null) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);
            byte[] hashedByteArray = digest.digest(byteOfTextToHash);
            String encoded = Base64.getEncoder().encodeToString(hashedByteArray);
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
}
