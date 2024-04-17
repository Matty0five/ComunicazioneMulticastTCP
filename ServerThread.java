import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread{

    private Socket clientSocket;
    private int numeroClient;
    private BufferedReader lettore;
    private BufferedWriter scrittore;
    private Scanner lettoreTastiera;
    private MultiServer multiServer;

    public ServerThread(Socket clientSocket, int numeroClient, MultiServer multiServer) {
        this.numeroClient = numeroClient;
        this.multiServer = multiServer;
        this.lettoreTastiera = new Scanner(System.in);
        try {
            this.clientSocket = clientSocket;
            if(clientSocket != null){
                scrittore = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                lettore = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));   
            }
        }
        catch (IOException ex) {
            System.err.println("Errore nella fase di connessione con il client " + numeroClient);
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void run(){
        comunica();
    }

    public synchronized void comunica(){
        while(!clientSocket.isClosed() ){
            leggi();
            scrivi();
        }
    }

    public void scrivi() {
        if(!clientSocket.isClosed()){
            System.err.println("Scrivi al client " + numeroClient);
            String messaggio = lettoreTastiera.nextLine();
            System.out.println("Messaggio inviato al client " + numeroClient + ": " + messaggio);
            try {
                scrittore.write(messaggio);
                scrittore.newLine();
                scrittore.flush(); 
                if(messaggio == null){
                    chiudi();
                }else if(messaggio.equalsIgnoreCase("chiudi") ){
                    chiudi();
                }
                
            } catch (IOException ex) {
                System.err.println( ex.getMessage());
            }
        }
    }

    public void leggi() {
        if(!clientSocket.isClosed()){
            String messaggioRicevuto = null;
            try {
                messaggioRicevuto = lettore.readLine(); // assegna alla variabile il messaggio ricevuto dal client
                System.out.println("Messaggio inviato dal client " + numeroClient + ": " + messaggioRicevuto); 
                if(messaggioRicevuto == null){
                    chiudi();
                }else if(messaggioRicevuto.equalsIgnoreCase("chiudi") ){
                    chiudi();
                }
            } catch (IOException ex) {
                 System.err.println(ex.getMessage());
            }
        }
       
    }

    public void chiudi() {
        try {
            // Chiudo il socket
            this.clientSocket.close();
            System.out.println("Connessione chiusa con il client " + numeroClient);
            multiServer.remove(clientSocket);
        
        } catch (IOException ex) {
            System.err.println("Errore");
            System.err.println(ex.getMessage());
        }
    }

}

