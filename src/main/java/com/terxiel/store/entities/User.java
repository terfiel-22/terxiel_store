package com.terxiel.store.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private List<Address> addresses = new ArrayList<Address>();

    public void addAddress(Address address)
    {
        addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(Address address)
    {
        addresses.remove(address);
        address.setUser(null);
    }

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_tags",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    public Set<Tag> tags = new HashSet<Tag>();

    public void addTag(String tagName)
    {
        var tag = new Tag(tagName);
        tags.add(tag);
        tag.users.add(this);
    }

    public void removeTag(String tagName)
    {
        var tag = new Tag(tagName);
        tags.remove(tag);
        tag.users.remove(this);
    }

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Profile profile;

    public void addProfile(Profile profile)
    {
        this.setProfile(profile);
        profile.setUser(this);
    }

    @ManyToMany
    @Builder.Default
    @JoinTable(
            name = "wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    public Set<Product> wishlist = new HashSet<Product>();

    public void addWishlist(Product product)
    {
        wishlist.add(product);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "email = " + email + ", " +
                ")";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }
}
