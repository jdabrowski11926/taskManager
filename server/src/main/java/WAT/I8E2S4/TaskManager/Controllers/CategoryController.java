package WAT.I8E2S4.TaskManager.Controllers;

import WAT.I8E2S4.TaskManager.Category.Category;
import WAT.I8E2S4.TaskManager.Category.CategoryRequestAndResponse;
import WAT.I8E2S4.TaskManager.Services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user/{username}/category")
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addCategory(@RequestBody CategoryRequestAndResponse category, @PathVariable String username){
        categoryService.addCategory(category, username);
    }

    @GetMapping
    public List<CategoryRequestAndResponse> getCategories(@PathVariable String username){
        return categoryService.getCategories(username);
    }

    @PutMapping("/{categoryName}")
    public void editCategory(@RequestBody Category editedCategory, @PathVariable String categoryName, @PathVariable String username){
        categoryService.editCategory(editedCategory, categoryName, username);
    }

    @DeleteMapping("/{categoryName}")
    public void deleteCategory(@PathVariable String categoryName, @PathVariable String username){
        categoryService.deleteCategory(categoryName, username);
    }
}