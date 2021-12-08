package app.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "university_table")
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20, unique = true)
    private String universityName;
    @Column(length = 20)
    private String creationDate;
    @OneToMany(mappedBy = "university", fetch = FetchType.LAZY)
    private List<Student> students;
}