package main.java.Classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a category with a name and a color.
 */

public class Category implements Serializable {
    private String name;
    private String color;


    private static Set<Category> predefinedCategories = new HashSet<>();



    static {       //La liste des categories predefinies
        predefinedCategories.add(new Category("Studies", "#0000FF")); // Blue
        predefinedCategories.add(new Category("Work", "#00FF00")); // Green
        predefinedCategories.add(new Category("Hobby", "#FFFF00")); // Yellow
        predefinedCategories.add(new Category("Sport", "#FF0000")); // Red
        predefinedCategories.add(new Category("Health", "#FF00FF")); // Magenta
    }

    /************* Constructor *******************/
    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    /************ Getters and Setters ************/
    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color returnColor ()
    {
        return Color.web(this.color);
    }

    /**************** Methods ***********************/
    public static void addCategory(Category category) { /***********Add a new Catgory **********/
        predefinedCategories.add(category);
    }


    public static Category findCategoryByName(String categoryName) { /*********** trouver la categorie par son nom *********/
        for (Category category : predefinedCategories) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }
        return null; // Category not found
    }

    /**
     * @param categoryListView Afficher la liste des categories dans une list view
     */
    public static void showCategories(ListView<Category> categoryListView) {
        categoryListView.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>() {
            @Override
            public ListCell<Category> call(ListView<Category> listView) {
                return new ListCell<Category>() {
                    @Override
                    protected void updateItem(Category category, boolean empty) {
                        super.updateItem(category, empty);
                        if (category != null && !empty) {
                            Rectangle colorRect = new Rectangle(20, 20);
                            String categoryColor = category.getColor();
                            Color fillColor = Color.web(categoryColor);
                            colorRect.setFill(fillColor);
                            setText(category.getName());
                            setGraphic(colorRect);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        categoryListView.getItems().setAll(predefinedCategories);
    }

    /**
     * @param categoryChoiceBox show categories in choicebox
     */
    public static void showCategories(ChoiceBox<String> categoryChoiceBox) {
        ObservableList<String> categoryNames = FXCollections.observableArrayList();
        for (Category category : predefinedCategories) {
            categoryNames.add(category.getName());
        }
        categoryChoiceBox.setItems(categoryNames);
    }


    /****** Implement equals() and hashCode() methods to ensure uniqueness in the set *******/
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Category category = (Category) obj;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }



}
