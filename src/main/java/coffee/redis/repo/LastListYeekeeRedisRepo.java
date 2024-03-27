package coffee.redis.repo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import coffee.buy.vo.res.YeeKeeNumberSubmitListRes;
import coffee.redis.model.YeekeeLastList;
import coffee.yeekeeResult.vo.res.YeekeeResultRoundSeqRes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class LastListYeekeeRedisRepo {

    @SuppressWarnings("rawtypes")
    private final HashOperations hashOperations;
    private final String key = "LastListYeekee:";
    private final StringRedisTemplate redisTemplate;

    public LastListYeekeeRedisRepo(
            @Qualifier(value = "redisLastListYeekeeTemplate") final StringRedisTemplate redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
        this.redisTemplate = redisTemplate;
    }

    private String generateKey(final String classCode, final String installment, final int round) {
        return this.key + ":" + classCode + ":" + installment + ":" + String.valueOf(round);
    }

    public YeekeeLastList getLastList(final String classCode, final String installment, final int round) {
        final String keySave = generateKey(classCode, installment, round);
        final Map<String, String> value = hashOperations.entries(keySave);
        if (value.size() == 0)
            return null;
        YeekeeLastList dataRes = null;
        try {
            dataRes = new Gson().fromJson(value.get("value"), YeekeeLastList.class);
        } catch (Exception e) {
            return null;
        }
        return dataRes;
    }

    public void setLastList(String classCode, String installment, int round, BigDecimal numberSum,
            List<YeeKeeNumberSubmitListRes> listLast20, List<YeekeeResultRoundSeqRes> listSeq) {
        final String keySave = generateKey(classCode, installment, round);
        final YeekeeLastList dataSet = new YeekeeLastList(numberSum, listLast20, listSeq);
        setList(dataSet, keySave);
        redisTemplate.expire(keySave, 10, TimeUnit.MINUTES);
    }

    public void setLastListDay(String classCode, String installment, int round, BigDecimal numberSum,
            List<YeeKeeNumberSubmitListRes> listLast20, List<YeekeeResultRoundSeqRes> listSeq, BigDecimal numberResult,
            BigDecimal num16) {
        final String keySave = generateKey(classCode, installment, round);
        final YeekeeLastList dataSet = new YeekeeLastList(numberSum, listLast20, listSeq);
        dataSet.setNumberResult(numberResult);
        dataSet.setNum16(num16);

        setList(dataSet, keySave);
        redisTemplate.expire(keySave, 1, TimeUnit.DAYS);
    }

    private void setList(YeekeeLastList dataSet, String keySave) {
        final Map<String, String> map = new HashMap<>();
        String req = "";
        try {
            req = new ObjectMapper().writeValueAsString(dataSet);
        } catch (final Exception e) {
            return;
        }
        map.put("value", req);
        redisTemplate.opsForHash().putAll(keySave, map);
    }
}