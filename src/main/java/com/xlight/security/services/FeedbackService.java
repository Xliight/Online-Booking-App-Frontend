package com.xlight.security.services;

import com.xlight.security.Repository.FeedbackRepository;
import com.xlight.security.Repository.ReservationRepository;
import com.xlight.security.Repository.RoomRepository;
import com.xlight.security.config.AuthenticatedUserUtil;
import com.xlight.security.model.Feedback;
import com.xlight.security.model.Room;

import com.xlight.security.model.User;
import com.xlight.security.payload.FeedbackRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final AuthenticatedUserUtil authenticatedUserUtil;

    @Transactional
    public Feedback saveFeedback(FeedbackRequest feedbackRequest) {
        User user = authenticatedUserUtil.getAuthenticatedUser();
        // Fetch the room based on roomId
        Room room = roomRepository.findById(feedbackRequest.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + feedbackRequest.getRoomId()));

        // Check if feedback already exists for the same email and room ID
        if (feedbackRepository.findByRoomIdAndEmail(feedbackRequest.getRoomId(), feedbackRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Feedback already exists for this room and user.");
        }

        if (!user.getEmail().equals(feedbackRequest.getEmail())) {
            throw new RuntimeException("Enter your email address.");
        }
        // Check if there is a reservation with the same room ID and email
        if (!reservationRepository.findByRoomIdAndUserEmail(feedbackRequest.getRoomId(), feedbackRequest.getEmail()).isPresent()) {
            throw new RuntimeException("No reservation found for this room and user.");
        }

        // Create Feedback entity from FeedbackRequest
        Feedback feedback = Feedback.builder()
                .room(room)
                .note(feedbackRequest.getNote())
                .content(feedbackRequest.getContent())
                .name(feedbackRequest.getName())
                .email(feedbackRequest.getEmail())
                .build();

        // Save Feedback to repository
        Feedback savedFeedback = feedbackRepository.save(feedback);
        List<Feedback> l=feedbackRepository.findAll();
        if (l == null || l.isEmpty()) {
                room.setAverageRating(0.0);
        }

        double sum = l.stream().mapToInt(Feedback::getNote).sum();
        room.setAverageRating((sum / l.size()));
        // Update the room's average rating
        System.out.println(room.getAverageRating());

        // Save the updated room to persist the new average rating
        roomRepository.save(room);

        return savedFeedback;
    }}

