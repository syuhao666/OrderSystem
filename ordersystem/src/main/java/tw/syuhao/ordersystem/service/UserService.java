package tw.syuhao.ordersystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.repository.UserRepository;
import tw.syuhao.ordersystem.utils.BCrypt;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public Users register(String username, String password, String email) throws Exception {
        if(repo.findByEmail(email) != null){
            throw new Exception();
        }

        String hashPasswd = BCrypt.hashpw(password, BCrypt.gensalt());

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(hashPasswd);
        user.setEmail(email);
        user.setRole("customer");
        
        return repo.save(user);
    }

    public Users login(String email, String password) {
		Users user = repo.findByEmail(email);
		if (user!=null && BCrypt.checkpw(password, user.getPassword())) {
			return user;
		}
		return null;
	}
     @Transactional(readOnly = true)
    public Users loadByPrincipal(String nameOrEmail) {
        if (nameOrEmail == null) return null;
        Users byEmail = repo.findByEmail(nameOrEmail.trim().toLowerCase());
        if (byEmail != null) return byEmail;
        return repo.findByUsername(nameOrEmail.trim());
    }
    /** 更新個資（username / email） */
    @Transactional
    public Users updateProfile(Long userId, String username, String email) {
        Users u = repo.findById(userId).orElseThrow();
        if (username != null && !username.trim().isEmpty()) u.setUsername(username.trim());
        if (email != null && !email.trim().isEmpty()) u.setEmail(email.trim().toLowerCase());
        try {
            return repo.save(u);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Email 已被使用");
        }
    }

    /** 修改密碼（需驗證舊密碼） */
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        Users u = repo.findById(userId).orElseThrow();
        if (!BCrypt.checkpw(oldPassword, u.getPassword())) {
            throw new IllegalArgumentException("舊密碼不正確");
        }
        u.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        repo.save(u);
    }
}


