package com.cms.reception.entity;

import lombok.Data;
import javax.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@Table(name = "college")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class College {
    private static final Logger logger = LoggerFactory.getLogger(College.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
} 