package coffee.lottoconfig.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.lottoconfig.vo.req.RuleDetailReq;
import coffee.lottoconfig.vo.req.RuleDetailReq.RuleDes;
import coffee.lottoconfig.vo.res.RuleDetailRes;
import coffee.model.RuleConfig;
import coffee.model.RuleConfigImage;
import coffee.repo.jpa.RuleConfigImageRepo;
import coffee.repo.jpa.RuleConfigRepo;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE.SAVE;
import framework.model.ImgUploadRes;
import framework.utils.GenerateRandomString;
import framework.utils.ImgUploadUtils;
import framework.utils.UserLoginUtil;

@Service
public class RuleConfigService {

	@Autowired
	ImgUploadUtils imgUploadUtils;

	@Autowired
	RuleConfigRepo ruleConfigRepo;

	@Autowired
	RuleConfigImageRepo ruleConfigImageRepo;

	@Transactional
	public String addRulesDetail(RuleDetailReq req) throws Exception {

		for (RuleDetailReq.RuleDes ruleDesInfo : req.getRuleDesList()) {
			RuleConfig dataSet = new RuleConfig();
			ImgUploadRes ruleDesImage = null;
			if (ruleDesInfo.getImageRule() != null) {
				ruleDesImage = imgUploadUtils.uploadImg(req.getPrefix(), ruleDesInfo.getImageRule(), "lottoruleimage");
				dataSet.setImagePath(ruleDesImage.getData().getSavedPath());
			} else {
				dataSet.setImagePath(null);
			}
			dataSet.setClassCode(req.getClassCode());
			dataSet.setCreatedAt(new Date());
			dataSet.setCreatedBy(UserLoginUtil.getUsername());
			dataSet.setRuleConfigCode(GenerateRandomString.generateUUID());
			dataSet.setRuleDes(ruleDesInfo.getRuleDes());

			ruleConfigRepo.save(dataSet);
		}

		for (RuleDetailReq.RuleDes imageData : req.getRuleImageList()) {
			ImgUploadRes ruleDesImage = null;
			RuleConfigImage dataSet = new RuleConfigImage();

			if (imageData.getImageRule() != null) {
				ruleDesImage = imgUploadUtils.uploadImg(req.getPrefix(), imageData.getImageRule(), "lottoruleimage");
				dataSet.setClassCode(req.getClassCode());
				dataSet.setCreatedAt(new Date());
				dataSet.setCreatedBy(UserLoginUtil.getUsername());
				dataSet.setImagePath(ruleDesImage.getData().getSavedPath());
				dataSet.setRuleConfigImageCode(GenerateRandomString.generateUUID());
				ruleConfigImageRepo.save(dataSet);
			}
		}

		return SAVE.SUCCESS;
	}

	@Transactional
	public RuleDetailRes getRuleDetail(String classCode) {
		List<RuleConfig> dataRuleDesList = ruleConfigRepo.findByClassCode(classCode);
		List<RuleConfigImage> dataImageRuleList = ruleConfigImageRepo.findByClassCode(classCode);

		RuleDetailRes dataRes = new RuleDetailRes();

		dataRes.setClassCode(classCode);
		List<RuleDetailRes.RuleDesRes> ruleDesList = new ArrayList<RuleDetailRes.RuleDesRes>();
		for (RuleConfig ruleData : dataRuleDesList) {
			RuleDetailRes.RuleDesRes dataSet = new RuleDetailRes.RuleDesRes();
			dataSet.setRuleCode(ruleData.getRuleConfigCode());
			dataSet.setRuleId(ruleData.getRuleConfigId());
			dataSet.setRuleDes(ruleData.getRuleDes());
			dataSet.setImageRule(ruleData.getImagePath());
			ruleDesList.add(dataSet);
		}

		List<RuleDetailRes.RuleDesRes> imageRuleList = new ArrayList<RuleDetailRes.RuleDesRes>();
		for (RuleConfigImage imageData : dataImageRuleList) {
			RuleDetailRes.RuleDesRes imageSet = new RuleDetailRes.RuleDesRes();

			imageSet.setRuleId(imageData.getRuleConfigImageId());
			imageSet.setRuleCode(imageData.getRuleConfigImageCode());
			imageSet.setImageRule(imageData.getImagePath());
			imageRuleList.add(imageSet);
		}

		dataRes.setRuleDesList(ruleDesList);
		dataRes.setRuleImageList(imageRuleList);
		return dataRes;
	}

	public RuleDetailRes updateRuleDetail(RuleDetailReq req) throws Exception {

		List<RuleDes> ruleDesList = req.getRuleDesList();
		List<RuleDes> ruleImageList = req.getRuleImageList();

		for (RuleDes item : ruleDesList) {
			if (StringUtils.isNotBlank(item.getRuleCode())) {
				RuleConfig data = ruleConfigRepo.findByRuleConfigCode(item.getRuleCode());
				ImgUploadRes ruleDesImage = null;
				data.setUpdatedAt(new Date());
				data.setUpdatedBy(UserLoginUtil.getUsername());
				data.setRuleDes(item.getRuleDes());
				if (StringUtils.isNotBlank(item.getImageRule()) && StringUtils.isNotBlank(item.getOldImage())) {
					ruleDesImage = imgUploadUtils.uploadImg(req.getPrefix(), item.getImageRule(), "lottoruleimage");
					data.setImagePath(ruleDesImage.getData().getSavedPath());
					imgUploadUtils.delete(item.getOldImage());
				}
				ruleConfigRepo.save(data);
			}
			else {
				RuleConfig dataSet = new RuleConfig();
				ImgUploadRes ruleDesImage = null;
				if (item.getImageRule() != null) {
					ruleDesImage = imgUploadUtils.uploadImg(req.getPrefix(), item.getImageRule(), "lottoruleimage");
					dataSet.setImagePath(ruleDesImage.getData().getSavedPath());
				} else {
					dataSet.setImagePath(null);
				}
				dataSet.setClassCode(req.getClassCode());
				dataSet.setCreatedAt(new Date());
				dataSet.setCreatedBy(UserLoginUtil.getUsername());
				dataSet.setRuleConfigCode(GenerateRandomString.generateUUID());
				dataSet.setRuleDes(item.getRuleDes());
				ruleConfigRepo.save(dataSet);
				
			}
		}
		
		for (RuleDes item : ruleImageList) {
			if (StringUtils.isNotBlank(item.getRuleCode())) {
				RuleConfigImage data = ruleConfigImageRepo.findByRuleConfigImageCode(item.getRuleCode());
				ImgUploadRes ruleDesImage = null;
				data.setUpdatedAt(new Date());
				data.setUpdatedBy(UserLoginUtil.getUsername());
				if (StringUtils.isNotBlank(item.getImageRule()) && StringUtils.isNotBlank(item.getOldImage())) {
					ruleDesImage = imgUploadUtils.uploadImg(req.getPrefix(), item.getImageRule(), "lottoruleimage");
					data.setImagePath(ruleDesImage.getData().getSavedPath());
					imgUploadUtils.delete(item.getOldImage());
				}
				ruleConfigImageRepo.save(data);
			}
			else {
				ImgUploadRes ruleDesImage = null;
				RuleConfigImage dataSet = new RuleConfigImage();

				if (item.getImageRule() != null) {
					ruleDesImage = imgUploadUtils.uploadImg(req.getPrefix(), item.getImageRule(), "lottoruleimage");
					dataSet.setClassCode(req.getClassCode());
					dataSet.setCreatedAt(new Date());
					dataSet.setCreatedBy(UserLoginUtil.getUsername());
					dataSet.setImagePath(ruleDesImage.getData().getSavedPath());
					dataSet.setRuleConfigImageCode(GenerateRandomString.generateUUID());
					ruleConfigImageRepo.save(dataSet);
				}
			}
		}

		return null;
	}

	@Transactional
	public void deleteRuleConfigImage(String ruleCode) throws Exception {
		ruleConfigImageRepo.deleteByRuleConfigImageCode(ruleCode);
	}

	@Transactional
	public void deleteRuleConfig(String ruleCode) throws Exception {
		ruleConfigRepo.deleteByRuleConfigCode(ruleCode);
		
	}
}
