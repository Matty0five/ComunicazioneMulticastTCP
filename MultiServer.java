import java.io.*;
import java.net.*;
import java.util.*;

public class MultiServer  {
    private ArrayList<Socket> clientConnessi;
    private int storicoClient;
    private ServerSocket serverSocket;
    private int porta;

    public MultiServer(int porta){
        this.clientConnessi = new ArrayList<Socket>();
        this.storicoClient = 0;
        this.porta = porta;
        try {
            this.serverSocket = new ServerSocket(this.porta);
            System.out.println("Il server è in ascolto sulla porta : " + this.porta);
        } catch(BindException ex){
            System.err.println("Porta gia in uso.");
            System.err.println(ex.getMessage());
        }catch (IOException ex){
            System.err.println(ex.getMessage());
        }
    }
    
    public Socket attendi() {
        try {
            // aspetto che si connetta un client
            return serverSocket.accept();
        }
        catch (NullPointerException ex) {
            System.err.println(ex.getMessage());
            return null;
        } 
        catch (SocketException ex) {
            System.err.println("Errore nella fase di connessione con il client " + storicoClient);
            System.err.println(ex.getMessage());
            return null;
        } 
        catch (IOException ex) {
            System.err.println("Errore durante l'attesa della connessione");
            System.err.println(ex.getMessage());
            return null;
        }
    }

    public void start(){
        while(true){
            System.out.println(clientConnessi.size() + " client connessi");
            
            // istanzio il nuovo client che si è provato a connettere al server e lo aggiungo all'array dei client
            Socket nuovoClient = attendi();
            clientConnessi.add(nuovoClient);
            storicoClient++;
            System.out.println("Connessione stabilita con il Client " + storicoClient);
            ServerThread serverThread = new ServerThread(nuovoClient, storicoClient, this);
            serverThread.start();
        }
    }

    public void remove(Socket client){
        clientConnessi.remove(client);
    }
}

