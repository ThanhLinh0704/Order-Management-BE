package fpt.linhlt.order_management_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createAt;

    @CreatedBy
    @Column(name = "created_by")
    private String createBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updateBy;

    @Column(name = "deleted")
    private boolean deleted = false;

}
