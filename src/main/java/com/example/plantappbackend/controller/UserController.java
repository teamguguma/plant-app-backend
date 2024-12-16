import com.example.plantappbackend.model.User;
import com.example.plantappbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 로그인 또는 사용자 생성
    @PostMapping("/login")
    public ResponseEntity<User> loginOrCreateUser(
            @RequestParam String client_uuid,
            @RequestParam(required = false) String nickname
    ) {
        User user = userService.findOrCreateUser(client_uuid, nickname);
        return ResponseEntity.ok(user);
    }

    // 닉네임 변경
    @PutMapping("/nickname")
    public ResponseEntity<User> updateNickname(
            @RequestParam String client_uuid,
            @RequestParam String newNickname
    ) {
        User updatedUser = userService.updateNickname(client_uuid, newNickname);
        return ResponseEntity.ok(updatedUser);
    }

    // 사용자 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String client_uuid) {
        userService.deleteUser(client_uuid);
        return ResponseEntity.ok("User successfully deleted.");
    }
}