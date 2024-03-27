package coffee.masterdata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.model.MsdLottoKind;
import coffee.repo.dao.MsdLottoKindDao;
import coffee.repo.jpa.MsdLottoKindRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MsdLottoKindService {
	
	@Autowired
	private MsdLottoKindRepository msdLottoKindRepository;
	
	@Autowired
	private MsdLottoKindDao msdLottoKindDao;
	
	public List<MsdLottoKind> getAllMsd(){
		return msdLottoKindRepository.findAll();
	}

	public List<MsdLottoKind> getByClassCodeNotIn(String classCode){
		return msdLottoKindDao.findMsdLottoNotInKindByClassCode(classCode);
	}

	public List<MsdLottoKind> getByClassCodeIn(String classCode){
		return msdLottoKindDao.findMsdLottoInKindByClassCode(classCode);
	}
}
