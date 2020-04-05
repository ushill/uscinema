package com.ushill.service.impl.search;

import com.github.pagehelper.PageHelper;
import com.ushill.DTO.search.*;
import com.ushill.DTO.search.item.UserFastSearchResItemDTO;
import com.ushill.DTO.search.item.UserRefineSearchResItemDTO;
import com.ushill.enums.RefineSearchType;
import com.ushill.mapper.UserMapper;
import com.ushill.models.User;
import com.ushill.service.interfaces.search.UserSearchService;
import com.ushill.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSearchServiceImpl implements UserSearchService {

    @Autowired
    private UserMapper userMapper;

    @Value("${uscinema.limit.users.fast-search.critics}")
    private int maxCriticsCnt;

    @Value("${uscinema.limit.users.fast-search.users}")
    private int maxUsersCnt;

    @Value("${uscinema.limit.users.refine-search.users}")
    private int usersPerPage;

    @Value("${uscinema.limit.users.refine-search.critics}")
    private int criticsPerPage;

    @Override
    public List<UserFastSearchResItemDTO> fastSearch(List<String> paraList, boolean isCritic) {
        List<UserFastSearchResItemDTO> list = new ArrayList<>();
        int maxCnt = isCritic ? maxCriticsCnt : maxUsersCnt;

        PageHelper.startPage(1, maxCnt);
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 1);
        criteria.andEqualTo("isCritic", isCritic);
        paraList.forEach(para -> criteria.andLike("nickname", "%" + para + "%"));

        List<User> usersList = userMapper.selectByExample(example);
        usersList.forEach(user -> list.add(new UserFastSearchResItemDTO(user, paraList)));
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserSearchRefineResultDTO refineSearchCritics(List<String> paraList, int page) {
        PageHelper.startPage(page, this.criticsPerPage);
        List<User> userList = refineSearch(paraList, RefineSearchType.CRITICS);
        PagedGridResult<User> paged = PagedGridResult.setPagedGrid(userList, page);

        List<UserRefineSearchResItemDTO> list = new ArrayList<>();
        paged.getRows().forEach(user -> list.add(new UserRefineSearchResItemDTO(user, RefineSearchType.CRITICS, paraList)));

        UserSearchRefineResultDTO userSearchRefineResultDTO = new UserSearchRefineResultDTO();
        userSearchRefineResultDTO.setUserResList(list);
        userSearchRefineResultDTO.setHasNext(page < paged.getTotal());

        return userSearchRefineResultDTO;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserSearchRefineResultDTO refineSearchUsers(List<String> paraList, int page) {
        PageHelper.startPage(page, this.usersPerPage);
        List<User> userList = refineSearch(paraList, RefineSearchType.USERS);
        PagedGridResult<User> paged = PagedGridResult.setPagedGrid(userList, page);

        List<UserRefineSearchResItemDTO> list = new ArrayList<>();
        paged.getRows().forEach(user -> list.add(new UserRefineSearchResItemDTO(user, RefineSearchType.USERS, paraList)));

        UserSearchRefineResultDTO userSearchRefineResultDTO = new UserSearchRefineResultDTO();
        userSearchRefineResultDTO.setUserResList(list);
        userSearchRefineResultDTO.setHasNext(page < paged.getTotal());

        return userSearchRefineResultDTO;
    }

    private List<User> refineSearch(List<String> paraList, RefineSearchType type) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 1);
        paraList.forEach(para -> criteria.andLike("nickname", "%" + para + "%"));
        criteria.andEqualTo("isCritic", type == RefineSearchType.CRITICS);
        List<User> userList = userMapper.selectByExample(example);
        return userList;
    }
}


