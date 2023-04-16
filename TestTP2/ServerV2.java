package server;

import javafx.util.Pair;
import server.models.RegistrationForm;
import server.models.Course;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Server {

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listen() throws IOException, ClassNotFoundException {
        String line;
        System.out.println("lets go");
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    public void disconnect() throws IOException {
        objectInputStream.close();
        objectOutputStream.close();
        client.close();
    }

    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            System.out.println("salam");
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        // TODO: implémenter cette méthode
        String[] line;
        ArrayList<Course> coursesList = new ArrayList<>();
        System.out.println("on essaye");
        try {
            System.out.println("avant");
            Scanner scan = new Scanner(new BufferedReader(new FileReader("main/java/server/data/cours.txt")));
            System.out.println("apre");
            while (scan.hasNext()) {
                line = scan.nextLine().split("\t");
                if (line[2].equals(arg)) {
                    coursesList.add(new Course(line[1], line[0], line[2]));
                }
            }
        } catch (IOException ex) {
            System.out.println("Erreur à la lecture du fichier");
        }
        try {
            System.out.println("damn zizi");
            System.out.println(coursesList);
            objectOutputStream.writeObject("Saluut");
            System.out.println("pitié");
            Course coucours = new Course("itc", "due", "dje");
            objectOutputStream.writeObject(coucours);
            System.out.println("jaimelabit");
            objectOutputStream.writeObject(coursesList);
            System.out.println("siuu");
            objectOutputStream.close();
        } catch (IOException ex) {
            System.out.println("Erreur à l'écriture du fichier");
        }
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        // TODO: implémenter cette méthode
        try {
            RegistrationForm formular = (RegistrationForm) objectInputStream.readObject();
            BufferedWriter writer;
            FileWriter fw = new FileWriter("/data/inscription.txt");
            writer = new BufferedWriter(fw);
            writer.append(formular.getCourse().getSession() + "\t" + formular.getCourse().getCode() + "\t" + formular.getMatricule() + "\t" + formular.getNom() + "\t" + formular.getPrenom() + "\t" + formular.getEmail() + "\n");
            writer.close();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Erreur à la lecture de l'objet");
        }
        BufferedWriter writer = null;
        try {
            objectOutputStream.writeUTF("Bravo, vous vous êtes inscrit au cours avec succès !");
        } catch (IOException ex) {
            System.out.println("Erreur à l'écriture du String");
        }


    }
}

