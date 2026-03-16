package Core.Moba.World;

/**
 * Les voies (lanes) du jeu - comme dans tous les jeux MOBA.
 * 
 * Concepts clés pour un débutants:
 * - TOP = voie du haut (diagonale allant du coin bas-gauche vers haut-droit)
 * - MID = voie du milieu (traverse la carte en diagonale)
 * - BOT = voie du bas (diagonale allant du coin haut-gauche vers bas-droit)
 * 
 * Dans la carte:
 * - La voie du haut est à gauche de la carte
 * - La voie du bas est à droite de la carte
 * - La voie du milieu est au centre
 * 
 * Cela corresponds aux lanes classiques dans Dota/LoL: top, mid, bottom
 */
public enum Voie {
    TOP,
    MID,
    BOT
}

