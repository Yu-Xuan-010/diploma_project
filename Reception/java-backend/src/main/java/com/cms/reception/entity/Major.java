package com.cms.reception.entity;

import lombok.Data;
import javax.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@Table(name = "major")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Major {
    private static final Logger logger = LoggerFactory.getLogger(Major.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "college_id", nullable = false)
    private Long collegeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private College college;
} 