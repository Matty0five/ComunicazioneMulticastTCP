import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private String nome;
    private String colore;
    private Socket s;
    private BufferedReader lettore;            // lettore del messaggio del client
    private BufferedWriter scrittore;          // scrittore del messaggio per il client
    private boolean connesso = true;
    private Scanner lettoreMessaggio;          // buffer per l'input da tastiera
    
    public Client(String nomeDefault, String coloreDefault) {
        this.nome = nomeDefault;
        this.colore = coloreDefault;
        this.lettoreMessaggio = new Scanner(System.in);
    }
    
    public void connetti(String nomeServer, int portaServer){
        try {
            s = new Socket(nomeServer, portaServer);
            System.out.println("C1 - Connessione con il server instaurata (fase di connessione con il server)");
            scrittore = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            lettore = new BufferedReader(new InputStreamReader(s.getInputStream()));
            connesso = true;
        } catch (UnknownHostException ex) {
            System.err.println("C1 - Host non risolto");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println("C1 - Errore con la connessione con il server");
            ex.printStackTrace();
        }
    }
    
    public void scrivi(){
        String messaggio;              // il messaggio da mandare al server

        try {
            // richiesta dell'inserimento del messaggio all'utente
            System.out.println("Inserisci un messaggio per il server: ");
            messaggio = lettoreMessaggio.nextLine();
            
            // trasmissione del messaggio al server
            scrittore.write(messaggio);
            scrittore.newLine();
            scrittore.flush();
            
            if (messaggio.equalsIgnoreCase("chiudi")) {
                connesso = false;
                chiudi();
            }
            
        } catch (IOException ex) {
            System.err.println("Errore nella lettura da tastiera.");
            ex.printStackTrace();
        }
    }

    public void leggi(){
        if(connesso){
            String messaggioServer;         // messaggio ricevuto dal server
                
            try{
                // lettura della risposta del server
                messaggioServer = lettore.readLine();

                // gestione dei casi in base al contenuto del messaggio
                if(messaggioServer.equals("chiudi")){
                    System.out.println("Il server ha chiuso la comunicazione con il client.");
                    chiudi();
                    connesso = false;
                }else if(messaggioServer.equals("termina")){
                    System.out.println("Il server è stato terminato");
                    chiudi();
                    connesso = false;
                }else{
                    System.out.println("Dal server: " + messaggioServer);
                }
    
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public void comunica() {
        while (connesso) {
            scrivi();
            leggi();
        }
    }

    public void chiudi(){
        System.out.println("Chiusura della comunicazione effettuata e client terminato.");
        try{
            s.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public boolean connesso(){
        return connesso;
    }
    
}