public class Pacman extends Character{
    
    private final int STARTPOSX = 11; //(01)
    private final int STARTPOSY = 11; //(02)
    private direction directionPending;
    
    public Pacman(){
        setPosX(STARTPOSX); 
        setPosY(STARTPOSY);
    }   
    
    public void updatePosition(){ //(03)
        
        checkDir(directionPending); //(04)
        
        if(getPosX() == 0 && getPosY() == 6 && getD() == direction.LEFT){ //(05)
            GameArea.map[6][0] = 'V';
            GameArea.currentMap[getPosY()][getPosX()] =  GameArea.map[getPosY()][getPosX()];
            setPosX(19);
            if(GameArea.map[6][19] == 'C') Game.score++; //(06)
        }else if(getPosX() == 19 && getPosY() == 6 && getD() == direction.RIGHT){ //(07)
            GameArea.map[6][19] = 'V';
            GameArea.currentMap[getPosY()][getPosX()] =  GameArea.map[getPosY()][getPosX()];
            setPosX(0);
            if(GameArea.map[6][0] == 'C') Game.score++;
        }else{ //(08)
            GameArea.map[getPosY()][getPosX()] = 'V';            
            if(getD() == direction.UP && getUpCell() != 'M'){ 
                GameArea.currentMap[getPosY()][getPosX()] =  GameArea.map[getPosY()][getPosX()];
                if(GameArea.map[getPosY()-1][getPosX()] == 'C') Game.score++;
                setPosY(getPosY()-1);            
            }else if(getD() == direction.DOWN && getDownCell() != 'M'){                
                GameArea.currentMap[getPosY()][getPosX()] =  GameArea.map[getPosY()][getPosX()];
                if(GameArea.map[getPosY()+1][getPosX()] == 'C') Game.score++;
                setPosY(getPosY()+1);            
            }else if(getD() == direction.LEFT && getLeftCell() != 'M'){
                GameArea.currentMap[getPosY()][getPosX()] =  GameArea.map[getPosY()][getPosX()];
                if(GameArea.map[getPosY()][getPosX()-1] == 'C') Game.score++;
                setPosX(getPosX() - 1);
            }else if(getD() == direction.RIGHT && getRightCell() != 'M'){      
                GameArea.currentMap[getPosY()][getPosX()] =  GameArea.map[getPosY()][getPosX()];
                if(GameArea.map[getPosY()][getPosX()+1] == 'C') Game.score++;
                setPosX(getPosX() + 1);
            }
        }
        GameArea.currentMap[getPosY()][getPosX()] = 'P'; //(09)
    }
    
    public void checkDir(direction d){ //(10)
        directionPending = d;
        
        if( directionPending == direction.UP && getUpCell() != 'M' ||
            directionPending == direction.DOWN && getDownCell() != 'M' ||
            directionPending == direction.LEFT && getLeftCell() != 'M' ||
            directionPending == direction.RIGHT && getRightCell() != 'M' ||
            directionPending == direction.STOP)
        {    
            setD(directionPending);
        }
        
    }
}
/*
Note
(01)-(02)
    Coordinate iniziali di default dove verrà istanziato Pacman.
(03)
    Aggiorna la posizione attuale di Pacman.
(04)-(10)
    Aggiorna la direzione scelta solo se è percorribile.
    Se non è percorribile allora la direzione scelta
    resterà in attesa fino a quando non lo diventerà.
(05)-(07)
    Gestione dei casi particolari dove la posizione di Pacman è al limite 
    dell'area di gioco, sfruttando l'effetto Pacman.
(06)
    Incrementa score se nella cella attuale c'è del cibo.
(08)
    Se Pacman non si trova nei casi particolari di prima,
    aggiorna la propria posizione continuando sulla direzione scelta.
(09)
    Aggiorna nella matrice la posizione di Pacman.
*/