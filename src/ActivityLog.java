import com.thoughtworks.xstream.XStream;
import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class ActivityLog {
    private String appName, eventName, IPAddress, currentTime;
    
    public ActivityLog(){} //(1)
    private ActivityLog(String event){ //(2)
        appName = "Pacman";
        eventName = event;
        IPAddress = "localhost";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();
        currentTime = dtf.format(now);
    }
    public void send(String event) { //(03)
        ActivityLog log = new ActivityLog(event); //(04)
        String x = (new XStream()).toXML(log);
        try ( DataOutputStream dout = new DataOutputStream((new Socket("localhost",8080)).getOutputStream())){ 
            dout.writeUTF(x+"\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
/*
Note
(01)
    Permette di istanziare un istanza di ActivityLog per poter usare 
    successivamente il metodo send della classe.
(02)
    Costruttore utilizzato dal metodo send per inizializzare il log.
(03)
    Invia il log attività del singolo evento al ServerActivityLog in formato XML.
(04)
    Crea la nuova istanza di ActivityLog da inviare e lo inizializza con l'evento.
*/