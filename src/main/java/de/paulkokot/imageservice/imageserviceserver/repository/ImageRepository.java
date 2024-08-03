package de.paulkokot.imageservice.imageserviceserver.repository;

import de.paulkokot.imageservice.imageserviceserver.model.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {

    List<Image> findImagesByUploadedAtBefore(LocalDateTime timestamp);

    Page<Image> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Image> findImageByImageIdAndDeletedAtIsNull(UUID imageId);

}