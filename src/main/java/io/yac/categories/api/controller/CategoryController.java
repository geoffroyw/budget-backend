package io.yac.categories.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.categories.api.View;
import io.yac.categories.domain.Category;
import io.yac.categories.repository.CategoryRepository;
import io.yac.common.api.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by geoffroy on 08/04/2016.
 */
@RestController
@RequestMapping(value = "/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository,
                              AuthenticationFacade authenticationFacade) {
        this.categoryRepository = categoryRepository;
        this.authenticationFacade = authenticationFacade;
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary.class)
    public @ResponseBody List<Category> index() {
        return categoryRepository.findByOwner(authenticationFacade.getCurrentUser());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary.class)
    public @ResponseBody Category get(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Category category = categoryRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (category == null) {
            throw new ResourceNotFoundException("No category found");
        }

        return category;
    }


    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json",
                    consumes = "application/json")
    @JsonView(View.Summary.class)
    public @ResponseBody Category create(@RequestBody Category category) {
        category.setOwner(authenticationFacade.getCurrentUser());
        categoryRepository.save(category);
        return category;
    }


}
