package Core.Moba.World;

/**
 * Couleur de l'équipe - utilisée pour distinguer les alliés des ennemis.
 * 
 * Concepts clés pour un débutants:
 * - BLUE = Équipe "Radiant" (commence en bas à gauche de la carte)
 * - RED = Équipe "Dire" (commence en haut à droite de la carte)
 * 
 * C'est crucial pour:
 * - Savoir qui est un allié vs un ennemi
 * - Afficher les bonne couleurs (bleu pour alliés, rouge pour ennemis)
 * - Les tours n'attaquent que les ennemis d'équipe différente
 */
public enum TeamColor {
    BLUE,
    RED
}

