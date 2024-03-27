package coffee.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import framework.utils.GenerateRandomString;
import lombok.Data;

@Data
@Entity
@Table(name = "approve_class")
public class ApproveClass implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approve_class_id")
    private Long approveClassId;

    @Column(name = "approve_class_code")
    private String approveClassCode = GenerateRandomString.generateUUID();

    @Column(name = "lotto_class_code")
    private String lottoClassCode;

    @Column(name = "draft_code")
    private String draftCode;

    @Column(name = "is_approve")
    private Boolean isApprove;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private Date updatedAt;

}