package WAT.I8E2S4.TaskManager.Responses;

import WAT.I8E2S4.TaskManager.Model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private long id;
    private String name;
    private String description;
    private String user;

    public CategoryResponse(Category category){
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.user = category.getUser().getUsername();
    }

    public static List<CategoryResponse> makeList(List<Category> categoryList){
        ArrayList<CategoryResponse> result = new ArrayList<>(categoryList.size());
        for(Category category: categoryList){
            result.add(new CategoryResponse(category));
        }
        return result;
    }
}