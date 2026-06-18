package com.terxiel.store.services;


import com.terxiel.store.entities.User;
import com.terxiel.store.repositories.AddressRepository;
import com.terxiel.store.repositories.ProfileRepository;
import com.terxiel.store.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


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
                .name("Terxiel Kenway")
                .email("terxiel@gmail.com")
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
        user.setName("Terciel Kenway");
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
}
