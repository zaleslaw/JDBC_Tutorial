package spring_jdbc;

/**
 * Created by Alexey_Zinovyev on 31-Oct-16.
 */
public class Product {

    private String name;
    private int weight;
    private String category;

    public Product(String name, int weight, String category) {
        this.name = name;
        this.weight = weight;
        this.category = category;
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", category='" + category + '\'' +
                '}';
    }
}
