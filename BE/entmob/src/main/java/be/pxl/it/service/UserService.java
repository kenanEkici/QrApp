package be.pxl.it.service;

import be.pxl.it.dao.users.UserRepository;
import be.pxl.it.model.domain.Party;
import be.pxl.it.model.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository _userRepository;

    @Autowired
    public UserService(UserRepository _userRepository) {
        this._userRepository = _userRepository;
    }

    public boolean add(User user) {
        User savedUser = _userRepository.save(user);
        return (savedUser != null);
    }

    public boolean delete(User user) {
        try {
            removeUser(user.getId());
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    public User update(User user) {
        return _userRepository.save(user);
    }

    public Iterable<User> getAllUsers() {
        return _userRepository.findAll();
    }

    public User getByCardNumber(String cardNumber) {
        return _userRepository.findByCardNumber(cardNumber);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        be.pxl.it.model.domain.User user = _userRepository.findByCardNumber(username);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getCardNumber(), user.getPassword(), true, true, true, true,
                    AuthorityUtils.createAuthorityList(user.getRole()));
        } else {
            throw new UsernameNotFoundException("could not find the user '"
                    + username + "'");
        }
    }

    private void removeUser(int userId){
        User userToBeRemoved = _userRepository.findOne(userId);
        userToBeRemoved.removeAllRelatonsFromUser();
        _userRepository.save(userToBeRemoved); //Save the new entity without the relations
        _userRepository.delete(userToBeRemoved); //delete the detached entity
    }

    public Iterable<Party> getPartiesOfUser(int userId){
        User u = _userRepository.findOne(userId);
        return u.getAssociatedParties();
    }
}
