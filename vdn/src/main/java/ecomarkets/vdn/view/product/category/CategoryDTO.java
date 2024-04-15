package ecomarkets.vdn.view.product.category;

import jakarta.validation.constraints.NotEmpty;

public class CategoryDTO {

    private Long id;

    @NotEmpty
    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
