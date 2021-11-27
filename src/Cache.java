import java.io.*;

public class Cache implements Serializable { 
  
    public final int level, score, nGhost;
    public final boolean gameover, inGame;
    public final Pacman player;
    public final Ghost[] enemy;
    public final String nameField, scoreField;
    public final char[][] map, currentMap;
    
    public Cache(Game game){ //(01)
        //(01)
        level = game.level;
        score = game.score;
        nGhost = game.nGhost;
        gameover = game.gameover;
        inGame = game.inGame;
        player = game.player; //(02)
        enemy = game.enemy; //(03)
        
        //(04)
        nameField = game.GUI.getNameField().getText();
        scoreField = game.GUI.getScoreField().getText();
        
        //(05)
        map = game.gameArea.getMap();
        currentMap = game.gameArea.getCurrentMap();

    }
    
}

/*
Note
(01)
    Contiene lo stato della partita.
(02)
    Cache della classe Game.
(03)-(04)
    Cache per salvare le coordinate di Pacman e dei Ghost
    (ereditano entrambi la serializzabilità dalla classe padre Character).
(05)
    Cache della classe GUI.
(06)
    Cache della classe GameArea.
*/