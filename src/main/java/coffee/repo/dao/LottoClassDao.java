package coffee.repo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import coffee.masterdata.vo.BoSyncRes;

@Repository
@SuppressWarnings("deprecation")
public class LottoClassDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<BoSyncRes> getAllLottoClass() {
        StringBuilder sqlBuilder = new StringBuilder();
        List<Object> params = new ArrayList<Object>();
        sqlBuilder.append(
                " SELECT lotto_class_code,class_name,lotto_category_code from lotto_class  order by lotto_category_code asc ");
        List<BoSyncRes> dataRes = null;
        try {
            dataRes = this.jdbcTemplate.query(sqlBuilder.toString(), params.toArray(),
                    BeanPropertyRowMapper.newInstance(BoSyncRes.class));
        } catch (Exception e) {
            dataRes = new ArrayList<>();
        }
        return dataRes;
    }
}