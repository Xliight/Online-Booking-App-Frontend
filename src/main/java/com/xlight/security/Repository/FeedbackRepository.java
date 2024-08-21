package com.xlight.security.Repository;

import com.xlight.security.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByRoomId(Long roomId);
    Optional<Feedback> findByRoomIdAndEmail(Long roomId, String email);


}