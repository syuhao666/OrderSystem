package tw.syuhao.ordersystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.repository.UserRepository;
import tw.syuhao.ordersystem.utils.BCrypt;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public Users register(String username, String password, String name, String email) throws Exception {
        if(repo.findByUsername(username) != null){
            throw new Exception();
        }

        String hashPasswd = BCrypt.hashpw(password, BCrypt.gensalt());

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(hashPasswd);
        user.setName(name);
        user.setUsername(username);
        
        return repo.save(user);
    }

    public Users login(String username, String password) {
		Users user = repo.findByUsername(username);
		if (user!=null && BCrypt.checkpw(password, user.getPassword())) {
			return user;
		}
		return null;
	}
}
