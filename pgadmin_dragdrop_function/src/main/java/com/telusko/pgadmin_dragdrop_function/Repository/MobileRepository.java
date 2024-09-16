package com.telusko.pgadmin_dragdrop_function.Repository;

import com.telusko.pgadmin_dragdrop_function.Model.Mobile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface MobileRepository extends JpaRepository<Mobile, Long> {

    Page<Mobile> findByCreatedDateBetween(LocalDateTime startCreatedDate, LocalDateTime endCreatedDate, Pageable pageable);

    Page<Mobile> findByUpdatedDateBetween(LocalDateTime startUpdatedDate, LocalDateTime endUpdatedDate, Pageable pageable);


    Page<Mobile> findAll(Pageable pageable);
}

