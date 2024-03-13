package ru.practicum.main.compilations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @ManyToMany
    @JoinTable(name = "compilations_events",
    joinColumns = {@JoinColumn(name = "compilation_id")},
    inverseJoinColumns = {@JoinColumn(name = "event_id")})
    private List<Event> events;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean pinned;
    private String title;
}
