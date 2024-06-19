package it.capstone.barpro.barpro.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
