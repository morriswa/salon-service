package org.morriswa.salon.control;

import org.morriswa.salon.model.PublicEmployeeProfile;
import org.morriswa.salon.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @RequestMapping("/public")
public class PublicController {
    private final ProfileService profileService;

    @Autowired
    public PublicController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/featuredEmployees")
    public ResponseEntity<List<PublicEmployeeProfile>> retrieveFeaturedEmployees() throws Exception {
        final var response = profileService.retrieveFeaturedEmployees();
        return ResponseEntity.ok(response);
    }

    /**
     * Http GET endpoint used to retrieve all stored information about the currently authenticated user
     *
     * @return profile and contact information about the user if operation was successful, else error response
     */
    @GetMapping("/profile/{employeeId}")
    public ResponseEntity<PublicEmployeeProfile> getPublicEmployeeProfile(
            @PathVariable Long employeeId
    ) throws Exception{
        // using the user profile service, retrieve the current users profile
        var profile = profileService.getPublicEmployeeProfile(employeeId);
        // and return it to them in JSON format
        return ResponseEntity.ok(profile);
    }
}
