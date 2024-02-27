package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats,Long> {
    @Query(value = "SELECT new ru.practicum.Stats(app, uri, COUNT(DISTINCT ip))" +
            "FROM Stats " +
            "WHERE timestamp BETWEEN :start AND :end " +
            "AND uri IN :uris " +
            "GROUP BY app, uri " +
            "ORDER BY 3 DESC")
    List<Stats> getByDistinctIpAndTimestampAfterAndTimestampBeforeAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.Stats(app, uri, COUNT(ip))" +
            "FROM Stats " +
            "WHERE timestamp BETWEEN :start AND :end " +
            "AND uri IN :uris " +
            "GROUP BY app, uri " +
            "ORDER BY 3 DESC")
    List<Stats> getByTimestampAfterAndTimestampBeforeAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.Stats(app, uri, COUNT(DISTINCT ip))" +
            "FROM Stats " +
            "WHERE timestamp BETWEEN :start AND :end " +
            "GROUP BY app, uri " +
            "ORDER BY 3 DESC")
    List<Stats> getByDistinctIpAndTimestampAfterAndTimestampBefore(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.Stats(app, uri, COUNT(ip))" +
            "FROM Stats " +
            "WHERE timestamp BETWEEN :start AND :end " +
            "GROUP BY app, uri " +
            "ORDER BY 3 DESC")
    List<Stats> getByTimestampAfterAndTimestampBefore(LocalDateTime start, LocalDateTime end);
}
