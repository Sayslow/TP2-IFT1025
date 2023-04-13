public class ClientSimple {
    private Socker clientSocket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String sessionChoice = "Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:\n1. Automne\n2. Hiver\n 3. Ete\n> Choix:";
    private String decision = "Que souhaitez-vous faire ?\n1. Consulter les cours offerts pour une autre session\n2. Inscription à un cours\n> Choix:";
    public static void main(String[] args) {
        try {
            clientSocket = new Socket("127.0.0.1", 1337);
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            objectOutputStream = new ObjectOutputStream(clienSocket.getOutputStream());

            Scanner scan = new Scanner(System.in);
            System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
            System.out.print(sessionChoice);
            load(scan.nextInt());


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void load(int choice) {
        String session;
        if (choice == 1) {
            session = "Automne";
        } else if (choice == 2) {
            session = "Hiver";
        } else if (choice == 3) {
            session = "Ete";
        }
        objectOutputStream.writeObject("CHARGER " + session);

        objectOutputStream.close();

        System.out.println("Les cours offerts pendant la session d'" + session.lower() + " sont:");
        Course course;
        int i = 0;
        while ((course = objectInputStream.readObject()) != null) {
            i++;
            System.out.println(i + ". " + course.code + "\t" + course.name);
        }
        System.out.print(decision);
        decisionHandler(scan.nextInt());
    }

    public static void decisionHandler(int choice) {
        if (choice == 1) {
            System.out.print(sessionChoice);
            load(scan.nextInt());
        } else if (choice == 2) {
            inscription();
        }
    }
    public static void inscription(){
                    System.out.println("Entrez votre prénom : ");
                    String prenom = scanner.nextLine();
                    System.out.println("Entrez votre nom : ");
                    String nom = scanner.nextLine();
                    System.out.println("Entrez votre email (étudiant) : ");
                    String email = scanner.nextLine();
                    System.out.println("Entrez votre matricule : ");
                    String matricule = scanner.nextLine();
                    System.out.println("Entrez le code du cours : ");
                    String codeCours = scanner.nextLine();
                    Course course = new Course(name, codeCours, session)

                    RegistrationForm formular = new RegistrationForm(prenom, nom, email, matricule, course);

                    objectOutputStream.writeObject("Inscription "+ formular);

                    //Recevoir la reponse du serveur
                    String reponse = objectInputStream.readUTF();
                    System.out.println(reponse);
                }
}
