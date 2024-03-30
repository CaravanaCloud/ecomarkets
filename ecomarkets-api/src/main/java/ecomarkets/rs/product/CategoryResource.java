package ecomarkets.rs.product;

import java.util.List;

import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.stream.*;

import ecomarkets.core.domain.core.product.Product;
import ecomarkets.core.domain.core.product.category.Category;

@Path("/category")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class CategoryResource {

    @GET
    public List<Category> getCategories() {
        return Product.listAll(Sort.ascending("name"));
    }

    @POST
    public Response create(Category category) {
        Category.persist(category);
        
        return Response
        .status(Response.Status.CREATED)
        .entity(category)
        .build();
    }
 
    @Path("/{id}")
    @PUT
    public Category update(@PathParam("id") Long id, String name) {
        Category category = Category.findById(id);
        category.name = name;
        return category;
    }
    
    @Path("/{id}")
    @DELETE
    public void delete(@PathParam("id") Long id) {
        final List<Product> products = Product.find("category.id", id).list();
        if(products.size() > 0){
            throw new IllegalArgumentException("The category has these products: "
             + products.stream().map(p -> p.id.toString()).collect(Collectors.joining(", ")));
        }
    }

}