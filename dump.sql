DROP DATABASE IF EXISTS `pacman`; 
CREATE DATABASE `pacman`;
USE `pacman`;

CREATE TABLE players(
	id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	pName VARCHAR(20) NOT NULL
);

CREATE TABLE ranking(
	id INT UNSIGNED NOT NULL AUTO_INCREMENT KEY, 
	player_id INT UNSIGNED,
	score INT UNSIGNED NOT NULL,
    FOREIGN KEY (player_id) REFERENCES players(id)
);

INSERT INTO `players` VALUES (1,'BestPlayer'),(2,'ProPlayer'),(3,'Pierciangelo'),(4,'CompetitivePlayer'),(5,'Gortenx'),(6,'RandomName'),(7,'GhostBuster'),(8,'CiProvo'),(9,'NonVolevoGiocarci'),(10,'Loser');
INSERT INTO `ranking` VALUES (1,1,3563),(2,2,3456),(3,3,3302),(4,4,2950),(5,5,1032),(6,6,802),(7,7,402),(8,8,203),(9,9,101),(10,10,2);

DROP PROCEDURE IF EXISTS getRanking;

DELIMITER $$
CREATE PROCEDURE getRanking(IN K INTEGER)
BEGIN
	SELECT pName, score
	FROM players p INNER JOIN ranking r ON p.id = r.player_id
	ORDER BY score DESC
	LIMIT K;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS saveScore;

DELIMITER $$
CREATE PROCEDURE saveScore(IN player_name_IN VARCHAR(20), IN score_IN INT(10))
BEGIN
	DECLARE playerID INT; -- se -1 allora il player non è stato trovato
	DECLARE playerScore INT;

	SET playerID = 	(
						SELECT id 
						FROM players
						WHERE pName = player_name_IN
					);

	IF playerID IS NULL THEN -- se il player non esiste viene inserito insieme al suo score
		INSERT INTO players	(pName)
		VALUES (player_name_IN);
		
		SET playerID = LAST_INSERT_ID(); -- LAST_INSERT_ID() permette di ricavare l'ultimo ID generato da un AUTO_INCREMENT
	
		INSERT INTO ranking (player_id, score)
		VALUES (playerID, score_IN);

	ELSE
		SET playerScore = 	(
								SELECT score
								FROM ranking
								WHERE playerID = player_id
							);

		IF playerScore < score_IN THEN -- se il player esiste e il suo score attuale è maggiore a quello precedente allora sovrascrive
			SET playerID = 	(
								SELECT id
								FROM players
								WHERE player_name_IN = pName
							);

			DELETE FROM ranking
			WHERE playerID = player_id;

			INSERT INTO ranking (player_id, score)
			VALUES (playerID, score_IN);
			
		END IF;
		-- se il player_name esiste già e lo score è minore di quello presente allora non sovrascrive lo score precedente

	END IF;


END$$
DELIMITER ;
