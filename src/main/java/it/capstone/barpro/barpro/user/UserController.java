package it.capstone.barpro.barpro.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseProj>> findAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.findAll(page, size));
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody @Valid Request request) {
        return ResponseEntity.ok(userService.create(request));
    }

    @PostMapping("/admin")
    public ResponseEntity<Response> createAdmin(@RequestBody @Valid Request request) {
        return ResponseEntity.ok(userService.create(request, true));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody @Valid Request request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(userService.delete(id));
    }
}
