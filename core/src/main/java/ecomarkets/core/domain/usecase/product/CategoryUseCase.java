package ecomarkets.core.domain.usecase.product;

import ecomarkets.core.domain.core.product.category.Category;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CategoryUseCase {

    @Transactional
    public Category newCategory(String name){
        Category result = Category.of(name);
        result.persist();
        return result;
    }

    @Transactional
    public Category changeCategory(Long id, String name){
        Category category = Category.findById(id);
        category.name = name;

        return category;
    }

    @Transactional
    public void deleteCategory(Long categoryId){
        Category category = Category.findById(categoryId);
        category.delete();
    }

}
