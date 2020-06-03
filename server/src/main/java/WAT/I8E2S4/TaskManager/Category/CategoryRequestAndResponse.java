package WAT.I8E2S4.TaskManager.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestAndResponse {
    private long id;
    private String name;
    private String description;
    private String user;

    public CategoryRequestAndResponse(Category category){
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.user = category.getUser().getUsername();
    }

    public static List<CategoryRequestAndResponse> makeList(List<Category> categoryList){
        ArrayList<CategoryRequestAndResponse> result = new ArrayList<>(categoryList.size());
        for(Category category: categoryList){
            result.add(new CategoryRequestAndResponse(category));
        }
        return result;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUser() {
        return this.name;
    }

    public long getId() {
        return this.id;
    }
}