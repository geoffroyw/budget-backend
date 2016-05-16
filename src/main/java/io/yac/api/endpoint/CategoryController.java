package io.yac.api.endpoint;

import io.yac.api.converter.impl.CategoryConverter;
import io.yac.api.exceptions.ResourceNotFoundException;
import io.yac.api.resources.CategoryResource;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.core.domain.Category;
import io.yac.core.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<CategoryResource> index() {

        return categoryRepository.findByOwner(authenticationFacade.getCurrentUser()).stream()
                .map(category -> categoryConverter.convertToResource(category)).collect(
                        Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody CategoryResource get(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Category category = categoryRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (category == null) {
            throw new ResourceNotFoundException("No category found");
        }

        return categoryConverter.convertToResource(category);
    }


    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json",
                    consumes = "application/json")
    public @ResponseBody CategoryResource create(@RequestBody CategoryResource toBeCreated) {
        Category category = categoryConverter.convertToEntity(toBeCreated, null);
        category.setOwner(authenticationFacade.getCurrentUser());
        categoryRepository.save(category);
        return categoryConverter.convertToResource(category);
    }


}
