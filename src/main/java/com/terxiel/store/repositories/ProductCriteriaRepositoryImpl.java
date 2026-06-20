package com.terxiel.store.repositories;

import com.terxiel.store.entities.Category;
import com.terxiel.store.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class ProductCriteriaRepositoryImpl implements ProductCriteriaRepository {
    @PersistenceContext
    private final EntityManager entityManager;


    @Override
    public List<Product> findProductsByCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // SELECT * FROM product
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);

        // FROM product
        Root<Product> root = cq.from(Product.class);

        // List of conditions
        List<Predicate> predicates = new ArrayList<>();

        if(name != null)
        {
            // name LIKE '%name%'
            predicates.add(cb.like(root.get("name"),"%"+name+"%"));
        }
        if(minPrice != null)
        {
            // price >= minPrice
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if(maxPrice != null)
        {
            // price <= maxPrice
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        // SELECT * FROM product WHERE ...
        cq.select(root).where(predicates.toArray(new Predicate[0]));

        // At this point the query is prepared but not executed.
        TypedQuery<Product> query = entityManager.createQuery(cq);

        // This sends the SQL to the database.
        // The database returns matching rows.
        // JPA converts each row into a Product object.
        return query.getResultList();
    }

    @Override
    public List<Product> findProductsByCategory(String categoryName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);

        // Join the Product entity with its Category field
        // Replace "category" with the exact field name defined in your Product class
        Join<Product, Category> categoryJoin = root.join("category");

        Predicate predicate = cb.equal(categoryJoin.get("name"), categoryName);
        cq.select(root).where(predicate);

        TypedQuery<Product> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
