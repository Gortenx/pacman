public class GameStyle {
    private static String pacmanColor, ghostColor, wallColor, foodColor, bgColor;

    public GameStyle(XMLConfigParameters cfg){ //(01)
        pacmanColor = cfg.getPacmanColor();
        ghostColor = cfg.getGhostColor();
        wallColor = cfg.getWallColor();
        foodColor = cfg.getFoodColor();
        bgColor = cfg.getBGColor();
    }
    
    public void setAreaStyle(){ //(02)
            String cellStyle = new String();
            for(int i=0; i<GameArea.getNUMROWS(); ++i){
                for(int j=0; j<GameArea.getNUMCOLS(); ++j){
                    switch(GameArea.getCurrentMap()[i][j]){
                        case 'M':
                            cellStyle = "-fx-background-color: "+wallColor+"; -fx-border-color: black;";              
                            break;
                        case 'C':
                            cellStyle = "-fx-background-color: "+foodColor+";";              
                            break;
                        case 'P':
                            cellStyle = "-fx-background-color: "+pacmanColor+"; -fx-border-color: black;";              
                            break;
                        case 'V':
                            cellStyle = "-fx-background-color: "+bgColor;              
                            break;
                        case 'G':
                            cellStyle = "-fx-background-color: "+ghostColor+"; -fx-border-color: black;";              
                            break;
                    }
                    setCellStyle(i,j,cellStyle);
                }
            } 
    }
    public void setCellStyle(int row, int col, String style){ //(03)
        GameArea.getArea().getChildren().get(row*GameArea.getNUMCOLS()+col).setStyle(style);
    }
}
/*
Note
(01)
    Carica i parametri di stile dal file di configurazione.
(02)
    Legge cosa sta nella matrice currentMap e aggiorna lo stile dell'area.
(03)
    Imposta lo stile della cella selezionata
*/