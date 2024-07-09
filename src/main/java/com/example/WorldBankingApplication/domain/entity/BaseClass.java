package com.example.WorldBankingApplication.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@EnableJpaAuditing
@Getter
@Setter
public class BaseClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate //this helps to create a date when the entity is been saved in the system
    private LocalDateTime dateCreated;

    @LastModifiedDate
    private LocalDateTime dateModified;

    @Override //This makes our app professional by customizing the equals method
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (obj== null || getClass()!= obj.getClass()) return false;
        BaseClass that = (BaseClass) obj;

        return Objects.equals(id, that.id);
    }

    @PrePersist
    @PreUpdate
    //This allows us to persist and allow the created date to and modify date to hit the database
    public void prePersist(){
        if (dateCreated == null){
            dateCreated= LocalDateTime.now();
        }
        dateModified=LocalDateTime.now();
    }

    @Override
    public int hashCode(){
        return id != null ? id.hashCode(): 0;
    }
}
