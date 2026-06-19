package com.terxiel.store.services;


import com.terxiel.store.entities.Address;
import com.terxiel.store.entities.Profile;
import com.terxiel.store.entities.Tag;
import com.terxiel.store.entities.User;
import com.terxiel.store.repositories.AddressRepository;
import com.terxiel.store.repositories.ProfileRepository;
import com.terxiel.store.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final AddressRepository addressRepository;
    private final EntityManager entityManager;


    @Transactional
    public void showEntityStates()
    {
        // 1. TRANSIENT STATE
        // The object is created in Java memory. It has no ID and JPA doesn't track it.
        var user = User.builder()
                .name("Terciel Kenway")
                .email("terciel@gmail.com")
                .password("admin@123")
                .build();
        if(entityManager.contains(user))
            System.out.println("Persistent");
        else
            System.out.println("Transient / Detached");


        // 2. PERSISTENT STATE
        // Saving the entity adds it to the Persistence Context. It now has a database ID.
        userRepository.save(user);
        if(entityManager.contains(user))
            System.out.println("Persistent");
        else
            System.out.println("Transient / Detached");


        // 3. DIRTY CHECKING (Still Managed)
        // Because the object is managed, you DO NOT need to call save() again!
        // JPA tracks this modification and automatically issues an SQL UPDATE statement
        // right before the transaction commits (flushes).
        var profile = Profile.builder()
                .bio("He/Him")
                .dateOfBirth(LocalDate.of(2003,10,7))
                .loyaltyPoints(100)
                .phoneNumber("09384756387")
                .build();
        var address = Address.builder()
                .barangay("Halang")
                .cityMunicipality("Naic")
                .province("Cavite")
                .zipCode("4110")
                .country("Philippines")
                .build();

        user.addProfile(profile);
        user.addAddress(address);
        user.addTag("Admin");

        if(entityManager.contains(user))
            System.out.println("Persistent");
        else
            System.out.println("Transient / Detached");
    }

    @Transactional
    public void showRelatedEntities()
    {
        var profile = profileRepository.findById(10L).orElseThrow(()->new RuntimeException("No profile found."));

        System.out.println(profile.getUser().getName());
    }

    public void fetchAddress()
    {
        var address = addressRepository.findById(1L).orElseThrow(()->new RuntimeException("No address found."));

        System.out.println(address);
    }

    public void persistRelated()
    {
        var user = User.builder()
                .name("Terxiel Kenway")
                .email("terxiel@gmail.com")
                .password("admin@123")
                .build();

        var profile = Profile.builder()
                .bio("He/Him")
                .phoneNumber("09384726482")
                .dateOfBirth(LocalDate.of(2003,10,7))
                .loyaltyPoints(100)
                .build();

        var address = Address.builder()
                .houseNumber("Blk 24 Lot 1")
                .streetSubdivision("Harbor Homes")
                .barangay("Halang")
                .cityMunicipality("Naic")
                .province("Cavite")
                .zipCode("4110")
                .country("Philippines")
                .build();

        user.addProfile(profile);
        user.addAddress(address);

        userRepository.save(user);
    }

    @Transactional
    public void deleteRelated()
    {
        var user = userRepository.findById(20L).orElseThrow(()->new RuntimeException("User not found"));
        var address = user.getAddresses().getFirst();

        user.removeAddress(address);
        userRepository.save(user);
    }

    @Transactional
    public void fetchUser()
    {
        var user = userRepository.findByEmail("terciel@gmail.com").orElseThrow(()->new RuntimeException("User not found."));
        System.out.println(user);
    }

    @Transactional
    public void fetchUsers()
    {
        var users = userRepository.findAllWithAddresses();
        users.forEach(u->{
            System.out.println(u);
            u.getAddresses().forEach(System.out::println);
        });
    }
}
