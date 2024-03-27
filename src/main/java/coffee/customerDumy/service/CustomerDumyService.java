package coffee.customerDumy.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import coffee.customerDumy.vo.CustomerDumyReq;
import coffee.customerDumy.vo.CustomerDumyRes;
import coffee.model.CustomerDumy;
import coffee.repo.dao.CustomerDumyDao;
import coffee.repo.jpa.CustomerDumyRepo;
import framework.utils.GenerateRandomString;
import framework.utils.UserLoginUtil;

@Service
public class CustomerDumyService {
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomerDumyRepo customerDumyRepo;
	
	@Autowired
	private CustomerDumyDao customerDumyDao;
	
	public List<CustomerDumyRes> getAllCustomerDumy() {
		List<CustomerDumy> cusDumy = customerDumyDao.getAll();
        List<CustomerDumyRes> resData = new ArrayList<>();
        for(CustomerDumy item:cusDumy) {
        	CustomerDumyRes resItem = new CustomerDumyRes();
        	resItem.setUsername(item.getUsername());
        	resItem.setDumyCode(item.getDumyCode());
        	resItem.setCreatedBy(item.getCreatedBy());
        	resItem.setCreatedDate(item.getCreatedDate());
        	resData.add(resItem);
        }
		return resData;
		
	}

	public CustomerDumyRes getCustomerDumy(String code) {
		CustomerDumy item = customerDumyRepo.findByDumyCode(code);
		CustomerDumyRes DataRes = new CustomerDumyRes();
		DataRes.setUsername(item.getUsername());
		DataRes.setDumyCode(item.getDumyCode());
		DataRes.setCreatedBy(item.getCreatedBy());
		DataRes.setCreatedDate(item.getCreatedDate());
		
		return DataRes;
	}

	public void saveCustomerDumy(CustomerDumyReq req) {
		CustomerDumy item = new CustomerDumy();
		item.setUsername(req.getUsername());
		item.setDumyCode(GenerateRandomString.generateUUID());
		item.setCreatedBy(UserLoginUtil.getUsername());
		customerDumyRepo.save(item);
	}

	@Transactional
	public void deleteByCustomerDumy(String code) {
		customerDumyRepo.deleteByDumyCode(code);
	}

	public CustomerDumyRes updateCustomerDumy(String code) {
		CustomerDumy item = customerDumyRepo.findByDumyCode(code);
		CustomerDumyRes DataRes = new CustomerDumyRes();
		DataRes.setUsername(item.getUsername());
		DataRes.setDumyCode(item.getDumyCode());
		DataRes.setCreatedBy(item.getCreatedBy());
		DataRes.setCreatedDate(item.getCreatedDate());
		return DataRes;
	}

}
