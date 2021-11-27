import java.io.*;

public class CacheManager {
    
    public static void save(Game game){ //(01)
        try(
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/cache.bin"))) {
            oos.writeObject(new Cache(game));
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static Cache load(){ //(02)
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/cache.bin"))) { 
            return (Cache)ois.readObject(); 
        } catch(IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}

/*
Note
(01)
    Serializza la classe Cache su file binario.
(02)
    Deserializza la classe Cache su file binario e la restituisce al chiamante.
*/