package ru.practicum.main.evente;

import lombok.*;
import ru.practicum.main.category.Category;
import ru.practicum.main.evente.location.Location;
import ru.practicum.main.users.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "confirmed_Requests")
    private long confirmedRequests;
    @Column(name = "created_On")
    private LocalDateTime createdOn;
    private String description;
    @Column(name = "event_Date")
    private LocalDateTime eventDate;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;
    @ToString.Exclude
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "loc_Lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "loc_Lon"))
    })
    private Location location;
    private boolean paid;
    @Column(name = "participant_Limit")
    private long participantLimit;
    @Column(name = "published_On")
    private LocalDateTime publishedOn;
    @Column(name = "request_Moderation")
    private boolean requestModeration;
    private String state;
    private String title;
    private long views;
}
