package ru.practicum.main.request;

import lombok.*;
import ru.practicum.main.evente.Event;
import ru.practicum.main.users.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "event", nullable = false)
    private Event event;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "requester", nullable = false)
    private User requester;
    private String status;
}
