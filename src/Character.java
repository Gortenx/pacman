import java.io.*;

public abstract class Character implements Serializable{
    private int posX;
    private int posY;
    public enum direction{UP, DOWN, LEFT, RIGHT, STOP} //(01)
    private direction relativeDir;
    private direction relativeOpp;
    private direction relativeLeft;
    private direction relativeRight;
    
    public void setD(direction d){ // (02)
        relativeDir = d;
        
        switch(relativeDir){
            case UP:
                relativeOpp = direction.DOWN;
                relativeLeft = direction.LEFT;
                relativeRight = direction.RIGHT;
                break;   
           case DOWN:
                relativeOpp = direction.UP;
                relativeLeft = direction.RIGHT;
                relativeRight = direction.LEFT;
                break;
           case LEFT:
                relativeOpp = direction.RIGHT;
                relativeLeft = direction.DOWN;
                relativeRight = direction.UP;
                break;
            case RIGHT:
                relativeOpp = direction.LEFT;
                relativeLeft = direction.UP;
                relativeRight = direction.DOWN;          
                break;
        }
    }
    public direction getD(){ return relativeDir; } //(03)
    public direction getOppositeD(){ return relativeOpp;}  
    public direction getRelativeLeft(){ return relativeLeft;} 
    public direction getRelativeRight(){ return relativeRight;} 
    public int getPosX(){return posX;} //(04)
    public int getPosY(){return posY;}
    public void setPosX(int x){posX = x;} //(05)
    public void setPosY(int y){posY = y;}
    public char getUpCell(){ return GameArea.getCurrentMap()[posY - 1][posX];} //(06)
    public char getDownCell(){ return GameArea.getCurrentMap()[posY + 1][posX];}
    public char getLeftCell(){ 
        if(posY == 6 && posX == 0)  //(07)
            return GameArea.getCurrentMap()[6][19];
        return GameArea.getCurrentMap()[posY][posX - 1];
    }
    public char getRightCell(){   
        if(posY == 6 && posX == 19) //(08)
            return GameArea.getCurrentMap()[6][0];
        return GameArea.getCurrentMap()[posY][posX + 1];
    }
}

/*
Note
(01)
    Struttura che rappresenta le direzioni disponibili.
(02)
    Imposta la direzione principale del personaggio rispetto a quella assoluta dello schermo 
    e le direzioni relative (laterali e opposta) rispetto a quella principale.
    Questo permette di avere subito a disposizione informazioni sulla direzione senza doverla ricalcolare.
(03)
    Restituisce la direzione principale, opposta e quelle laterali (relative alla principale).
(04)
    Restituisce la posizione attuale in coordinate x e y.
(05)
    Imposta la posizione in coordinate x e y.
(06)
    Restituisce il contenuto delle celle adiacenti a quelle attualmente occupate.
(07)-(08)
    Gestione dei casi particolari dove le celle coinvolte sono al limite dell'area di gioco,
    sfruttando l'effetto Pacman.
*/
