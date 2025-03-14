package com.rainlf.mgttbe.infra.db.manager;

import com.rainlf.mgttbe.infra.db.dataobj.UserDO;
import com.rainlf.mgttbe.infra.db.repository.UserRepository;
import com.rainlf.mgttbe.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManager {
    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return toUser(userRepository.save(toUserDO(user)));
    }

    public List<User> save(List<User> users) {
        return userRepository.saveAll(users.stream().map(this::toUserDO).toList())
                .stream()
                .map(this::toUser)
                .toList();
    }

    public User findById(Integer userId) {
        UserDO userDO = userRepository.findByIdAndIsDeleted(userId, 0);
        if (userDO == null) {
            throw new RuntimeException("user not found, id: " + userId);
        }
        return toUser(userDO);
    }

    public User findByOpenId(String openid) {
        UserDO userDO = userRepository.findByOpenId(openid).orElse(null);
        return toUser(userDO);
    }

    public int updateUserPoint(Integer userId, Integer point) {
        return userRepository.updateUserPoint(userId, point);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(x -> x.getIsDeleted() == 0)
                .map(this::toUser)
                .toList();
    }

    private User toUser(UserDO userDO) {
        if (userDO == null) {
            return null;
        }

        User user = new User();
        BeanUtils.copyProperties(userDO, user);
        user.setDeleted(userDO.getIsDeleted() == 1);
        return user;
    }

    private UserDO toUserDO(User user) {
        if (user == null) {
            return null;
        }

        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(user, userDO);
        userDO.setIsDeleted(user.isDeleted() ? 1 : 0);
        return userDO;
    }

}
