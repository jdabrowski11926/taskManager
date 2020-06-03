package WAT.I8E2S4.TaskManager.User;

import lombok.Data;

@Data
public class UserEditPassword {
    private String oldPassword;
    private String newPassword;
}
