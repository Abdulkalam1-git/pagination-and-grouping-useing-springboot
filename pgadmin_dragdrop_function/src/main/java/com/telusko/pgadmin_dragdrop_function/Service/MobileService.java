package com.telusko.pgadmin_dragdrop_function.Service;

import com.telusko.pgadmin_dragdrop_function.Model.Mobile;
import com.telusko.pgadmin_dragdrop_function.Repository.MobileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MobileService {

    @Autowired
    private MobileRepository mobileRepository;

    // add data
    public Mobile saveMobile(Mobile mobile) {
        return mobileRepository.save(mobile);
    }

    // Method to filter by created date range and apply pagination
    public Page<Mobile> getMobilesByCreatedDateRange(LocalDateTime startCreatedDate, LocalDateTime endCreatedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mobileRepository.findByCreatedDateBetween(startCreatedDate, endCreatedDate, pageable);
    }

    // Method to filter by updated date range and apply pagination
    public Page<Mobile> getMobilesByUpdatedDateRange(LocalDateTime startUpdatedDate, LocalDateTime endUpdatedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mobileRepository.findByUpdatedDateBetween(startUpdatedDate, endUpdatedDate, pageable);
    }

    //updates
    public Mobile updateMobile(Long id, Mobile updatedMobile) {
        return mobileRepository.findById(id).map(existingMobile -> {
                    existingMobile.setMobileName(updatedMobile.getMobileName());
                    existingMobile.setPriority(updatedMobile.getPriority());
                    existingMobile.setUpdatedDate(LocalDateTime.now()); // set updated date to now
                    return mobileRepository.save(existingMobile);
                })
                .orElseThrow(() -> new RuntimeException("Mobile not found with id: " + id));
    }


    public Page<Mobile> getAllMobilesPaginated(int page, int size) {
        // Fetch all mobiles from the repository
        List<Mobile> allMobiles = mobileRepository.findAll();

        // Sort all mobiles by priority
        List<Mobile> sortedList = allMobiles.stream()
                .sorted((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()))
                .toList();

        // Apply pagination
        int start = Math.min((int) PageRequest.of(page, size).getOffset(), sortedList.size());
        int end = Math.min((start + size), sortedList.size());
        List<Mobile> pagedList = sortedList.subList(start, end);

        // Create and return the Page object
        return new PageImpl<>(pagedList, PageRequest.of(page, size), sortedList.size());
    }


    public Map<LocalDate, List<Mobile>> getMobilesGroupedByDate() {
        // Fetch all mobiles
        List<Mobile> allMobiles = mobileRepository.findAll();

        // Group by date part of createdDate (ignoring time)
        return allMobiles.stream()
                .collect(Collectors.groupingBy(mobile -> mobile.getCreatedDate().toLocalDate()));
    }

    public Map<LocalDate, List<Mobile>> getMobilesGroupByUpdated() {
        List<Mobile> allMobiles = mobileRepository.findAll();
        return allMobiles.stream().collect(Collectors.groupingBy(mobile -> mobile.getUpdatedDate().toLocalDate()));
    }


}

