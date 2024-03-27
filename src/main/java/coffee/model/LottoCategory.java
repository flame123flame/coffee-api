package coffee.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "lotto_category")
public class LottoCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="lotto_category_id")
    private Long lottoCategoryId;
    @Column(name="lotto_category_code")
    private String lottoCategoryCode;
    @Column(name="type_name")
    private String typeName;
    @Column(name="created_by")
    private String createdBy;
    @Column(name="created_at")
    private Date createdAt;
    @Column(name="updated_by")
    private String updatedBy;
    @Column(name="updated_at")
    private Date updatedAt;
}
