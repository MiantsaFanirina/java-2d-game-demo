package Engine.UI.HeroSelection;

import Core.Database.JsonDataProvider;
import Core.Database.JsonDataProviderFactory;
import Core.Database.model.Hero;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for hero selection: manages hero data, filtering, and selection state.
 */
public class HeroSelectionModel {
    
    private List<Hero> allHeroes;
    private List<Hero> filteredHeroes;
    private Hero selectedHero;
    private int selectedIndex = -1;
    private String selectedCategory = "All";
    private final String[] categories = {"All", "Force", "Agilité", "Intelligence"};
    
    public HeroSelectionModel() {
        loadHeroes();
        filterHeroes();
    }
    
    private void loadHeroes() {
        try {
            JsonDataProvider dataProvider = JsonDataProviderFactory.create();
            allHeroes = dataProvider.getAllHeroes();
            System.out.println("DEBUG: Loaded " + allHeroes.size() + " heroes");
        } catch (IOException e) {
            e.printStackTrace();
            allHeroes = new ArrayList<>();
        }
    }
    
    public void filterHeroes() {
        filteredHeroes = new ArrayList<>();
        for (Hero hero : allHeroes) {
            if (selectedCategory.equals("All")) {
                filteredHeroes.add(hero);
            } else {
                String heroCategory = switch (hero.getCategoryId()) {
                    case 1 -> "Force";
                    case 2 -> "Agilité";
                    case 3 -> "Intelligence";
                    default -> "Force";
                };
                if (heroCategory.equals(selectedCategory)) {
                    filteredHeroes.add(hero);
                }
            }
        }
        selectedIndex = -1;
        selectedHero = null;
    }
    
    public void selectCategory(String category) {
        selectedCategory = category;
        filterHeroes();
    }
    
    public void selectHero(int index) {
        if (index >= 0 && index < filteredHeroes.size()) {
            selectedIndex = index;
            selectedHero = filteredHeroes.get(index);
        } else {
            selectedIndex = -1;
            selectedHero = null;
        }
    }
    
    public Hero getSelectedHero() {
        return selectedHero;
    }
    
    public int getSelectedIndex() {
        return selectedIndex;
    }
    
    public List<Hero> getFilteredHeroes() {
        return filteredHeroes;
    }
    
    public List<Hero> getAllHeroes() {
        return allHeroes;
    }
    
    public String[] getCategories() {
        return categories;
    }
    
    public String getSelectedCategory() {
        return selectedCategory;
    }
    
    public int getTotalHeroCount() {
        return allHeroes.size();
    }
    
    public int getFilteredHeroCount() {
        return filteredHeroes.size();
    }
}