package Core.Moba.World;

import java.util.Objects;

/**
 * Représente une équipe dans le jeu (BLEU/Radiant ou ROUGE/Dire).
 * 
 * Concepts clés pour un débutants:
 * - Equivalent aux équipes dans Dota 2 ou League of Legends
 * - nom = "Radiant" ou "Dire" (les noms classiques des jeux MOBA)
 * - couleur = BLUE ou RED (utilisé pour distinguer les équipes visuellement)
 * - base = la base principale (CoreBase) qui si elle meurt fait perdre l'équipe
 * - fontaine = la zone de régénération où les alliés renaissance et heals
 * 
 * Dans ce jeu:
 * - Équipe BLEU commence en bas à gauche (position 5, 95)
 * - Équipe ROUGE commence en haut à droite (position 95, 5)
 */
public final class Equipe {
    private final String nom;
    private final TeamColor couleur;
    private final Base base;
    private final Fontaine fontaine;

    public Equipe(String nom, TeamColor couleur, Base base, Fontaine fontaine) {
        this.nom = Objects.requireNonNull(nom, "nom");
        if (nom.isBlank()) throw new IllegalArgumentException("nom cannot be blank");
        this.couleur = Objects.requireNonNull(couleur, "couleur");
        this.base = Objects.requireNonNull(base, "base");
        this.fontaine = Objects.requireNonNull(fontaine, "fontaine");
    }

    public String nom() {
        return nom;
    }

    public TeamColor couleur() {
        return couleur;
    }

    public Base base() {
        return base;
    }

    public Fontaine fontaine() {
        return fontaine;
    }
}

