package com.xlight.security.Repository;

import com.xlight.security.enums.AvailabilityStatus;
import com.xlight.security.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(String roomNumber);

    List<Room> findByAvailabilityStatus(AvailabilityStatus availabilityStatus);

    @Query("SELECT r FROM Room r LEFT JOIN FETCH r.images")
    List<Room> findAllWithImages();

    @Query("SELECT r FROM Room r JOIN r.images i WHERE i.id = :imageId")
    List<Room> findByImageId(@Param("imageId") Long imageId);

    @Modifying
    @Query("UPDATE Room r SET r.mainImage = null WHERE r.id = :roomId")
    void removeMainImageFromRoom(@Param("roomId") Long roomId);
}