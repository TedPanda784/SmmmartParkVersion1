package com.smmmartparkversion1;

import com.smmmartparkversion1.model.User;

import java.io.*;

public class SessionManager {

    private static final String SESSION_FILE = "session.dat";

    public static void createSession(User user) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SESSION_FILE))) {
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User loadSession() {
        File file = new File(SESSION_FILE);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (User) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void clearSession() {
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean hasActiveSession() {
        return new File(SESSION_FILE).exists();
    }
}