package ru.practicum.main.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByEvent_Id(long eventId);

    List<Request> findAllByIdInAndStatus(List<Long> requestIds, String status);

    List<Request> findAllByIdIn(List<Long> requestIds);

    List<Request> findAllByRequester_Id(Long userId);
}
