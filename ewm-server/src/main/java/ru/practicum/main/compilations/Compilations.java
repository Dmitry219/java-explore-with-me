package ru.practicum.main.compilations;

import lombok.*;
import ru.practicum.main.evente.Event;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "compilations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Compilations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "compilations_events",
    joinColumns = {@JoinColumn(name = "compilation_id")},
    inverseJoinColumns = {@JoinColumn(name = "event_id")})
    private List<Event> events;
    private boolean pinned;
    private String title;
}
