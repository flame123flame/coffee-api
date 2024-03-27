package coffee.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import coffee.lottoconfig.constant.LottoConfigConstant;
import framework.constant.ProjectConstant;
import framework.utils.GenerateRandomString;
import lombok.Data;

@Data
@Entity
@Table(name = "draft_lotto_class")
public class DraftLottoClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lotto_class_id")
    private Long lottoClassId;

    @Column(name = "lotto_class_code", unique = true, length = 40, nullable = false)
    private String lottoClassCode;

    @Column(name = "lotto_category_code", unique = true, length = 40, nullable = false)
    private String lottoCategoryCode;

    @Column(name = "class_name")
    private String className;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "rule_des")
    private String ruleDes;

    @Column(name = "type_installment")
    private String typeInstallment;

    @Column(name = "create_status")
    private String createStatus = LottoConfigConstant.CREATE_TYPE.INIT;

    @Column(name = "view_status")
    private String viewStatus = ProjectConstant.STATUS.HIDE;

    @Column(name = "group_list")
    private String groupList;

    @Column(name = "affiliate_list")
    private String affiliateList;

    @Column(name = "time_after_buy")
    private Integer timeAfterBuy;

    @Column(name = "time_before_lotto")
    private Integer timeBeforeLotto;

    private String lottoClassImg;

    private String lottoClassColor;

    @Column(name = "close_message")
    private String closeMessage;

    @Column(name = "prefix_trans_number")
    private String prefixTransNumber;

    @Column(name = "count_refund")
    private Integer countRefund;

    @Column(name = "auto_update_wallet")
    private Boolean autoUpdateWallet;

    @Column(name = "ignore_weekly")
    private Boolean ignoreWeekly;

    @Column(name = "draft_code")
    private String draftCode = GenerateRandomString.generateUUID();

    @Column(name = "remark_version")
    private String remarkVersion;
}
