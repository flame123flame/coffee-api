package coffee.grouplist.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.grouplist.vo.GroupListRes;
import coffee.model.GroupListMapping;
import coffee.model.LottoClass;
import coffee.repo.jpa.GroupListMappingRepo;
import coffee.repo.jpa.LottoClassRepository;
import framework.utils.UserLoginUtil;

@Service
public class GroupListService {

    @Autowired
    private GroupListMappingRepo groupListMappingRepo;

    @Autowired
    private LottoClassRepository lottoClassRepo;

    public void add(String classCode, String groupCode) {
        GroupListMapping entity = new GroupListMapping();
        entity.setGroupListMappingCode(groupCode);
        entity.setClassCode(classCode);
        entity.setCreatedBy(UserLoginUtil.getUsername());
        groupListMappingRepo.save(entity);
    }

    public List<GroupListRes> get(String groupCode) {
        List<GroupListMapping> listEntity = groupListMappingRepo.findByGroupListMappingCode(groupCode);

        List<LottoClass> classList = lottoClassRepo.findAll();

        List<GroupListRes> list = new ArrayList<>();
        GroupListRes dataSet;
        for (GroupListMapping entity : listEntity) {
            dataSet = new GroupListRes();
            dataSet.setId(entity.getGroupListMappingId());
            dataSet.setGroupListMappingCode(entity.getGroupListMappingCode());
            dataSet.setClassCode(entity.getClassCode());
            dataSet.setClassName(
                    classList.stream().filter(item -> item.getLottoClassCode().equals(entity.getClassCode()))
                            .findFirst().orElse(new LottoClass()).getClassName());
            list.add(dataSet);
        }
        return list;
    }
    
    public List<GroupListRes> getAll() {
        List<GroupListMapping> listEntity = groupListMappingRepo.findAll();

        List<LottoClass> classList = lottoClassRepo.findAll();

        List<GroupListRes> list = new ArrayList<>();
        GroupListRes dataSet;
        for (GroupListMapping entity : listEntity) {
            dataSet = new GroupListRes();
            dataSet.setId(entity.getGroupListMappingId());
            dataSet.setGroupListMappingCode(entity.getGroupListMappingCode());
            dataSet.setClassCode(entity.getClassCode());
            dataSet.setClassName(
                    classList.stream().filter(item -> item.getLottoClassCode().equals(entity.getClassCode()))
                            .findFirst().orElse(new LottoClass()).getClassName());
            list.add(dataSet);
        }
        return list;
    }

    public void delete(Long id) {
        groupListMappingRepo.deleteById(id);
    }

}