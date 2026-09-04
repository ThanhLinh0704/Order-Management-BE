package fpt.linhlt.order_management_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
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

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createAt;

    @CreatedBy
    @Column(name = "created_by")
    private String createBy;

    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @LastModifiedBy
    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "deleted")
    private boolean deleted;

}
