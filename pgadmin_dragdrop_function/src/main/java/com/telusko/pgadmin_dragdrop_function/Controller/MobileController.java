package com.telusko.pgadmin_dragdrop_function.Controller;

import com.telusko.pgadmin_dragdrop_function.Model.Mobile;
import com.telusko.pgadmin_dragdrop_function.Service.MobileService;
import jakarta.annotation.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class MobileController {

    @Autowired
    private MobileService mobileService;

    // Endpoint to save a Mobile entity
    @PostMapping("/add")
    public ResponseEntity<Mobile> saveMobile(@RequestBody Mobile mobile) {
        Mobile savedMobile = mobileService.saveMobile(mobile);
        return new ResponseEntity<>(savedMobile, HttpStatus.CREATED);  // Return 201 Created status
    }


    // Get paginated results (no filter), fixed size = 10
    @GetMapping("/paginated")
    public ResponseEntity<Page<Mobile>> getAllMobilesPaginated(@RequestParam("page") int page) {
        int size = 10;  // fix Size to 10 per page
        Page<Mobile> mobiles = mobileService.getAllMobilesPaginated(page, size);
        return ResponseEntity.ok(mobiles);
    }

    // Get mobiles by created date range with pagination
    @GetMapping("/filter/created")
    public ResponseEntity<Page<Mobile>> getMobilesByCreatedDateRange(
            @RequestParam("startCreatedDate") String startCreatedDate,
            @RequestParam("endCreatedDate") String endCreatedDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        LocalDateTime startCreated = LocalDateTime.parse(startCreatedDate);
        LocalDateTime endCreated = LocalDateTime.parse(endCreatedDate);

        Page<Mobile> mobiles = mobileService.getMobilesByCreatedDateRange(startCreated, endCreated, page, size);

        return ResponseEntity.ok(mobiles);
    }

    // Get mobiles by updated date range with pagination
    @GetMapping("/filter/updated")
    public ResponseEntity<Page<Mobile>> getMobilesByUpdatedDateRange(
            @RequestParam("startUpdatedDate") String startUpdatedDate,
            @RequestParam("endUpdatedDate") String endUpdatedDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5 ") int size
    ) {
        LocalDateTime startUpdated = LocalDateTime.parse(startUpdatedDate);
        LocalDateTime endUpdated = LocalDateTime.parse(endUpdatedDate);

        Page<Mobile> mobiles = mobileService.getMobilesByUpdatedDateRange(startUpdated, endUpdated, page, size);

        return ResponseEntity.ok(mobiles);
    }

    // Update a mobile by id
    @PutMapping("updates/{id}")
    public ResponseEntity<Mobile> updateMobile(
            @PathVariable Long id,
            @RequestBody Mobile mobileDetails) {
        Mobile updatedMobile = mobileService.updateMobile(id, mobileDetails);
        return ResponseEntity.ok(updatedMobile);
    }
    @GetMapping("/grouped-by-date")
    public ResponseEntity<Map<LocalDate, List<Mobile>>> getMobilesGroupedByDate() {
        Map<LocalDate, List<Mobile>> groupedMobiles = mobileService.getMobilesGroupedByDate();
        return ResponseEntity.ok(groupedMobiles);
    }
    //groups by updated-date
    @GetMapping("/grouped-by-updated-date")
    public ResponseEntity<Map<LocalDate, List<Mobile>>> getMobilesGroupedByUpdatedDate() {
        Map<LocalDate, List<Mobile>> groupedMobiles = mobileService.getMobilesGroupByUpdated();
        return ResponseEntity.ok(groupedMobiles);
    }

}

