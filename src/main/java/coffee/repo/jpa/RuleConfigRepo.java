package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.RuleConfig;

@Repository
public interface RuleConfigRepo extends CrudRepository<RuleConfig, Long> {
	
	List<RuleConfig> findByClassCode(String classCode);

	RuleConfig findByRuleConfigCode(String ruleCode);

	void deleteByRuleConfigCode(String ruleCode);
}
