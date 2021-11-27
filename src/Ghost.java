public class Ghost extends Character{    
    private int nAvailableDirections; //(01)
    private boolean[] availableDirections; //(02)
    
    public Ghost(){ 
        availableDirections = new boolean[4];
        int randomX = (int)(Math.ceil(Math.random() * 18)); //(03)
        int randomD = (int)(Math.floor(Math.random() * 4)); //(04)
        setPosX(randomX);
        setPosY(4); //(05)
        setD(direction.values()[randomD]); 
    }
    
    public void updateDirection(){ //(06)  
        if(getD() == direction.UP && getUpCell() == 'P') //(07)
            setD(direction.UP);
        else if(getD() == direction.DOWN && getDownCell() == 'P')
            setD(direction.DOWN);
        else if(getD() == direction.LEFT && getLeftCell() == 'P')
            setD(direction.LEFT);
        else if(getD() == direction.RIGHT && getRightCell() == 'P')
            setD(direction.RIGHT);
        else{ 
            updateAvailableDirections(); 
            if(nAvailableDirections == 1){ //(08)
                for(int i = 0; i < 4; ++i){        
                    if(availableDirections[i]){
                        setD(direction.values()[i]);
                        break;
                    }
                }
            }else if(nAvailableDirections == 2){ //(09)
                    if(getD() == direction.UP && (getLeftCell() == 'C' || getLeftCell() == 'V' || getLeftCell() == 'P') && (getUpCell() == 'M' || getUpCell() == 'G'))
                       setD(getRelativeLeft());
                    else if(getD() == direction.UP && (getRightCell() == 'C' || getRightCell() == 'V' || getRightCell() == 'P' ) && (getUpCell() == 'M' || getUpCell() == 'G'))
                       setD(getRelativeRight());
                    else if(getD() == direction.DOWN && (getLeftCell() == 'C' || getLeftCell() == 'V' || getLeftCell() == 'P') && (getDownCell() == 'M' || getDownCell() == 'G'))
                       setD(getRelativeRight());
                    else if(getD() == direction.DOWN && (getRightCell() == 'C' || getRightCell() == 'V' || getRightCell() == 'P') && (getDownCell() == 'M' || getDownCell() == 'G'))
                       setD(getRelativeLeft());   
                    else if(getD() == direction.LEFT && (getDownCell() == 'C' || getDownCell() == 'V' || getDownCell() == 'P') && (getLeftCell() == 'M' || getLeftCell() == 'G'))
                       setD(getRelativeLeft());
                    else if(getD() == direction.LEFT && (getUpCell() == 'C' || getUpCell() == 'V' || getUpCell() == 'P') && (getLeftCell() == 'M' || getLeftCell() == 'G'))
                       setD(getRelativeRight());   
                    else if(getD() == direction.RIGHT && (getUpCell() == 'C' || getUpCell() == 'V' || getUpCell() == 'P') && (getRightCell() == 'M' || getRightCell() == 'G'))
                       setD(getRelativeLeft());
                    else if(getD() == direction.RIGHT && (getDownCell() == 'C' || getDownCell() == 'V' || getDownCell() == 'P') && (getRightCell() == 'M' || getRightCell() == 'G'))
                       setD(getRelativeRight());   
            }else if(nAvailableDirections > 2){ //(10)
                int r = (int)(Math.ceil(Math.random() * nAvailableDirections)); 
                for(int i = 0; i < 4; ++i){
                    if(availableDirections[(r+i)%4])
                        setD(direction.values()[(r+i)%4]);    
                }
            }
        }
    }
    public void updateAvailableDirections(){ //(11)
        nAvailableDirections = 0;
        
        if(getUpCell() == 'C' || getUpCell() == 'V' || getUpCell() == 'P') {
            nAvailableDirections++;
            availableDirections[0] = true; 
        }else 
            availableDirections[0] = false; 
        if(getDownCell() == 'C' || getDownCell() == 'V' || getDownCell() == 'P') {
            nAvailableDirections++;
            availableDirections[1] = true;
        }else
            availableDirections[1] = false; 
        if(getLeftCell() == 'C' || getLeftCell() == 'V' ||  getLeftCell() == 'P') {
            nAvailableDirections++;
            availableDirections[2] = true; 
        }else
            availableDirections[2] = false; 
        if(getRightCell() == 'C' || getRightCell() == 'V' || getRightCell() == 'P') {
            nAvailableDirections++;
            availableDirections[3] = true; 
        }else
            availableDirections[3] = false; 
    }
    
    public void updatePosition(){ //(12)
        GameArea.currentMap[getPosY()][getPosX()] = GameArea.map[getPosY()][getPosX()];

        if(getPosX() == 0 && getPosY() == 6 && getD() == direction.LEFT){ //(13)
            GameArea.currentMap[6][0] = GameArea.map[6][0];
            setPosX(19);
        }else if(getPosX() == 19 && getPosY() == 6 && getD() == direction.RIGHT){ //(14)
            GameArea.currentMap[6][19] = GameArea.map[6][19];
            setPosX(0);
        }else{ //(15)
           if(getD() == direction.UP && getUpCell() != 'M' && getUpCell() != 'G'){
                setPosY(getPosY() - 1);
            }else if(getD() == direction.DOWN && getDownCell() != 'M' && getDownCell() != 'G'){
                setPosY(getPosY() + 1);
            }else if(getD() == direction.LEFT && getLeftCell() != 'M' && getLeftCell() != 'G'){
                setPosX(getPosX() - 1);
            }else if(getD() == direction.RIGHT && getRightCell() != 'M' && getRightCell() != 'G'){     
                setPosX(getPosX() + 1);
            }
            
            updateDirection(); //(16)
        }
        GameArea.currentMap[getPosY()][getPosX()] = 'G'; //(17)
    }
}

/*
(01)
    Restituisce il numero di direzioni percorribili dal personaggio,
    cioè quelle dove non è presente un muro.
(02)
    Restituisce quali sono le direzioni percorribili.
    [0]->UP
    [1]->DOWN
    [2]->LEFT
    [3]->RIGHT
(03)
    Calcola randomicamente la colonna dove verrà istanziato il Ghost.
    Le colonne possibili vanno da 1 a 18.
(04)
    Calcola randomicamente una direzione.
(05)
    Tutti i Ghost verranno istanziati nella riga di indice 4.
(06)
    1- Controlla quali direzioni sono percorribili
    2- Sceglie a caso una di quelle direzioni.
(07)
    Se la direzione che il Ghost sta seguendo corrisponde a quella attuale di Pacman, 
    allora continua su quella...
(08)
    ...se esiste solo una direzione percorribile, percorri quella...
    (questo evita di dover aspettare più frame se la direzione scelta non è percorribile)
(09)
    ...altrimenti se esistono solo 2 direzioni percorribili (via a L), sceglie l'unica
    direzione non ancora percorsa...
(10)
    ...altrimenti se le direzioni percorribili sono più di 2, sceglie una direzione random.
    Se la direzione random scelta non è percorribile allora sceglie quella adiacente. 
(11)
    Aggiorna il contatore nAvailableDirections e le direzioni in AvailableDirections.
(12)
    Aggiorna la posizione attuale del Ghost nella mappa.
(13)-(14)
    Gestione dei casi particolari dove la posizione del Ghost è al limite 
    dell'area di gioco, sfruttando l'effetto Pacman.
(15)
    Se il Ghost non si trova nei casi particolari di prima,
    aggiorna la propria posizione continuando sulla direzione scelta.
(16)
    Calcola una nuova direzione.
(17)
    Aggiorna nella matrice la posizione del Ghost.
*/
