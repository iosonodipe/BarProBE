package it.capstone.barpro.barpro.user;

import it.capstone.barpro.barpro.role.Role;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
public class User extends Person{

}
