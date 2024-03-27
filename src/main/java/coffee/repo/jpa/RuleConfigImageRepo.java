package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import coffee.model.RuleConfigImage;

public interface RuleConfigImageRepo extends CrudRepository<RuleConfigImage, Long> {
	
	List<RuleConfigImage> findByClassCode(String classCode);

	RuleConfigImage findByRuleConfigImageCode(String ruleCode);
	
	void deleteByRuleConfigImageCode(String ruleCode);
}
