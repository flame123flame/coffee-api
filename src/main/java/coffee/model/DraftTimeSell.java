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

@Entity
@Data
@Table(name = "draft_time_sell")
public class DraftTimeSell implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_sell_id")
    private Long timeSellId;

    @Column(name = "time_sell_code", length = 40, nullable = false)
    private String timeSellCode;

    @Column(name = "lotto_class_code", length = 40, nullable = false)
    private String lottoClassCode;

    @Column(name = "time_open", nullable = false)
    private Date timeOpen;

    @Column(name = "time_close", nullable = false)
    private Date timeClose;

    @Column(name = "created_by", length = 255, nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private Date createdAt = new Date();

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "draft_code")
    private String draftCode;
}
