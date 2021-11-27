import java.io.*;
import java.net.*;

public class ServerActivityLog {
    
    public static void main(String[] args){
        String result;
        try(ServerSocket servs = new ServerSocket(8080);){
            while(true){ //(01)
                try(Socket sd = servs.accept();
                    DataInputStream din = new DataInputStream(sd.getInputStream());
                ){
                    result = (String)din.readUTF();
                    if(XMLValidator.validate(result, "files/log.xsd", false)){  //(02)
                        System.out.println(result);
                        writeLog(result);
                    }
                }           
            }
        }catch(Exception e) {e.printStackTrace();}
        
    }
    
    public static void writeLog(String log){ //(03)
        try(BufferedWriter out = new BufferedWriter(new FileWriter("files/log.xml", true))){ //(04)
            out.write(log);
        }catch(IOException e){
           System.err.println(e.getMessage());
        }
        
    }
}

/*
Note
(01)
    Loop che consente al server di restare in ascolto
(02)
    Valida il log ricevuto in formato XML attraverso la classe XMLValidator 
    e lo schema XSD fornito. Se restituisce 'true', allora scrive sulla console
    e sul file di log (non ci sono stati errori durante la validazione).
(03)
    Scrive sul file di log la stringa indicata.
(04)
    Concatena le stringhe di log al file XML. 
    Se il file di log non esiste allora viene creato.
    Il parametro 'true' nel costruttore di FileWriter consente la concatenazione.
    https://docs.oracle.com/javase/7/docs/api/java/io/BufferedWriter.html
    https://docs.oracle.com/javase/7/docs/api/java/io/FileWriter.html
*/