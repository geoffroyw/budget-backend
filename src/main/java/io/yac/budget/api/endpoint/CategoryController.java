package io.yac.budget.api.endpoint;

import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.CategoryConverter;
import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.domain.Category;
import io.yac.budget.recurring.transactions.client.exception.ResourceNotFoundException;
import io.yac.budget.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by geoffroy on 08/04/2016.
 */
@RestController
@RequestMapping(value = "/api/categories")
public class CategoryController {

    @Autowired
    CategoryConverter categoryConverter;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public List<CategoryResource> index() {

        return StreamSupport
                .stream(categoryRepository.findAll().spliterator(), false)
                .map(category -> categoryConverter.convertToResource(category)).collect(
                        Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public CategoryResource get(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Category category = categoryRepository.findOne(id);

        if (category == null) {
            throw new ResourceNotFoundException("No category found");
        }

        return categoryConverter.convertToResource(category);
    }


    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json",
                    consumes = "application/json")
    public CategoryResource create(@RequestBody CategoryResource toBeCreated) {
        Category category = categoryConverter.convertToEntity(toBeCreated);
        categoryRepository.save(category);
        return categoryConverter.convertToResource(category);
    }


}
